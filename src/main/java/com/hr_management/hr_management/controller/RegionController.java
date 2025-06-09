package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.exception.ResourceNotFoundException;
import com.hr_management.hr_management.mapper.RegionMapper;
import com.hr_management.hr_management.model.dto.ApiResponseDto;
import com.hr_management.hr_management.model.dto.region.RegionDTO;
import com.hr_management.hr_management.model.entity.Country;
import com.hr_management.hr_management.model.entity.Region;
import com.hr_management.hr_management.repository.RegionRepository;
import com.hr_management.hr_management.utils.BuildResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/regions")
public class RegionController {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private RegionMapper regionMapper;

    // Get all regions
    @GetMapping
    public ResponseEntity<ApiResponseDto> getAllRegions(HttpServletRequest request) {
        List<RegionDTO> regionDTOs = regionRepository.findAll()
                .stream()
                .map(regionMapper::toDTO)
                .collect(Collectors.toList());

        return BuildResponse.success(regionDTOs, "List of all Regions", request.getRequestURI());
    }

    // Get region by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto> getRegionById(@PathVariable BigDecimal id, HttpServletRequest request) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Region with ID " + id + " does not exist"));

        RegionDTO regionDTO = regionMapper.toDTO(region);

        return BuildResponse.success(regionDTO, "Region fetched successfully", request.getRequestURI());
    }

    // Get all countries for a specific region ID
    @GetMapping("/{id}/countries")
    public ResponseEntity<ApiResponseDto> getCountriesByRegionId(@PathVariable BigDecimal id, HttpServletRequest request) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Region with ID " + id + " does not exist"));

        List<Country> countries = region.getCountries();  // Ensure it's `Country`, not `Countries`

        return BuildResponse.success(countries, "Countries under Region ID " + id, request.getRequestURI());
    }



    // Create a new region
    @PostMapping
    public ResponseEntity<ApiResponseDto> createRegion(@RequestBody RegionDTO regionDTO, HttpServletRequest request) {
        Region region = regionMapper.toEntity(regionDTO);
        Region savedRegion = regionRepository.save(region);

        RegionDTO savedRegionDTO = regionMapper.toDTO(savedRegion);

        return BuildResponse.success(savedRegionDTO, "Region created successfully", request.getRequestURI());
    }

    // Update region by ID
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto> updateRegion(@PathVariable BigDecimal id,
                                                       @RequestBody RegionDTO regionDTO,
                                                       HttpServletRequest request) {
        Region updatedRegion = regionRepository.findById(id)
                .map(existingRegion -> {
                    existingRegion.setRegionName(regionDTO.getRegionName());
                    return regionRepository.save(existingRegion);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Region with ID " + id + " does not exist"));

        RegionDTO updatedRegionDTO = regionMapper.toDTO(updatedRegion);

        return BuildResponse.success(updatedRegionDTO, "Region updated successfully", request.getRequestURI());
    }
}
