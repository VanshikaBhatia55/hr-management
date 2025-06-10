package com.hr_management.hr_management.model.dto.department;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DepartmentHeadcountDTO {
    private Long departmentId;
    private String departmentName;
    private Long employeeCount;
}
