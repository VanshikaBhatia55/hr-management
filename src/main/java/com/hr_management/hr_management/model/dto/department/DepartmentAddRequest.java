package com.hr_management.hr_management.model.dto.department;

import lombok.Data;

@Data
public class DepartmentAddRequest {
    private String departmentName;
    private String locationName;
    private String countryName;
    private String regionName;
}
