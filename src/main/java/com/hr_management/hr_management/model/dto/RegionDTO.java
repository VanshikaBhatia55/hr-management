package com.hr_management.hr_management.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionDTO {
    private BigDecimal regionId;
    private String regionName;
}
