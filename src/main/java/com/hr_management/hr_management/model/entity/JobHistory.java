package com.hr_management.hr_management.model.entity;


import com.hr_management.hr_management.model.key.JobHistoryId;
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

    @EmbeddedId
    private JobHistoryId id;

    @MapsId("employeeId") // 🔥 This tells JPA to map the composite key field to this entity
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
