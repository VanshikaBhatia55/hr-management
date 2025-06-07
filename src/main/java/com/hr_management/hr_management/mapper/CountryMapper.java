package com.hr_management.hr_management.mapper;

import com.hr_management.hr_management.model.dto.CountryDTO;
import com.hr_management.hr_management.model.entity.Country;
import org.springframework.stereotype.Component;

@Component
public class CountryMapper {
    public CountryDTO mapToCountryDTO(Country country){
        if (country == null) {
            return null;
        }
        CountryDTO dto = new CountryDTO();
        dto.setCountryId(country.getCountryId());
        dto.setCountryName(country.getCountryName());
        dto.setRegion_ID(country.getRegion().getRegionId());
        return dto;
    }
}
