package com.hr_management.hr_management.repository;
import com.hr_management.hr_management.model.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, BigDecimal> {
    List<Department> findByLocationLocationId(BigDecimal locationId);

    List<Department> findDepartmentsByManager_EmployeeId(BigDecimal managerEmployeeId);

}
