package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.mapper.CountryMapper;
import com.hr_management.hr_management.model.dto.ApiResponseDto;
import com.hr_management.hr_management.model.dto.country.CountryDTO;
import com.hr_management.hr_management.model.dto.country.countryCountInterface;
import com.hr_management.hr_management.model.entity.Country;
import com.hr_management.hr_management.repository.CountryRepository;
import com.hr_management.hr_management.service.CountryService;
import com.hr_management.hr_management.utils.BuildResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/countries")
public class CountryController {
    @Autowired
    private CountryRepository countryRepo;

    @Autowired
    private CountryMapper mapper;

    @Autowired
    private CountryService countryService;

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

    @PostMapping
    public ResponseEntity<CountryDTO> save(@RequestBody CountryDTO countryDTO) {
        Country country = new Country();
        country.setCountryName(countryDTO.getCountryName());
        country.setRegion(countryDTO.getRegion());
        country.setCountryId(countryDTO.getCountryId());

        return ResponseEntity.ok(mapper.mapToCountryDTO(countryService.checkRegionId(country)));
    }

    @PutMapping("/{countryId}")
    public ResponseEntity<CountryDTO> update(@RequestBody CountryDTO countryDTO, @PathVariable("countryId") String countryId) {
        Country country = countryRepo.findByCountryId(countryId);
        if (country == null) {
            return ResponseEntity.notFound().build();
        }
        country.setCountryName(countryDTO.getCountryName());
        country.setRegion(countryDTO.getRegion());

        return ResponseEntity.ok(mapper.mapToCountryDTO(countryRepo.save(country)));
    }

    @GetMapping("/count_by_region")
    public ResponseEntity<List<countryCountInterface>> countByRegion(){
        List<countryCountInterface> countryCountInterfaceList = countryRepo.countCountriesByRegion() ;
        return ResponseEntity.ok(countryCountInterfaceList);
    }
}