package com.hr_management.hr_management.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDTO {
    private String countryId;
    private String countryName;
    private Long regionId; // assuming Region has Long id
}
