package com.hr_management.hr_management.model.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmployeeFullDetailsDTO {
    Long id;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    LocalDate hireDate;
    Double salary;
    String jobTitle;
    String departmentName;
    String streetAddress;
    String postalCode;
    String city;
    String stateProvince;
    String countryName;
    String regionName;
}
