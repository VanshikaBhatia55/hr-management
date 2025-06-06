package com.hr_management.hr_management.model.dto;

import com.hr_management.hr_management.model.entity.Department;
import com.hr_management.hr_management.model.entity.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.batch.BatchProperties;

import java.time.LocalDate;

@Entity
@Table(name = "job_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobHistoryDTO {
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
    private BatchProperties.Job job;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}