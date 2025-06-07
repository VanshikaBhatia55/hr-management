package com.hr_management.hr_management.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    @Id
    @Column(name = "department_id", nullable = false, precision = 4, scale = 0)
    private BigDecimal departmentId;

    @Column(name = "department_name", nullable = false, length = 30)
    private String departmentName;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @JsonManagedReference(value = "location-departments")
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @JsonBackReference(value = "employee-department")
    @OneToMany(mappedBy = "department")
    private List<Employee> employees;

    @OneToMany(mappedBy = "department")
    private List<JobHistory> jobHistories;
}
