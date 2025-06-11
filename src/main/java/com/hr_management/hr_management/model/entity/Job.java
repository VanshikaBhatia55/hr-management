package com.hr_management.hr_management.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;


@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Job {

    @Id
    @Column(name = "job_id", nullable = false, length = 10)
    private String jobId;

    @Column(name = "job_title", nullable = false, length = 35)
    private String jobTitle;

    @Column(name = "min_salary")
    private BigDecimal minSalary;

    @Column(name = "max_salary")
    private BigDecimal maxSalary;

    @JsonBackReference(value = "job-employees")
    @OneToMany(mappedBy = "job" , fetch = FetchType.EAGER)
    private List<Employee> employees;

    @OneToMany(mappedBy = "job")
    private List<JobHistory> jobHistories;
}
