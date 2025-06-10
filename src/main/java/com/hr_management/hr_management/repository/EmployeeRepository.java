package com.hr_management.hr_management.repository;

import com.hr_management.hr_management.model.entity.Employee;
import com.hr_management.hr_management.model.entity.Location;
import com.hr_management.hr_management.model.entity.Region;
import com.hr_management.hr_management.model.projection.ManagerGroupView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


//@RepositoryRestResource(path = "employee")
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, BigDecimal> {

    List<Employee> findAllByHireDate(LocalDate hireDate);

    List<Employee> findByDepartment_DepartmentId(BigDecimal departmentId);

    List<Employee> findByManagerEmployeeId(BigDecimal managerId);

    List<Employee> findByJob_JobId(String jobId);

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByFirstNameAndLastName(String firstName, String lastName);

    List<Employee> findBySalaryGreaterThan(BigDecimal amount , Pageable pageable);

    List<Employee> findBySalaryLessThan(BigDecimal amount);

    Page<Employee> findByDepartmentLocationCountryRegion(Region region, Pageable page);

    long countByDepartmentLocation(Location location);

    List<Employee> findByHireDateAfter(LocalDate date , Pageable page);

    List<Employee> findByHireDateBefore(LocalDate date , Pageable page);

    List<Employee> findAllByOrderBySalaryDesc();

    List<Employee> findByHireDateBetween(LocalDate start, LocalDate end);

    @Query("SELECT CONCAT(m.firstName, ' ', m.lastName) AS managerName, " +
            "m.department.departmentName AS managerDepartment, " +
            "COUNT(e) AS employeeCount " +
            "FROM Employee e JOIN e.manager m " +
            "GROUP BY m.firstName, m.lastName, m.department.departmentName")
    List<ManagerGroupView> findEmployeesGroupedByManager();

}
