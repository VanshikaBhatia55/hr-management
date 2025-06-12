package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.exception.ResourceNotFoundException;
import com.hr_management.hr_management.mapper.DepartmentMapper;
import com.hr_management.hr_management.mapper.EmployeeMapper;
import com.hr_management.hr_management.model.dto.ApiResponseDto;

import com.hr_management.hr_management.model.dto.EmployeeByDepartmentDTO;
import com.hr_management.hr_management.model.dto.EmployeeDTO;

import com.hr_management.hr_management.model.dto.department.DepartmentDTO;
import com.hr_management.hr_management.model.dto.department.DepartmentResponseDTO;
import com.hr_management.hr_management.model.entity.Department;
import com.hr_management.hr_management.model.entity.Employee;
import com.hr_management.hr_management.model.projection.DepartmentCountProjection;
import com.hr_management.hr_management.repository.*;
import com.hr_management.hr_management.utils.BuildResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

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
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    private  EmployeeMapper employeeMapper;

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final LocationRepository locationRepository;
    private final EmployeeRepository employeeRepository;


    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private RegionRepository regionRepository;


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
        return BuildResponse.success(departmentMapper.toFrontendDTO(department), "Department details retrieved", request.getRequestURI());
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

    @GetMapping("/by_manager/{manager_id}")
    public ResponseEntity<ApiResponseDto> getDepartmentsByManager(@PathVariable("manager_id") BigDecimal managerId, HttpServletRequest request) {
        if (!employeeRepository.existsById(managerId)) {
            throw new ResourceNotFoundException("Manager not found with ID: " + managerId);
        }

        List<DepartmentDTO> departments = departmentRepository.findDepartmentsByManager_EmployeeId(managerId).stream()
                .map(departmentMapper::toDTO)
                .collect(Collectors.toList());

        if (departments.isEmpty()) {
            throw new ResourceNotFoundException("No departments found for manager ID: " + managerId);
        }

        return BuildResponse.success(departments, "List of departments for manager ID: " + managerId, request.getRequestURI());
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto> createDepartment(@Valid @RequestBody DepartmentResponseDTO departmentResponseDTO, HttpServletRequest request) {
        var location = locationRepository.findById(departmentResponseDTO.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with ID: " + departmentResponseDTO.getLocationId()));

        var manager = departmentResponseDTO.getManagerId() != null ?
                employeeRepository.findById(departmentResponseDTO.getManagerId())
                        .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + departmentResponseDTO.getManagerId()))
                : null;

        Department department = new Department();
        department.setDepartmentId(departmentResponseDTO.getDepartmentId());
        department.setDepartmentName(departmentResponseDTO.getDepartmentName());
        department.setLocation(location);
        department.setManager(manager);

        Department savedDepartment = departmentRepository.save(department);

        return BuildResponse.success(departmentMapper.toDTO(savedDepartment), "Department successfully created", request.getRequestURI());
    }

    @PutMapping("/{department_id}")
    public ResponseEntity<ApiResponseDto> updateDepartment(@PathVariable("department_id") BigDecimal departmentId, @Valid @RequestBody DepartmentResponseDTO departmentResponseDTO, HttpServletRequest request) {
        // First, find the existing department
        Department existingDepartment = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + departmentId));

        var location = locationRepository.findById(departmentResponseDTO.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with ID: " + departmentResponseDTO.getLocationId()));

        var manager = departmentResponseDTO.getManagerId() != null ?
                employeeRepository.findById(departmentResponseDTO.getManagerId())
                        .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + departmentResponseDTO.getManagerId()))
                : null;

        existingDepartment.setDepartmentName(departmentResponseDTO.getDepartmentName());
        existingDepartment.setLocation(location);
        existingDepartment.setManager(manager);

        Department updatedDepartment = departmentRepository.save(existingDepartment);

        return BuildResponse.success(departmentMapper.toDTO(updatedDepartment), "Department successfully updated", request.getRequestURI());
    }

    @GetMapping("/count_by_location")
    public ResponseEntity<ApiResponseDto> getDepartmentCountByLocation(HttpServletRequest request) {
        List<DepartmentCountProjection> results = departmentRepository.countDepartmentsByLocation();

        Map<String, Long> departmentCounts = results.stream()
                .collect(Collectors.toMap(
                        DepartmentCountProjection::getLocationCity,
                        DepartmentCountProjection::getDepartmentCount
                ));

        return BuildResponse.success(departmentCounts, "Department count per location", request.getRequestURI());
    }

    @GetMapping("/unmanaged")
    public ResponseEntity<ApiResponseDto> getUnmanagedDepartments(HttpServletRequest request) {
        List<DepartmentDTO> departments = departmentRepository.findByManagerIsNull().stream()
                .map(departmentMapper::toDTO)
                .collect(Collectors.toList());

        String message = departments.isEmpty()
                ? "All departments have a manager assigned."
                : "List of departments without a manager.";

        return BuildResponse.success(departments, message, request.getRequestURI());
    }
    @GetMapping("/{departmentName}/employees")
    public ResponseEntity<ApiResponseDto> getEmployeesByDepartmentName(
            @PathVariable String departmentName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "employeeId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            HttpServletRequest request) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Employee> employeePage = employeeRepository.findByDepartmentDepartmentNameIgnoreCase(departmentName, pageable);

        Page<EmployeeDTO> dtoPage = employeePage.map(employeeMapper::toDTO);

        return BuildResponse.success(
                dtoPage,
                "Successfully retrieved employees for department: " + departmentName,
                request.getRequestURI()
        );
    }


   // for frontend

    @GetMapping("/departments")
    public ResponseEntity<?> getDepartments(
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        List<Department> departments;

        if (location != null && !location.isEmpty()) {
            departments = departmentRepository.findByLocation_City(location, pageable);
            if (departments.isEmpty()) {
                return ResponseEntity.ok("No department is there in this location.");
            }
        } else {
            departments = departmentRepository.findAll(pageable).getContent();
        }

        return ResponseEntity.ok(departments);
    }

    @PostMapping("/departments")
    public ResponseEntity<String> addDepartment(@RequestBody DepartmentAddRequest request) {
        Region region = regionRepository.findByName(request.getRegionName())
                .orElseGet(() -> regionRepository.save(new Region(request.getRegionName())));

        Country country = countryRepository.findByName(request.getCountryName())
                .orElseGet(() -> {
                    Country c = new Country();
                    c.setCountryName(request.getCountryName());
                    c.setRegion(region);
                    return countryRepository.save(c);
                });

        Location location = locationRepository.findByCity(request.getLocationName())
                .orElseGet(() -> {
                    Location l = new Location();
                    l.setCity(request.getLocationName());
                    l.setCountry(country);
                    return locationRepository.save(l);
                });

        Department dept = new Department();
        dept.setDepartmentName(request.getDepartmentName());
        dept.setLocation(location);
        departmentRepository.save(dept);

        return ResponseEntity.ok("Department added successfully.");
    }




}
