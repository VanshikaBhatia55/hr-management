package com.hr_management.hr_management.controller;

/*import com.hr_management.hr_management.model.dto.DepartmentHeadcountDTO;
import com.hr_management.hr_management.model.dto.JobDistributionDTO;*/
import com.hr_management.hr_management.model.entity.Department;
import com.hr_management.hr_management.model.entity.Employee;
import com.hr_management.hr_management.model.entity.Job;
import com.hr_management.hr_management.repository.DepartmentRepository;
import com.hr_management.hr_management.repository.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/report")
public class ReportController {


    private final EmployeeRepository employeeRepository;

    private final DepartmentRepository departmentRepository;


    //  Constructor injection of DepartmentRepository
    public ReportController(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

      //return List of DepartmentHeadcount
   /* @GetMapping("/departments/headcount")
    public ResponseEntity<List<DepartmentHeadcountDTO>> getDepartmentsHeadCount() {

        // Fetch all departments from the database
        List<Department> departments = departmentRepository.findAll();

        // Map each department to DepartmentHeadcountDTO
        List<DepartmentHeadcountDTO> departmentHeadcountDTOList = departments.stream()
                .map(department -> new DepartmentHeadcountDTO(
                        department.getDepartmentId().longValue(), // Department ID as Long
                        department.getDepartmentName(),           // Department name
                        department.getEmployees() == null ? 0L : department.getEmployees().size() // Employee count
                ))
                .toList();


        return ResponseEntity.ok(departmentHeadcountDTOList);
    }


    @GetMapping("/jobs/distribution")
    public ResponseEntity<List<JobDistributionDTO>> getJobDistribution() {

        // Fetch all employees from the database
        List<Employee> employees = employeeRepository.findAll();

        // Group employees by job title and count
        Map<Job, Long> jobCountMap = employees.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Employee::getJob,
                        java.util.stream.Collectors.counting()
                ));

        // Map to DTO
        List<JobDistributionDTO> jobDistribution = jobCountMap.entrySet().stream()
                .map(entry -> new JobDistributionDTO(
                        entry.getKey().getJobId(),
                        entry.getKey().getJobTitle(),
                        entry.getValue()
                ))
                .toList();

        return ResponseEntity.ok(jobDistribution);
    }*/

}
