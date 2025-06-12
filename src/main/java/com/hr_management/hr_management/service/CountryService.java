package com.hr_management.hr_management.service;

import com.hr_management.hr_management.exception.DuplicateRegionIdException;
import com.hr_management.hr_management.model.entity.Country;
import com.hr_management.hr_management.model.entity.Region;
import com.hr_management.hr_management.repository.CountryRepository;
import com.hr_management.hr_management.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private RegionRepository regionRepository;

    public Country checkRegionId(Country country){
        Region region = country.getRegion();

        // Check if region has an ID (existing or new)
        if (region != null && region.getRegionId() != null) {
            Optional<Region> existingRegion = regionRepository.findById(region.getRegionId());

            if (existingRegion.isPresent()) {
                throw new DuplicateRegionIdException(
                        "Region with id " + region.getRegionId() + " already exists."
                );}
            // Save new region (only if it doesn't exist)
            Region savedRegion = regionRepository.save(region) ;
            country.setRegion(savedRegion);
        }
        else{
            throw new IllegalArgumentException("Region and regionId must be provided.");
        }
        return countryRepository.save(country);
    }
}