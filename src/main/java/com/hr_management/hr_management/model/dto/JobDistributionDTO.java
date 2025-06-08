package com.hr_management.hr_management.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class JobDistributionDTO {
    private String jobId;
    private String jobTitle;
    private Long employeeCount;
}
