package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.mapper.EmployeeMapper;
import com.hr_management.hr_management.model.entity.Employee;
import com.hr_management.hr_management.repository.EmployeeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    EmployeeRepository employeeRepository;
    EmployeeMapper employeeMapper;

    public EmployeeController(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @GetMapping("employees")
    public List<Employee> getAllEmployees() {
        /*return employeeRepository.findAll().stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());*/
        return employeeRepository.findAll().stream().toList();
    }

}
