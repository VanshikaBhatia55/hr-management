package com.hr_management.hr_management.mapper;

import com.hr_management.hr_management.model.dto.LocationDTO;
import com.hr_management.hr_management.model.entity.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {
    public LocationDTO toLocationDto(Location location) {
        LocationDTO dto = new LocationDTO();
        dto.setLocationId(location.getLocationId());
        dto.setCity(location.getCity());
        dto.setStreetAddress(location.getStreetAddress());
        dto.setPostalCode(location.getPostalCode());
        dto.setCountryName(location.getCountry().getCountryName());
        dto.setCity(location.getCity());
        return dto;
    }
}
