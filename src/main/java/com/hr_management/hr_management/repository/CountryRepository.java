package com.hr_management.hr_management.repository;

import com.hr_management.hr_management.model.dto.country.countryCountInterface;
import com.hr_management.hr_management.model.entity.Country;
import com.hr_management.hr_management.model.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;


public interface CountryRepository extends JpaRepository<Country, String> {
    public Country findByCountryId(String countryId) ;

    public List<Country> findByRegion_RegionId(BigDecimal regionRegionId);


    @Query("SELECT c.region.regionId as regionId, COUNT(c) as countryCount FROM Country c GROUP BY c.region.regionId")
    public List<countryCountInterface> countCountriesByRegion();

}
