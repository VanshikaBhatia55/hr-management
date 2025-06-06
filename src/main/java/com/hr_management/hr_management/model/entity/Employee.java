package com.hr_management.hr_management.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @Column(name = "employee_id", nullable = false)
    private BigDecimal employeeId;

    @Column(name = "first_name", length = 20)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 25)
    private String lastName;

    @Column(name = "email", nullable = false, length = 25, unique = true)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(name = "salary")
    private BigDecimal salary;

    @Column(name = "commission_pct")
    private BigDecimal commissionPct;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "manager")
    private List<Employee> subordinates;

    @OneToMany(mappedBy = "employee")
    private List<JobHistory> jobHistories;
}