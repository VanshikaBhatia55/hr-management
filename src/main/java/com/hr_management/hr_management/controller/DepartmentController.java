package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.exception.ResourceNotFoundException;
import com.hr_management.hr_management.mapper.DepartmentMapper;
import com.hr_management.hr_management.model.dto.ApiResponseDto;
import com.hr_management.hr_management.model.dto.DepartmentDTO;
import com.hr_management.hr_management.model.entity.Department;
import com.hr_management.hr_management.repository.DepartmentRepository;
import com.hr_management.hr_management.repository.EmployeeRepository;
import com.hr_management.hr_management.repository.LocationRepository;
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
    private final LocationRepository locationRepository;
    private final EmployeeRepository employeeRepository;

    public DepartmentController(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper, LocationRepository locationRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
        this.locationRepository = locationRepository;
        this.employeeRepository = employeeRepository;
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

    @GetMapping("/by_location/{location_id}")
    public ResponseEntity<ApiResponseDto> getDepartmentsByLocation(@PathVariable("location_id") BigDecimal locationId, HttpServletRequest request) {
        if (!locationRepository.existsById(locationId)) {
            throw new ResourceNotFoundException("Location not found with ID: " + locationId);
        }

        List<DepartmentDTO> departments = departmentRepository.findByLocationLocationId(locationId).stream()
            .map(departmentMapper::toDTO)
            .collect(Collectors.toList());

        if (departments.isEmpty()) {
            throw new ResourceNotFoundException("No departments found for location ID: " + locationId);
        }

        return BuildResponse.success(departments, "List of departments for location ID: " + locationId, request.getRequestURI());
    }

}
