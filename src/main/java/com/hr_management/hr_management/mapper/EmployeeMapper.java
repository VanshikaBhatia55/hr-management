package com.hr_management.hr_management.mapper;

import com.hr_management.hr_management.model.dto.EmployeeDTO;
import com.hr_management.hr_management.model.entity.Department;
import com.hr_management.hr_management.model.entity.Employee;
import com.hr_management.hr_management.model.entity.Job;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public  EmployeeDTO toDTO(Employee employee) {
        if (employee == null) return null;

        String jobTitle = employee.getJob() != null ? employee.getJob().getJobTitle() : null;
        String departmentName = employee.getDepartment() != null ? employee.getDepartment().getDepartmentName() : null;
        String managerName = employee.getManager() != null
                ? employee.getManager().getFirstName() + " " + employee.getManager().getLastName()
                : null;

        return new EmployeeDTO(
                employee.getEmployeeId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhoneNumber(),
                employee.getHireDate(),
                jobTitle,
                departmentName,
                managerName
        );
    }

    public  Employee toEntity(EmployeeDTO dto, Job job, Department department, Employee manager) {
        if (dto == null) return null;

        Employee employee = new Employee();
        employee.setEmployeeId(dto.getEmployeeId());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setHireDate(dto.getHireDate());
        employee.setJob(job);
        employee.setDepartment(department);
        employee.setManager(manager);

        return employee;
    }
}