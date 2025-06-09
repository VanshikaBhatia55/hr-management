package com.hr_management.hr_management.mapper;

import com.hr_management.hr_management.model.dto.DepartmentDTO;
import com.hr_management.hr_management.model.dto.EmployeeDTO;
import com.hr_management.hr_management.model.dto.EmployeeDetailDTO;
import com.hr_management.hr_management.model.dto.JobDTO;
import com.hr_management.hr_management.model.entity.Department;
import com.hr_management.hr_management.model.entity.Employee;
import com.hr_management.hr_management.model.entity.Job;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EmployeeMapper {

    public EmployeeDTO toDTO(Employee employee) {
        if (employee == null) return null;

        String jobId = employee.getJob() != null ? employee.getJob().getJobId() : null;
        BigDecimal departmentId = employee.getDepartment() != null ? employee.getDepartment().getDepartmentId() : null;
        BigDecimal managerId = employee.getManager() != null ? employee.getManager().getEmployeeId() : null;

        return new EmployeeDTO(
                employee.getEmployeeId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhoneNumber(),
                employee.getHireDate(),
                employee.getSalary(),
                employee.getCommissionPct(),
                jobId,
                departmentId,
                managerId
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

    public EmployeeDetailDTO toDetailDTO(Employee employee) {
        if (employee == null) return null;

        JobDTO jobDTO = null;
        if (employee.getJob() != null) {
            jobDTO = new JobDTO(
                    employee.getJob().getJobId(),
                    employee.getJob().getJobTitle(),
                    employee.getJob().getMinSalary(),
                    employee.getJob().getMaxSalary()
            );
        }

        DepartmentDTO departmentDTO = null;
        if (employee.getDepartment() != null) {
            String managerName = null;
            if (employee.getDepartment().getManager() != null) {
                managerName = employee.getDepartment().getManager().getFirstName() + " " +
                        employee.getDepartment().getManager().getLastName();
            }

            String city = null;
            if (employee.getDepartment().getLocation() != null) {
                city = employee.getDepartment().getLocation().getCity();
            }

            departmentDTO = new DepartmentDTO(
                    employee.getDepartment().getDepartmentId(),
                    employee.getDepartment().getDepartmentName(),
                    managerName,
                    city
            );
        }

        EmployeeDTO managerDTO = null;
        if (employee.getManager() != null) {
            Employee manager = employee.getManager();
            managerDTO = new EmployeeDTO(
                    manager.getEmployeeId(),
                    manager.getFirstName(),
                    manager.getLastName(),
                    manager.getEmail(),
                    manager.getPhoneNumber(),
                    manager.getHireDate(),
                    manager.getSalary(),
                    manager.getCommissionPct(),
                    manager.getJob() != null ? manager.getJob().getJobId() : null,
                    manager.getDepartment() != null ? manager.getDepartment().getDepartmentId() : null,
                    manager.getManager()!=null ? manager.getManager().getEmployeeId() : null
            );
        }

        return new EmployeeDetailDTO(
                employee.getEmployeeId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhoneNumber(),
                employee.getHireDate(),
                jobDTO,
                departmentDTO,
                managerDTO
        );
    }

}