package com.hr_management.hr_management.mapper;

import com.hr_management.hr_management.model.dto.DepartmentDTO;
import com.hr_management.hr_management.model.entity.Department;
import com.hr_management.hr_management.model.entity.Employee;
import com.hr_management.hr_management.model.entity.Location;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

    public DepartmentDTO toDTO(Department department) {
        if (department == null) return null;

        String managerName = department.getManager() != null
                ? department.getManager().getFirstName() + " " + department.getManager().getLastName()
                : null;

        String locationCity = department.getLocation() != null
                ? department.getLocation().getCity()
                : null;

        return new DepartmentDTO(
                department.getDepartmentId(),
                department.getDepartmentName(),
                managerName,
                locationCity
        );
    }

    public Department toEntity(DepartmentDTO dto, Employee manager, Location location) {
        if (dto == null) return null;

        Department department = new Department();
        department.setDepartmentId(dto.getDepartmentId());
        department.setDepartmentName(dto.getDepartmentName());
        department.setManager(manager);
        department.setLocation(location);

        return department;
    }


}
