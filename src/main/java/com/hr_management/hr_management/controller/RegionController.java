package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.mapper.RegionMapper;
import com.hr_management.hr_management.model.dto.RegionDTO;
import com.hr_management.hr_management.model.entity.Region;
import com.hr_management.hr_management.repository.RegionRepository;
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

    @GetMapping
    public ResponseEntity<List<RegionDTO>> getAllRegions() {
        return ResponseEntity.ok(regionRepository.findAll()
                .stream()
                .map(regionMapper::toDTO)
                .collect(Collectors.toList()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<RegionDTO> getRegionById(@PathVariable BigDecimal id) {
        return regionRepository.findById(id)
                .map(regionMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
