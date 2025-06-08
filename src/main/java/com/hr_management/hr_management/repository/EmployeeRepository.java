package com.hr_management.hr_management.repository;

import com.hr_management.hr_management.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
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

    List<Employee> findBySalaryGreaterThan(BigDecimal amount);

    List<Employee> findBySalaryLessThan(BigDecimal amount);

}
