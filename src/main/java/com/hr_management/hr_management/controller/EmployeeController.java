package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.exception.ResourceNotFoundException;
import com.hr_management.hr_management.mapper.EmployeeMapper;
import com.hr_management.hr_management.model.dto.ApiResponseDto;
import com.hr_management.hr_management.model.dto.EmployeeDTO;
import com.hr_management.hr_management.model.dto.EmployeeDetailDTO;
import com.hr_management.hr_management.model.entity.Department;
import com.hr_management.hr_management.model.entity.Employee;
import com.hr_management.hr_management.model.entity.Job;
import com.hr_management.hr_management.model.projection.ManagerGroupView;
import com.hr_management.hr_management.repository.DepartmentRepository;
import com.hr_management.hr_management.repository.EmployeeRepository;
import com.hr_management.hr_management.repository.JobRepository;
import com.hr_management.hr_management.utils.BuildResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    EmployeeRepository employeeRepository;
    EmployeeMapper employeeMapper;

    private final JobRepository jobRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeController(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper
    , JobRepository jobRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.jobRepository = jobRepository;
        this.departmentRepository = departmentRepository;
    }

    // Get all employees
    @GetMapping("employees")
    public ResponseEntity<ApiResponseDto> getAllEmployees(HttpServletRequest request) {
        List<EmployeeDTO> data = employeeRepository.findAll().stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
        return BuildResponse.success(data, "List of all Employee ", request.getRequestURI());
    }

    // get employee by hire date
    @GetMapping("employees/by_hire_date/{hireDate}")
    public ResponseEntity<ApiResponseDto> getEmployeesByHireDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hireDate,
            HttpServletRequest request) {

        List<EmployeeDTO> data = employeeRepository.findAllByHireDate(hireDate).stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());

        if (data.isEmpty()) {
            return BuildResponse.success(data, "No employees found for hire date: " + hireDate, request.getRequestURI());
        }

        return BuildResponse.success(data, "Employees fetched by hire date", request.getRequestURI());
    }

    // Get employee by id
    @GetMapping("employees/{id}")
    public ResponseEntity<ApiResponseDto> getEmployeeById(@PathVariable BigDecimal id, HttpServletRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
        EmployeeDetailDTO data = employeeMapper.toDetailDTO(employee);
        return BuildResponse.success(data, "Employee fetched successfully", request.getRequestURI());
    }

    // Get employee by department
    @GetMapping("employees/by_department/{dept_id}")
    public ResponseEntity<ApiResponseDto> getEmployeeByDepartmentId(@PathVariable BigDecimal dept_id, HttpServletRequest request) {
        List<EmployeeDTO> data = employeeRepository.findByDepartment_DepartmentId(dept_id).stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());

        if (data.isEmpty()) {
            return BuildResponse.success(data, "No employees found for Department ID: " + dept_id, request.getRequestURI());
        }

        return BuildResponse.success(data, "Employees fetched by Department ID", request.getRequestURI());
    }

    // Get employee by manager
    @GetMapping("employees/by_manager/{manager_id}")
    public ResponseEntity<ApiResponseDto> getEmployeeByManagerId(@PathVariable BigDecimal manager_id, HttpServletRequest request) {
        List<EmployeeDTO> data = employeeRepository.findByManagerEmployeeId(manager_id).stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());

        if (data.isEmpty()) {
            return BuildResponse.success(data, "No employees found for Manager ID: " + manager_id, request.getRequestURI());
        }

        return BuildResponse.success(data, "Employees fetched by Manager ID", request.getRequestURI());
    }

    // get employee by job id
    @GetMapping("employees/by_job/{job_id}")
    public ResponseEntity<ApiResponseDto> getEmployeeByJobId(@PathVariable String job_id, HttpServletRequest request) {
        List<EmployeeDTO> data = employeeRepository.findByJob_JobId(job_id).stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());

        if (data.isEmpty()) {
            return BuildResponse.success(data, "No employees found for Job ID: " + job_id, request.getRequestURI());
        }

        return BuildResponse.success(data, "Employees fetched by Job ID", request.getRequestURI());
    }

    // Get employee by email
    @GetMapping("employees/by_email/{email}")
    public ResponseEntity<ApiResponseDto> getEmployeeByEmail(@PathVariable String email, HttpServletRequest request) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));

        EmployeeDetailDTO data = employeeMapper.toDetailDTO(employee);
        return BuildResponse.success(data, "Employee fetched by email", request.getRequestURI());
    }

    // Get Employee by full name
    @GetMapping("employees/search")
    public ResponseEntity<ApiResponseDto> getEmployeeByFirstAndLastName(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            HttpServletRequest request) {

        Employee employee = employeeRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with name: " + firstName + " " + lastName));

        EmployeeDetailDTO data = employeeMapper.toDetailDTO(employee);

        return BuildResponse.success(data, "Employee fetched by first and last name", request.getRequestURI());
    }
    // Get Manager details from employees under him/her
    @GetMapping("employees/manager/{emp_id}")
    public ResponseEntity<ApiResponseDto> getManagerByEmployee(@PathVariable BigDecimal emp_id, HttpServletRequest request) {
        // Step 1: Get employee by ID
        Employee employee = employeeRepository.findById(emp_id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + emp_id));

        // Step 2: Get the employee’s manager
        Employee manager = employee.getManager();
        if (manager == null) {
            return BuildResponse.success(null, "No manager assigned for this employee", request.getRequestURI());
        }

        // Step 3: Convert manager entity to DTO and return
        EmployeeDTO dto = employeeMapper.toDTO(manager);
        return BuildResponse.success(dto, "Manager details fetched successfully", request.getRequestURI());
    }

    // Get Employees whose salary is greater than <Amount>
    @GetMapping("employees/salary_greater_than/{amount}")
    public ResponseEntity<ApiResponseDto> getEmployeesWithHighSalary(@PathVariable BigDecimal amount, HttpServletRequest request) {
        List<EmployeeDTO> data = employeeRepository.findBySalaryGreaterThan(amount , Pageable.unpaged())
                .stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());

        return BuildResponse.success(data, "Employees with salary greater than " + amount, request.getRequestURI());
    }

    // Get Employees whose salary is less than <Amount>
    @GetMapping("employees/salary_less_than/{amount}")
    public ResponseEntity<ApiResponseDto> getEmployeesWithLowSalary(@PathVariable BigDecimal amount, HttpServletRequest request) {
        List<EmployeeDTO> data = employeeRepository.findBySalaryLessThan(amount)
                .stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());

        return BuildResponse.success(data, "Employees with salary less than " + amount, request.getRequestURI());
    }

    // POST: Create new employee
    @PostMapping("employees")
    public ResponseEntity<ApiResponseDto> createEmployee(@RequestBody EmployeeDTO dto, HttpServletRequest request) {
        Job job = jobRepository.findById(dto.getJob_Id())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + dto.getJob_Id()));

        Department department = departmentRepository.findById(dto.getDepartment_Id())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + dto.getDepartment_Id()));

        Employee manager = dto.getManager_Id() != null
                ? employeeRepository.findById(dto.getManager_Id())
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + dto.getManager_Id()))
                : null;

        Employee employee = employeeMapper.toEntity(dto, job, department, manager);
        employeeRepository.save(employee);

        return BuildResponse.success(null, "Employee created successfully", request.getRequestURI());
    }

    // PUT: Update employee
    @PutMapping("employees/{id}")
    public ResponseEntity<ApiResponseDto> updateEmployee(
            @PathVariable BigDecimal id,
            @RequestBody EmployeeDTO dto,
            HttpServletRequest request) {

        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        Job job = jobRepository.findById(dto.getJob_Id())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + dto.getJob_Id()));

        Department department = departmentRepository.findById(dto.getDepartment_Id())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + dto.getDepartment_Id()));

        Employee manager = dto.getManager_Id() != null
                ? employeeRepository.findById(dto.getManager_Id())
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + dto.getManager_Id()))
                : null;

        // Update fields
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setPhoneNumber(dto.getPhoneNumber());
        existing.setHireDate(dto.getHireDate());
        existing.setJob(job);
        existing.setDepartment(department);
        existing.setManager(manager);
        existing.setSalary(dto.getSalary());
        existing.setCommissionPct(dto.getCommissionPct());

        employeeRepository.save(existing);

        return BuildResponse.success(null, "Employee updated successfully", request.getRequestURI());
    }

    // Group By
    @GetMapping("/group-by-manager")
    public ResponseEntity<ApiResponseDto> getEmployeesGroupedByManager(HttpServletRequest request) {
        List<ManagerGroupView> groupedData = employeeRepository.findEmployeesGroupedByManager();

        return BuildResponse.success(groupedData, "Employees grouped", request.getRequestURI());
    }

}

