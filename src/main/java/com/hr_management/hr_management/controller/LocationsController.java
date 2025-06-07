package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.mapper.LocationMapper;
import com.hr_management.hr_management.model.dto.LocationDTO;
import com.hr_management.hr_management.model.entity.Location;
import com.hr_management.hr_management.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/locations")  // Base path for location-related endpoints
public class LocationsController {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationMapper locationMapper;

    // Return all locations
    @GetMapping
    public ResponseEntity<List<LocationDTO>> findAllLocations() {
        List<Location> locations = locationRepository.findAll();
        return ResponseEntity.ok(locations.stream()
                .map(locationMapper::toLocationDto)
                .toList());
    }

    // Return location by ID
    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> findLocationById(@PathVariable BigDecimal id) {
        Location location = locationRepository.findById(id).get();
        return ResponseEntity.ok(locationMapper.toLocationDto(location));
    }
}

