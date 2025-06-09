package com.hr_management.hr_management.model.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDistributionDTO {
    private String city;
    private String state;
    private long employeeCount;
}
