package com.hr_management.hr_management.mapper;


import com.hr_management.hr_management.model.dto.report.EmployeeFullDetailsDTO;
import com.hr_management.hr_management.model.dto.report.EmployeeRegionDTO;
import com.hr_management.hr_management.model.entity.*;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {

    public EmployeeFullDetailsDTO toEmployeeFullDetailsDTO(Employee emp) {
        Department dept = emp.getDepartment();
        Location loc = (dept != null) ? dept.getLocation() : null;
        Country country = (loc != null) ? loc.getCountry() : null;
        Region region = (country != null) ? country.getRegion() : null;

        return EmployeeFullDetailsDTO.builder()
                .firstName(emp.getFirstName())
                .lastName(emp.getLastName())
                .email(emp.getEmail())
                .phoneNumber(emp.getPhoneNumber())
                .hireDate(emp.getHireDate())
                .salary(emp.getSalary() != null ? emp.getSalary().doubleValue() : null)
                .jobTitle(emp.getJob() != null ? emp.getJob().getJobTitle() : null)
                .departmentName(dept != null ? dept.getDepartmentName() : null)
                .streetAddress(loc != null ? loc.getStreetAddress() : null)
                .postalCode(loc != null ? loc.getPostalCode() : null)
                .city(loc != null ? loc.getCity() : null)
                .stateProvince(loc != null ? loc.getStateProvince() : null)
                .countryName(country != null ? country.getCountryName() : null)
                .regionName(region != null ? region.getRegionName() : null)
                .build();
    }


    public EmployeeRegionDTO toEmployeeRegionDTO(Employee emp) {
        String fullName = (emp.getFirstName() != null ? emp.getFirstName() : "") +
                " " +
                (emp.getLastName() != null ? emp.getLastName() : "");

        String departmentName = emp.getDepartment() != null ? emp.getDepartment().getDepartmentName() : null;

        String jobTitle = emp.getJob() != null ? emp.getJob().getJobTitle() : null;

        String city = null;
        if (emp.getDepartment() != null && emp.getDepartment().getLocation() != null) {
            city = emp.getDepartment().getLocation().getCity();
        }

        return EmployeeRegionDTO.builder()
                .id(emp.getEmployeeId())
                .fullName(fullName.trim())
                .department(departmentName)
                .jobTitle(jobTitle)
                .city(city)
                .build();
    }
}

