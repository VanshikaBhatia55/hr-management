package com.hr_management.hr_management.mapper;

import com.hr_management.hr_management.model.dto.LocationDTO;
import com.hr_management.hr_management.model.dto.PostLocationDTO;
import com.hr_management.hr_management.model.entity.Country;
import com.hr_management.hr_management.model.entity.Location;
import com.hr_management.hr_management.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {

    @Autowired
    private CountryRepository countryRepository;

    public LocationDTO toLocationDto(Location location) {
        if (location == null) {
            return null;
        }

        LocationDTO dto = new LocationDTO();
        dto.setLocationId(location.getLocationId());
        dto.setStreetAddress(location.getStreetAddress());
        dto.setPostalCode(location.getPostalCode());
        dto.setCity(location.getCity());
        dto.setStateProvince(location.getStateProvince());
        dto.setCountryName(location.getCountry().getCountryName());
        dto.setRegionName(location.getCountry().getRegion().getRegionName());
        return dto;
    }

    public Location toLocationEntity(PostLocationDTO dto, Country country) {
        Location location = new Location();
        location.setLocationId(dto.getLocationId());
        location.setLocationId(dto.getLocationId());
        location.setStreetAddress(dto.getStreetAddress());
        location.setPostalCode(dto.getPostalCode());
        location.setCity(dto.getCity());
        location.setStateProvince(dto.getStateProvince());
        location.setCountry(country);
        return location;
    }

}

