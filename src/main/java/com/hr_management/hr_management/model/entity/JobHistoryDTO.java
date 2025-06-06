package com.hr_management.hr_management.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobHistoryDTO {
    private BigDecimal employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String jobTitle;
    private String departmentName;
}
