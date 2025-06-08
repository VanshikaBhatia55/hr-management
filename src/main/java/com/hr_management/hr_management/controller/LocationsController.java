package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.exception.ResourceNotFoundException;
import com.hr_management.hr_management.mapper.LocationMapper;
import com.hr_management.hr_management.model.dto.ApiResponseDto;
import com.hr_management.hr_management.model.dto.LocationDTO;
import com.hr_management.hr_management.model.entity.Location;
import com.hr_management.hr_management.repository.LocationRepository;
import com.hr_management.hr_management.utils.BuildResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/locations")  // Base path for location-related endpoints
public class LocationsController {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationMapper locationMapper;

    // Return all locations
    @GetMapping
    public ResponseEntity<ApiResponseDto> findAllLocations(HttpServletRequest request) {
        List<LocationDTO> locationDTOs = locationRepository.findAll().stream()
                .map(locationMapper::toLocationDto)
                .collect(Collectors.toList());

        return BuildResponse.success(locationDTOs, "List of all Locations", request.getRequestURI());
    }

    // Return location by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto> findLocationById(@PathVariable BigDecimal id, HttpServletRequest request) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location with ID " + id + " does not exist"));

        LocationDTO locationDTO = locationMapper.toLocationDto(location);

        return BuildResponse.success(locationDTO, "Location fetched successfully", request.getRequestURI());
    }
}
