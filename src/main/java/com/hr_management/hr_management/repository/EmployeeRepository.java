package com.hr_management.hr_management.repository;

import com.hr_management.hr_management.model.entity.Employee;
import com.hr_management.hr_management.model.entity.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.math.BigDecimal;
import java.util.List;

@RepositoryRestResource(path = "employee")
public interface EmployeeRepository extends JpaRepository<Employee, BigDecimal> {

    Page<Employee> findByDepartmentLocationCountryRegion(Region region, Pageable page);
}
