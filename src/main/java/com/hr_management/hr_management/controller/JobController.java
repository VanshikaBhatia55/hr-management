package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.exception.ResourceNotFoundException;
import com.hr_management.hr_management.mapper.JobMapper;
import com.hr_management.hr_management.model.dto.ApiResponseDto;
import com.hr_management.hr_management.model.dto.JobDTO;
import com.hr_management.hr_management.model.entity.Job;
import com.hr_management.hr_management.repository.JobRepository;
import com.hr_management.hr_management.utils.BuildResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    // Get the list of all jobs
    @GetMapping
    public ResponseEntity<ApiResponseDto> findAll(HttpServletRequest request) {
        List<Job> jobs = jobRepository.findAll();
        List<JobDTO> jobDTOs = jobs.stream()
                .map(jobMapper::toJobDTO)
                .collect(Collectors.toList());
        return BuildResponse.success(jobDTOs, "List of all Jobs", request.getRequestURI());
    }

    // Get the job by job id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto> getJobById(@PathVariable("id") String jobId, HttpServletRequest request) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException(jobId + " job does not exist"));

        JobDTO jobDTO = jobMapper.toJobDTO(job);
        return BuildResponse.success(jobDTO, "Job fetched successfully", request.getRequestURI());
    }

    // Update the job by id
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto> updateJob(@PathVariable("id") String jobId,
                                                    @RequestBody JobDTO jobDTO,
                                                    HttpServletRequest request) {
        Job updatedJob = jobRepository.findById(jobId)
                .map(existingJob -> {
                    existingJob.setJobTitle(jobDTO.getJobTitle());
                    existingJob.setMinSalary(jobDTO.getMinSalary());
                    existingJob.setMaxSalary(jobDTO.getMaxSalary());
                    return jobRepository.save(existingJob);
                })
                .orElseThrow(() -> new ResourceNotFoundException(jobId + " job does not exist"));

        JobDTO updatedJobDTO = jobMapper.toJobDTO(updatedJob);

        return BuildResponse.success(updatedJobDTO, "Job updated successfully", request.getRequestURI());
    }

}
