package com.hr_management.hr_management.repository;

import com.hr_management.hr_management.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.math.BigDecimal;

@RepositoryRestResource(path = "employee")
public interface EmployeeRepository extends JpaRepository<Employee, BigDecimal> {
}
