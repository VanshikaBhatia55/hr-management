package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.mapper.DepartmentMapper;
import com.hr_management.hr_management.model.dto.DepartmentDTO;
import com.hr_management.hr_management.model.entity.Department;
import com.hr_management.hr_management.repository.DepartmentRepository;
import com.hr_management.hr_management.repository.EmployeeRepository;
import com.hr_management.hr_management.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DepartmentControllerTest {

    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private DepartmentMapper departmentMapper;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private DepartmentController departmentController;

    private MockMvc mockMvc;
    private Department department;
    private DepartmentDTO departmentDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();
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

}
