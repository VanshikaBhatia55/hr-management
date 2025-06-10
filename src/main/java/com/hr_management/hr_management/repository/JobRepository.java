package com.hr_management.hr_management.repository;

import com.hr_management.hr_management.model.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, String> {

    List<Job> findByMinSalaryLessThanEqualAndMaxSalaryGreaterThanEqual(BigDecimal max, BigDecimal min);

    Optional<Job> findByJobTitle(String jobTitle);





}
