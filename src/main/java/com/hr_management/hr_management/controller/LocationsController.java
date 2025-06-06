package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.mapper.LocationMapper;
import com.hr_management.hr_management.model.dto.LocationDTO;
import com.hr_management.hr_management.model.entity.Location;
import com.hr_management.hr_management.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationsController {
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationMapper locationMapper;

    @GetMapping
    public List<LocationDTO> findAllLocations() {
        List<Location> locations = locationRepository.findAll();
        return  locations.stream().map(location -> locationMapper.toLocationDto(location)).toList();
    }

    @GetMapping("/{id}")
    public LocationDTO findLocationById(@PathVariable BigDecimal id) {
        return locationMapper.toLocationDto(locationRepository.findById(id).get());
    }
}
