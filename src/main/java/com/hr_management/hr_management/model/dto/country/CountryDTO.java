package com.hr_management.hr_management.model.dto.country;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hr_management.hr_management.model.entity.Region;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDTO {
    private String countryId;
    private String countryName;
    private Region region;
}
