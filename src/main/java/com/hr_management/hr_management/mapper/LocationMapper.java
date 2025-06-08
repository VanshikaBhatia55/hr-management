package com.hr_management.hr_management.mapper;

import com.hr_management.hr_management.model.dto.LocationDTO;
import com.hr_management.hr_management.model.entity.Country;
import com.hr_management.hr_management.model.entity.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {

    public LocationDTO toLocationDto(Location location) {
        if (location == null) {
            return null;
        }

        LocationDTO dto = new LocationDTO();
        dto.setLocationId(location.getLocationId());
        dto.setStreetAddress(location.getStreetAddress());
        dto.setPostalCode(location.getPostalCode());
        dto.setCity(location.getCity());

        // Safe mapping of countryName
//        Country country = location.getCountry();
//        dto.setCountryName(country != null ? country.getCountryName() : null);

        dto.setCountryName(location.getCountry());
        return dto;
    }
}

