package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.mapper.ReportMapper;
import com.hr_management.hr_management.model.dto.ApiResponseDto;
import com.hr_management.hr_management.model.dto.department.DepartmentHeadcountDTO;
import com.hr_management.hr_management.model.dto.report.*;
import com.hr_management.hr_management.model.dto.JobDistributionDTO;
import com.hr_management.hr_management.model.entity.*;
import com.hr_management.hr_management.repository.DepartmentRepository;
import com.hr_management.hr_management.repository.EmployeeRepository;
import com.hr_management.hr_management.repository.LocationRepository;
import com.hr_management.hr_management.repository.RegionRepository;
import com.hr_management.hr_management.utils.BuildResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final ReportMapper reportMapper;
    private final RegionRepository regionRepository;
    private final LocationRepository locationRepository;

    // Constructor injection of repositories and mapper
    public ReportController(EmployeeRepository employeeRepository,
                            DepartmentRepository departmentRepository,
                            ReportMapper reportMapper,
                            LocationRepository locationRepository,
                            RegionRepository regionRepository) {

        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.reportMapper = reportMapper;
        this.regionRepository = regionRepository;
        this.locationRepository = locationRepository;
    }

    // Get department headcount report
    @GetMapping("/departments-headcount")
    public ResponseEntity<ApiResponseDto> getDepartmentsHeadCount(HttpServletRequest request) {
        List<Department> departments = departmentRepository.findAll();

        // Map each department to DTO with headcount
        List<DepartmentHeadcountDTO> departmentHeadcountDTOList = departments.stream()
                .map(department -> new DepartmentHeadcountDTO(
                        department.getDepartmentId().longValue(),
                        department.getDepartmentName(),
                        department.getEmployees() == null ? 0L : department.getEmployees().size()
                ))
                .toList();

        return BuildResponse.success(departmentHeadcountDTOList,
                "Department headcount report",
                request.getRequestURI());
    }

    // Get job distribution report
    @GetMapping("/jobs-distribution")
    public ResponseEntity<ApiResponseDto> getJobDistribution(HttpServletRequest request) {
        List<Employee> employees = employeeRepository.findAll();

        // Group employees by job and count
        Map<Job, Long> jobCountMap = employees.stream()
                .collect(Collectors.groupingBy(Employee::getJob, Collectors.counting()));

        // Map to DTO list
        List<JobDistributionDTO> jobDistribution = jobCountMap.entrySet().stream()
                .map(entry -> new JobDistributionDTO(
                        entry.getKey().getJobId(),
                        entry.getKey().getJobTitle(),
                        entry.getValue()
                ))
                .toList();

        return BuildResponse.success(jobDistribution, "Job distribution report", request.getRequestURI());
    }

    // Get paginated employee full details with sorting
    @GetMapping("employees-details")
    public Page<EmployeeFullDetailsDTO> getEmployeesFullDetails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Employee> employeePage = employeeRepository.findAll(pageable);

        return employeePage.map(reportMapper::toEmployeeFullDetailsDTO);
    }

    // Get paginated employees by region with sorting
    @GetMapping("/employees-by-region/{region_id}")
    public ResponseEntity<ApiResponseDto> getEmployeesByRegion(
            HttpServletRequest request,
            @PathVariable BigDecimal region_id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "employeeId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Region region = regionRepository.findById(region_id)
                .orElseThrow(() -> new RuntimeException("Region not found"));

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Employee> employeePage = employeeRepository.findByDepartmentLocationCountryRegion(region, pageable);

        return BuildResponse.success(
                employeePage.map(reportMapper::toEmployeeRegionDTO),
                "Fetch  employee region",
                request.getRequestURI()
        );
    }


    // 3. Location Distribution Report
    @GetMapping("/location-distribution")
    public List<LocationDistributionDTO> getLocationDistribution() {
        return locationRepository.findAll().stream()
                .map(location -> {
                    long count = employeeRepository.countByDepartmentLocation(location);
                    return new LocationDistributionDTO(
                            location.getCity(),
                            location.getStateProvince(),
                            count
                    );
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/departments-average-salary")
    public ResponseEntity<ApiResponseDto> getAverageSalaryByDepartment(HttpServletRequest request,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size) {

        List<Department> departments = departmentRepository.findAll(PageRequest.of(page, size)).getContent();

        List<DepartmentSalaryDTO> result = departments.stream()
                .map(dept -> {
                    List<Employee> employees = dept.getEmployees();
                    BigDecimal avgSalary = BigDecimal.ZERO;

                    if (employees != null && !employees.isEmpty()) {
                        avgSalary = BigDecimal.valueOf(
                                employees.stream()
                                        .filter(emp -> emp.getSalary() != null)
                                        .mapToDouble(emp -> emp.getSalary().doubleValue())
                                        .average()
                                        .orElse(0.0)
                        ).setScale(2, RoundingMode.HALF_UP);
                    }

                    return new DepartmentSalaryDTO(dept.getDepartmentName(), avgSalary);
                }).toList();

        return BuildResponse.success(result, "Average salary by department", request.getRequestURI());
    }

    @GetMapping("/employees-hired-after")
    public ResponseEntity<ApiResponseDto> getEmployeesHiredAfter(
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<EmployeeBasicDTO> result = employeeRepository.findByHireDateAfter(date, pageable).stream()
                .map(emp -> new EmployeeBasicDTO(
                        emp.getEmployeeId(),
                        emp.getFirstName() + " " + emp.getLastName(),
                        emp.getHireDate(),
                        emp.getJob() != null ? emp.getJob().getJobTitle() : null
                )).toList();

        return BuildResponse.success(result, "Employees hired after " + date, request.getRequestURI());
    }

    @GetMapping("/employees-hired-before")
    public ResponseEntity<ApiResponseDto> getEmployeesHiredBefore(HttpServletRequest request,
                                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<EmployeeBasicDTO> result = employeeRepository.findByHireDateBefore(date, pageable).stream()
                .map(emp -> new EmployeeBasicDTO(
                        emp.getEmployeeId(),
                        emp.getFirstName() + " " + emp.getLastName(),
                        emp.getHireDate(),
                        emp.getJob() != null ? emp.getJob().getJobTitle() : null
                )).toList();

        return BuildResponse.success(result, "Employees hired before " + date, request.getRequestURI());
    }

    @GetMapping("/employees-salary-above")
    public ResponseEntity<ApiResponseDto> getEmployeesWithHighSalary(HttpServletRequest request,
                                                                     @RequestParam BigDecimal amount,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<EmployeeBasicDTO> result = employeeRepository.findBySalaryGreaterThan(amount, pageable).stream()
                .map(emp -> new EmployeeBasicDTO(
                        emp.getEmployeeId(),
                        emp.getFirstName() + " " + emp.getLastName(),
                        emp.getHireDate(),
                        emp.getJob() != null ? emp.getJob().getJobTitle() : null
                )).toList();

        return BuildResponse.success(result, "Employees with salary above " + amount, request.getRequestURI());
    }

    @GetMapping("/employees-top-paid")
    public ResponseEntity<ApiResponseDto> getTopPaidEmployees(
            HttpServletRequest request,
            @RequestParam int limit,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<EmployeeBasicDTO> result = employeeRepository.findAllByOrderBySalaryDesc().stream()
                .skip((long) page * size)
                .limit(size)
                .map(emp -> new EmployeeBasicDTO(
                        emp.getEmployeeId(),
                        emp.getFirstName() + " " + emp.getLastName(),
                        emp.getHireDate(),
                        emp.getJob() != null ? emp.getJob().getJobTitle() : null
                )).limit(limit).toList();

        return BuildResponse.success(result, "Top paid employees", request.getRequestURI());
    }

    @GetMapping("/salary-expense")
    public ResponseEntity<ApiResponseDto> getSalaryExpenseByYear( HttpServletRequest request,
            @RequestParam int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        BigDecimal total = employeeRepository.findByHireDateBetween(start, end).stream()
                .filter(emp -> emp.getSalary() != null)
                .map(Employee::getSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return BuildResponse.success(new SalaryExpenseDTO(year, total), "Salary expense for year " + year, request.getRequestURI());
    }
}






