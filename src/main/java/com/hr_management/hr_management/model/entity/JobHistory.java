package com.hr_management.hr_management.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "job_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JobHistory {
    @Id
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Id
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}