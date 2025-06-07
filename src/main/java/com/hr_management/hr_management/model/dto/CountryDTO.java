package com.hr_management.hr_management.model.dto;

import com.hr_management.hr_management.model.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDTO {
    private String countryId;
    private String countryName;
    private BigDecimal region_ID;
}
