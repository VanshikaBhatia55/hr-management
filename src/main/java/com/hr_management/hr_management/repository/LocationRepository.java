package com.hr_management.hr_management.repository;

import com.hr_management.hr_management.model.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface LocationRepository extends JpaRepository<Location, BigDecimal> {
}
