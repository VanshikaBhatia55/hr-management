package com.hr_management.hr_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hr_management.hr_management.exception.ResourceNotFoundException;
import com.hr_management.hr_management.mapper.DepartmentMapper;
import com.hr_management.hr_management.model.dto.DepartmentDTO;
import com.hr_management.hr_management.model.dto.DepartmentResponseDTO;
import com.hr_management.hr_management.model.entity.Department;
import com.hr_management.hr_management.model.entity.Employee;
import com.hr_management.hr_management.model.entity.Location;
import com.hr_management.hr_management.repository.DepartmentRepository;
import com.hr_management.hr_management.repository.EmployeeRepository;
import com.hr_management.hr_management.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartmentController.class)
@SuppressWarnings("deprecation")
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //added mock implementations to the Spring application context
    @MockitoBean
    private DepartmentRepository departmentRepository;
    @MockitoBean
    private LocationRepository locationRepository;
    @MockitoBean
    private EmployeeRepository employeeRepository;
    @MockitoBean
    private DepartmentMapper departmentMapper;

    private Department department;
    private DepartmentDTO departmentDTO;
    private DepartmentResponseDTO departmentResponseDTO; // Changed from DepartmentInputDTO
    private Location location;
    private Employee manager;

//    @BeforeEach
//    void setUp() {
//        department = new Department();
//        department.setDepartmentId(BigDecimal.valueOf(10));
//        department.setDepartmentName("Administration");
//
//        departmentDTO = new DepartmentDTO(BigDecimal.valueOf(10), "Administration", "Jennifer Whalen", "Seattle");
//    }

    @BeforeEach
    void setUp() {
        location = new Location();
        location.setLocationId(BigDecimal.valueOf(1700));
        location.setCity("Seattle");

        manager = new Employee();
        manager.setEmployeeId(BigDecimal.valueOf(200));
        manager.setFirstName("Jennifer");
        manager.setLastName("Whalen");

        department = new Department();
        department.setDepartmentId(BigDecimal.valueOf(10));
        department.setDepartmentName("Administration");
        department.setLocation(location);
        department.setManager(manager);

        departmentDTO = new DepartmentDTO(BigDecimal.valueOf(10), "Administration", "Jennifer Whalen", "Seattle");

        departmentResponseDTO = new DepartmentResponseDTO();
        departmentResponseDTO.setDepartmentId(BigDecimal.valueOf(10));
        departmentResponseDTO.setDepartmentName("Administration");
        departmentResponseDTO.setLocationId(BigDecimal.valueOf(1700));
        departmentResponseDTO.setManagerId(BigDecimal.valueOf(200));
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

    @Test
    void getDepartmentsByManager_whenManagerDoesNotExist_shouldReturnNotFound() throws Exception {
        BigDecimal managerId = BigDecimal.valueOf(240);
        given(employeeRepository.existsById(managerId)).willReturn(false);

        mockMvc.perform(get("/api/department/by_manager/{id}", managerId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Manager not found with ID: " + managerId)));
    }

    @Test
    void getDepartmentsByManager_whenManagerExistsButNoDepartments_shouldReturnNotFound() throws Exception {
        BigDecimal managerId = BigDecimal.valueOf(115);
        given(employeeRepository.existsById(managerId)).willReturn(true);
        given(departmentRepository.findDepartmentsByManager_EmployeeId(managerId)).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/department/by_manager/{id}", managerId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("No departments found for manager ID: " + managerId)));
    }

    @Test
    void getDepartmentsByManager_whenSuccess_shouldReturnDepartments() throws Exception {
        BigDecimal managerId = BigDecimal.valueOf(200);
        given(employeeRepository.existsById(managerId)).willReturn(true);
        given(departmentRepository.findDepartmentsByManager_EmployeeId(managerId)).willReturn(List.of(department));
        given(departmentMapper.toDTO(any(Department.class))).willReturn(departmentDTO);

        mockMvc.perform(get("/api/department/by_manager/{id}", managerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].managerName", is("Jennifer Whalen")));
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    void createDepartment_whenSuccess_shouldReturnCreatedDepartment() throws Exception {
        given(locationRepository.findById(departmentResponseDTO.getLocationId())).willReturn(Optional.of(location));
        given(employeeRepository.findById(departmentResponseDTO.getManagerId())).willReturn(Optional.of(manager));
        given(departmentRepository.save(any(Department.class))).willReturn(department);
        given(departmentMapper.toDTO(department)).willReturn(departmentDTO);

        mockMvc.perform(post("/api/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentResponseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data.departmentName", is("Administration")))
                .andExpect(jsonPath("$.message", is("Department successfully created")));
    }


    @Test
    void createDepartment_whenLocationNotFound_shouldReturnNotFound() throws Exception {
        BigDecimal invalidLocationId = BigDecimal.valueOf(9999);
        departmentResponseDTO.setLocationId(invalidLocationId);
        given(locationRepository.findById(invalidLocationId)).willReturn(Optional.empty());

        mockMvc.perform(post("/api/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentResponseDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Location not found with ID: " + invalidLocationId)));
    }

    @Test
    void createDepartment_whenManagerNotFound_shouldReturnNotFound() throws Exception {
        BigDecimal invalidManagerId = BigDecimal.valueOf(9999);
        departmentResponseDTO.setManagerId(invalidManagerId);

        given(locationRepository.findById(departmentResponseDTO.getLocationId())).willReturn(Optional.of(location));
        given(employeeRepository.findById(invalidManagerId)).willReturn(Optional.empty());

        mockMvc.perform(post("/api/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentResponseDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Manager not found with ID: " + invalidManagerId)));
    }

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
@Test
void updateDepartment_whenSuccess_shouldReturnUpdatedDepartment() throws Exception {
    BigDecimal departmentId = BigDecimal.valueOf(10);
    given(departmentRepository.findById(departmentId)).willReturn(Optional.of(department));
    given(locationRepository.findById(departmentResponseDTO.getLocationId())).willReturn(Optional.of(location));
    given(employeeRepository.findById(departmentResponseDTO.getManagerId())).willReturn(Optional.of(manager));
    given(departmentRepository.save(any(Department.class))).willReturn(department);
    given(departmentMapper.toDTO(department)).willReturn(departmentDTO);

    mockMvc.perform(put("/api/department/{department_id}", departmentId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(departmentResponseDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.data.departmentName", is("Administration")))
            .andExpect(jsonPath("$.message", is("Department successfully updated")));
}

    @Test
    void updateDepartment_whenDepartmentNotFound_shouldReturnNotFound() throws Exception {
        BigDecimal invalidDepartmentId = BigDecimal.valueOf(99);
        given(departmentRepository.findById(invalidDepartmentId)).willReturn(Optional.empty());

        mockMvc.perform(put("/api/department/{department_id}", invalidDepartmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentResponseDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Department not found with ID: " + invalidDepartmentId)));
    }

    @Test
    void updateDepartment_whenNewLocationNotFound_shouldReturnNotFound() throws Exception {
        BigDecimal departmentId = BigDecimal.valueOf(10);
        BigDecimal invalidLocationId = BigDecimal.valueOf(9999);
        departmentResponseDTO.setLocationId(invalidLocationId);

        given(departmentRepository.findById(departmentId)).willReturn(Optional.of(department));
        given(locationRepository.findById(invalidLocationId)).willReturn(Optional.empty());

        mockMvc.perform(put("/api/department/{department_id}", departmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentResponseDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Location not found with ID: " + invalidLocationId)));
    }
}