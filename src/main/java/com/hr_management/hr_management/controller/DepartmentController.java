package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.exception.ResourceNotFoundException;
import com.hr_management.hr_management.mapper.DepartmentMapper;
import com.hr_management.hr_management.model.dto.ApiResponseDto;
import com.hr_management.hr_management.model.dto.DepartmentDTO;
import com.hr_management.hr_management.model.entity.Department;
import com.hr_management.hr_management.repository.DepartmentRepository;
import com.hr_management.hr_management.utils.BuildResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentController(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto> getAllDepartments(HttpServletRequest request) {
         List<DepartmentDTO> departments = departmentRepository.findAll().stream()
                .map(departmentMapper::toDTO)
                .collect(Collectors.toList());

        return BuildResponse.success(departments, "List of all Departments", request.getRequestURI());
    }

    @GetMapping("/{department_id}")
    public ResponseEntity<ApiResponseDto> getDepartmentById(@PathVariable("department_id") BigDecimal departmentId, HttpServletRequest request) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + departmentId));
        return BuildResponse.success(departmentMapper.toDTO(department), "Department details retrieved", request.getRequestURI());
    }



}
