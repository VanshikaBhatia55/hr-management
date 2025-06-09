package com.hr_management.hr_management.repository;

import com.hr_management.hr_management.model.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, BigDecimal> {
    List<Location> findByCountryCountryId(String countryId);
    List<Location> findByStateProvinceIgnoreCase(String stateProvince);
}
