package com.hr_management.hr_management.model.dto;

import com.hr_management.hr_management.model.entity.Country;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionDTO {
    private BigDecimal regionId;
    private String regionName;
    private List<Country> countries;
}
