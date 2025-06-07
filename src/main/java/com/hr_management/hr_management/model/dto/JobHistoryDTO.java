package com.hr_management.hr_management.model.dto;

import com.hr_management.hr_management.model.entity.Department;
import com.hr_management.hr_management.model.entity.Employee;
import com.hr_management.hr_management.model.entity.Job;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.batch.BatchProperties;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobHistoryDTO {
    private Employee employee;
    private LocalDate startDate;
    private LocalDate endDate;
  
    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

}