package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.model.dto.ApiResponseDto;
import com.hr_management.hr_management.model.dto.DepartmentHeadcountDTO;
import com.hr_management.hr_management.model.dto.JobDistributionDTO;
import com.hr_management.hr_management.model.entity.Department;
import com.hr_management.hr_management.model.entity.Employee;
import com.hr_management.hr_management.model.entity.Job;
import com.hr_management.hr_management.repository.DepartmentRepository;
import com.hr_management.hr_management.repository.EmployeeRepository;
import com.hr_management.hr_management.utils.BuildResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    // Constructor injection
    public ReportController(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    // Return List of DepartmentHeadcount
    @GetMapping("/departments/headcount")
    public ResponseEntity<ApiResponseDto> getDepartmentsHeadCount(HttpServletRequest request) {

        // Fetch all departments
        List<Department> departments = departmentRepository.findAll();

        // Map to DepartmentHeadcountDTO
        List<DepartmentHeadcountDTO> departmentHeadcountDTOList = departments.stream()
                .map(department -> new DepartmentHeadcountDTO(
                        department.getDepartmentId().longValue(),
                        department.getDepartmentName(),
                        department.getEmployees() == null ? 0L : department.getEmployees().size()
                ))
                .toList();

        return BuildResponse.success(departmentHeadcountDTOList, "Department headcount report", request.getRequestURI());
    }

    // Return Job distribution
    @GetMapping("/jobs/distribution")
    public ResponseEntity<ApiResponseDto> getJobDistribution(HttpServletRequest request) {

        // Fetch all employees
        List<Employee> employees = employeeRepository.findAll();

        // Group by job and count
        Map<Job, Long> jobCountMap = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getJob,
                        Collectors.counting()
                ));

        // Map to JobDistributionDTO
        List<JobDistributionDTO> jobDistribution = jobCountMap.entrySet().stream()
                .map(entry -> new JobDistributionDTO(
                        entry.getKey().getJobId(),
                        entry.getKey().getJobTitle(),
                        entry.getValue()
                ))
                .toList();

        return BuildResponse.success(jobDistribution, "Job distribution report", request.getRequestURI());
    }
}
