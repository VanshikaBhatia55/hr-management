package com.hr_management.hr_management.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {
    private BigDecimal departmentId;
    private String departmentName;
    private String managerName;
    private String locationCity;
}
