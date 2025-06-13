package com.hr_management.hr_management.repository;

import com.hr_management.hr_management.model.entity.Employee;
import com.hr_management.hr_management.model.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, String> {

    List<Job> findByMinSalaryLessThanEqualAndMaxSalaryGreaterThanEqual(BigDecimal max, BigDecimal min);

    Optional<Job> findByJobTitle(String jobTitle);

    @Query("SELECT e FROM Employee e WHERE e.job.jobId = :jobId")
    Page<Employee> findEmployeesByJobId(@Param("jobId") String jobId, Pageable pageable);






}
