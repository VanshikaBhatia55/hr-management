package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.mapper.EmployeeMapper;
import com.hr_management.hr_management.model.dto.ApiResponseDto;
import com.hr_management.hr_management.model.dto.EmployeeDTO;
import com.hr_management.hr_management.model.entity.Employee;
import com.hr_management.hr_management.repository.EmployeeRepository;
import com.hr_management.hr_management.utils.BuildResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<ApiResponseDto> getAllEmployees(HttpServletRequest request) {
        List<EmployeeDTO> data = employeeRepository.findAll().stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
        return BuildResponse.success(data, "List of all Employee ", request.getRequestURI());
    }


}
