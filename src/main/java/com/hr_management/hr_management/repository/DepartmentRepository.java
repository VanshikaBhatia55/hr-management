package com.hr_management.hr_management.repository;
import com.hr_management.hr_management.model.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface DepartmentRepository extends JpaRepository<Department, BigDecimal> {
}
