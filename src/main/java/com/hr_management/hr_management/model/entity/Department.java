package com.hr_management.hr_management.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "departments")
@Getter
@Setter
@ToString(exclude = {"manager", "location", "employees", "jobHistories"})

public class Department {
    @Id
    @Column(name = "department_id", nullable = false, precision = 4, scale = 0)
    private BigDecimal departmentId;

    @Column(name = "department_name", nullable = false, length = 30)
    private String departmentName;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees;

    @OneToMany(mappedBy = "department")
    private List<JobHistory> jobHistories;

}
