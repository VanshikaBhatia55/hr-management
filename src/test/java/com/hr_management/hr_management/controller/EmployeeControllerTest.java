package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.model.dto.ApiResponseDto;
import com.hr_management.hr_management.model.dto.EmployeeDTO;
import com.hr_management.hr_management.model.dto.EmployeeDetailDTO;
import com.hr_management.hr_management.model.entity.Employee;
import com.hr_management.hr_management.repository.DepartmentRepository;
import com.hr_management.hr_management.repository.EmployeeRepository;
import com.hr_management.hr_management.mapper.EmployeeMapper;
import com.hr_management.hr_management.repository.JobRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> mockEmployees = List.of(new Employee());
        when(employeeRepository.findAll()).thenReturn(mockEmployees);
        when(employeeMapper.toDTO(any(Employee.class))).thenReturn(new EmployeeDTO());

        ResponseEntity<ApiResponseDto> response = employeeController.getAllEmployees(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("List of all Employee ", response.getBody().getMessage());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testGetEmployeesByHireDate() {
        LocalDate hireDate = LocalDate.of(2024, 1, 1);
        List<Employee> mockList = List.of(new Employee());
        when(employeeRepository.findAllByHireDate(hireDate)).thenReturn(mockList);
        when(employeeMapper.toDTO(any(Employee.class))).thenReturn(new EmployeeDTO());

        ResponseEntity<ApiResponseDto> response = employeeController.getEmployeesByHireDate(hireDate, request);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().getMessage().contains("Employees fetched"));
        verify(employeeRepository).findAllByHireDate(hireDate);
    }

    @Test
    void testGetEmployeeById() {
        BigDecimal id = new BigDecimal("101");
        Employee employee = new Employee();
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        when(employeeMapper.toDetailDTO(employee)).thenReturn(new EmployeeDetailDTO());

        ResponseEntity<ApiResponseDto> response = employeeController.getEmployeeById(id, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Employee fetched successfully", response.getBody().getMessage());
        verify(employeeRepository).findById(id);
    }

    @Test
    public void testGetEmployeeByEmail() {
        String email = "DWILLIAMS";
        Employee mockEmployee = new Employee();
        EmployeeDetailDTO mockDTO = new EmployeeDetailDTO();

        when(employeeRepository.findByEmail(email)).thenReturn(Optional.of(mockEmployee));
        when(employeeMapper.toDetailDTO(mockEmployee)).thenReturn(mockDTO);
        when(request.getRequestURI()).thenReturn("/api/employees/by_email/" + email);

        ApiResponseDto response = employeeController
                .getEmployeeByEmail(email, request)
                .getBody();

        assertNotNull(response);
        assertEquals("Employee fetched by email", response.getMessage());
        assertEquals(mockDTO, response.getData());
    }

    @Test
    public void testGetEmployeeByDepartmentId() {
        BigDecimal deptId = new BigDecimal("10");
        List<Employee> mockEmployees = List.of(new Employee(), new Employee());
        List<EmployeeDTO> mockDTOs = List.of(new EmployeeDTO(), new EmployeeDTO());

        when(employeeRepository.findByDepartment_DepartmentId(deptId)).thenReturn(mockEmployees);
        when(employeeMapper.toDTO(any(Employee.class))).thenReturn(mockDTOs.get(0), mockDTOs.get(1));
        when(request.getRequestURI()).thenReturn("/api/employees/by_department/" + deptId);

        ApiResponseDto response = employeeController
                .getEmployeeByDepartmentId(deptId, request)
                .getBody();

        assertNotNull(response);
        assertEquals("Employees fetched by Department ID", response.getMessage());
        assertEquals(mockDTOs.size(), ((List<?>) response.getData()).size());
        verify(employeeRepository).findByDepartment_DepartmentId(deptId);
    }

    @Test
    public void testGetEmployeeByManagerId() {
        BigDecimal managerId = new BigDecimal("101");
        List<Employee> mockEmployees = List.of(new Employee());
        List<EmployeeDTO> mockDTOs = List.of(new EmployeeDTO());

        when(employeeRepository.findByManagerEmployeeId(managerId)).thenReturn(mockEmployees);
        when(employeeMapper.toDTO(any(Employee.class))).thenReturn(mockDTOs.get(0));
        when(request.getRequestURI()).thenReturn("/api/employees/by_manager/" + managerId);

        ApiResponseDto response = employeeController
                .getEmployeeByManagerId(managerId, request)
                .getBody();

        assertNotNull(response);
        assertEquals("Employees fetched by Manager ID", response.getMessage());
        assertEquals(mockDTOs.size(), ((List<?>) response.getData()).size());
        verify(employeeRepository).findByManagerEmployeeId(managerId);
    }
}
