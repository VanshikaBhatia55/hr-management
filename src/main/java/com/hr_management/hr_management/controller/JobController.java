package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.mapper.JobMapper;
import com.hr_management.hr_management.model.dto.JobDTO;
import com.hr_management.hr_management.model.entity.Job;
import com.hr_management.hr_management.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/api/jobs")  // Base path for job-related endpoints
public class JobController {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    // Constructor injection
    public JobController(JobRepository jobRepository, JobMapper jobMapper) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
    }

    // Return all jobs
    @GetMapping
    public List<JobDTO> findAll() {
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream()
                .map(jobMapper::toJobDTO)
                .toList();
    }
}

