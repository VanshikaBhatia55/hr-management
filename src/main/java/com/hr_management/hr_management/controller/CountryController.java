package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.mapper.CountryMapper;
import com.hr_management.hr_management.model.dto.ApiResponseDto;
import com.hr_management.hr_management.model.dto.country.CountryDTO;
import com.hr_management.hr_management.model.entity.Country;
import com.hr_management.hr_management.repository.CountryRepository;
import com.hr_management.hr_management.utils.BuildResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/countries")
public class CountryController {

    @Autowired
    private CountryRepository countryRepo;

    @Autowired
    private CountryMapper mapper;

    //     Get all countries
    @GetMapping
    public ResponseEntity<ApiResponseDto> findAll(HttpServletRequest request) {
        List<Country> countryList = countryRepo.findAll();
        List<CountryDTO> dtoList = countryList.stream()
                .map(mapper::mapToCountryDTO)
                .collect(Collectors.toList());

        return BuildResponse.success(dtoList, "List of all countries ", request.getRequestURI());
    }

    // Get country by its countryId
    @GetMapping("/{id}")
    public ResponseEntity<CountryDTO> findUsingId(@PathVariable("id") String countryId) {
        Country country = countryRepo.findByCountryId(countryId);

        if (country == null) {
            return ResponseEntity.notFound().build();  // HTTP 404 NOT FOUND
        }

        CountryDTO dto = mapper.mapToCountryDTO(country);
        return ResponseEntity.ok(dto);  // HTTP 200 OK
    }

    //      Get all countries under a specific region
    @GetMapping("/by_region/{region_id}")
    public ResponseEntity<List<CountryDTO>> findByRegionId(@PathVariable("region_id") BigDecimal regionId) {
        List<Country> countryList = countryRepo.findByRegion_RegionId(regionId);

        if (countryList.isEmpty()) {
            return ResponseEntity.notFound().build();  // HTTP 404 NOT FOUND
        }

        List<CountryDTO> dtoList = countryList.stream()
                .map(mapper::mapToCountryDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);  // HTTP 200 OK
    }
}