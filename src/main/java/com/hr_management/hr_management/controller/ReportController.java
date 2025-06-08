package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.mapper.ReportMapper;
import com.hr_management.hr_management.model.dto.ApiResponseDto;
import com.hr_management.hr_management.model.dto.DepartmentHeadcountDTO;
import com.hr_management.hr_management.model.dto.report.EmployeeFullDetailsDTO;
import com.hr_management.hr_management.model.dto.JobDistributionDTO;
import com.hr_management.hr_management.model.dto.report.EmployeeRegionDTO;
import com.hr_management.hr_management.model.entity.*;
import com.hr_management.hr_management.repository.DepartmentRepository;
import com.hr_management.hr_management.repository.EmployeeRepository;
import com.hr_management.hr_management.repository.RegionRepository;
import com.hr_management.hr_management.utils.BuildResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final ReportMapper reportMapper;
    private final RegionRepository regionRepository;

    // Constructor injection of repositories and mapper
    public ReportController(EmployeeRepository employeeRepository,
                            DepartmentRepository departmentRepository,
                            ReportMapper reportMapper,
                            RegionRepository regionRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.reportMapper = reportMapper;
        this.regionRepository = regionRepository;
    }

    // Get department headcount report
    @GetMapping("/departments/headcount")
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
    @GetMapping("/jobs/distribution")
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
    @GetMapping("/employees_full_details")
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
    @GetMapping("/employees_by_region/{region_id}")
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
}






