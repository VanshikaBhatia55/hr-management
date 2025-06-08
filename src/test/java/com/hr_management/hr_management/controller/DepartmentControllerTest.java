package com.hr_management.hr_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hr_management.hr_management.exception.ResourceNotFoundException;
import com.hr_management.hr_management.mapper.DepartmentMapper;
import com.hr_management.hr_management.model.dto.DepartmentDTO;
import com.hr_management.hr_management.model.entity.Department;
import com.hr_management.hr_management.repository.DepartmentRepository;
import com.hr_management.hr_management.repository.EmployeeRepository;
import com.hr_management.hr_management.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartmentController.class)
@SuppressWarnings("deprecation")
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //added mock implementations to the Spring application context
    @MockBean
    private DepartmentRepository departmentRepository;
    @MockBean
    private LocationRepository locationRepository;
    @MockBean
    private EmployeeRepository employeeRepository;
    @MockBean
    private DepartmentMapper departmentMapper;

    private Department department;
    private DepartmentDTO departmentDTO;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setDepartmentId(BigDecimal.valueOf(10));
        department.setDepartmentName("Administration");

        departmentDTO = new DepartmentDTO(BigDecimal.valueOf(10), "Administration", "Jennifer Whalen", "Seattle");
    }

    @Test
    void getAllDepartments_shouldReturnListOfDepartments() throws Exception {
        given(departmentRepository.findAll()).willReturn(List.of(department));
        given(departmentMapper.toDTO(any(Department.class))).willReturn(departmentDTO);

        mockMvc.perform(get("/api/department"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data[0].departmentName").value("Administration"));
    }

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @Test
    void getDepartmentById_whenDepartmentExists_shouldReturnDepartment() throws Exception {
        BigDecimal departmentId = BigDecimal.valueOf(10);
        given(departmentRepository.findById(departmentId)).willReturn(Optional.of(department));
        given(departmentMapper.toDTO(department)).willReturn(departmentDTO);

        mockMvc.perform(get("/api/department/{id}", departmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data.departmentId").value(10));
    }

    @Test
    void getDepartmentById_whenDepartmentDoesNotExist_shouldReturnNotFound() throws Exception {
        BigDecimal departmentId = BigDecimal.valueOf(99);
        given(departmentRepository.findById(departmentId)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/department/{id}", departmentId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(jsonPath("$.message", is("Department not found with ID: " + departmentId)));
    }

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        @Test
    void getDepartmentsByLocation_whenSuccess_shouldReturnDepartments() throws Exception {
        BigDecimal locationId = BigDecimal.valueOf(1700);
        given(locationRepository.existsById(locationId)).willReturn(true);
        given(departmentRepository.findByLocationLocationId(locationId)).willReturn(List.of(department));
        given(departmentMapper.toDTO(any(Department.class))).willReturn(departmentDTO);

        mockMvc.perform(get("/api/department/by_location/{id}", locationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].departmentName", is("Administration")));
    }

    @Test
    void getDepartmentsByLocation_whenLocationDoesNotExist_shouldReturnNotFound() throws Exception {
        BigDecimal locationId = BigDecimal.valueOf(1234);
        given(locationRepository.existsById(locationId)).willReturn(false);

        mockMvc.perform(get("/api/department/by_location/{id}", locationId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Location not found with ID: " + locationId)));
    }

    @Test
    void getDepartmentsByLocation_whenLocationExistsButNoDepartments_shouldReturnNotFound() throws Exception {
        BigDecimal locationId = BigDecimal.valueOf(1100);
        given(locationRepository.existsById(locationId)).willReturn(true);
        given(departmentRepository.findByLocationLocationId(locationId)).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/department/by_location/{id}", locationId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("No departments found for location ID: " + locationId)));
    }

}
