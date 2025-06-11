package com.hr_management.hr_management.repository;
import com.hr_management.hr_management.model.entity.Department;
import com.hr_management.hr_management.model.projection.DepartmentCountProjection;
import com.hr_management.hr_management.model.projection.DepartmentSalaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, BigDecimal> {
    List<Department> findByLocationLocationId(BigDecimal locationId);

    List<Department> findDepartmentsByManager_EmployeeId(BigDecimal managerEmployeeId);

    List<Department> findByDepartmentName(String departmentName);


    @Query("SELECT d.location.city AS locationCity, COUNT(d) AS departmentCount " +
            "FROM Department d " +
            "GROUP BY d.location.city")
    List<DepartmentCountProjection> countDepartmentsByLocation();


    List<Department> findByManagerIsNull();


    @Query("""
    SELECT d.departmentName AS departmentName, 
           COALESCE(AVG(e.salary), 0) AS averageSalary
    FROM Department d
    LEFT JOIN d.employees e
    GROUP BY d.id, d.departmentName
    ORDER BY d.departmentName
    """)
    Page<DepartmentSalaryProjection> findAverageSalaryByDepartment(Pageable pageable);

}


