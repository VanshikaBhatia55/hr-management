package com.hr_management.hr_management.repository;

import com.hr_management.hr_management.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface EmployeeRepository extends JpaRepository<Employee, BigDecimal> {
}
