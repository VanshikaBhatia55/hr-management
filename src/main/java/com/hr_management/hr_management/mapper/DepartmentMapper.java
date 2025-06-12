package com.hr_management.hr_management.mapper;

import com.hr_management.hr_management.model.dto.department.DepartmentDTO;
import com.hr_management.hr_management.model.dto.department.DepartmentForFrontendDTO;
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

    public DepartmentForFrontendDTO toFrontendDTO(Department department) {
        LocationMapper locationMapper = new LocationMapper();
        DepartmentForFrontendDTO  dto = new DepartmentForFrontendDTO();
        dto.setDepartmentId(department.getDepartmentId());
        dto.setDepartmentName(department.getDepartmentName());
        dto.setLocation(locationMapper.toLocationDto(department.getLocation()));
        dto.setCountryName(department.getLocation().getCountry().getCountryName());
        dto.setRegionName(department.getLocation().getCountry().getRegion().getRegionName());
        return dto;
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
