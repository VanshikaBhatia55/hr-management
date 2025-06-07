package com.hr_management.hr_management.controller;
import com.hr_management.hr_management.mapper.JobMapper;
import com.hr_management.hr_management.model.dto.JobDTO;
import com.hr_management.hr_management.model.entity.Job;
import com.hr_management.hr_management.repository.JobRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobRepository jobRepository;

    private final JobMapper jobMapper;




    public JobController(JobRepository jobRepository , JobMapper jobMapper) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
    }

    // Get the list of all jobs
     @GetMapping
     public ResponseEntity<List<JobDTO>> findAll() {
         List<Job> jobs = jobRepository.findAll();
         List<JobDTO> jobDTOs = jobs.stream()
                 .map(jobMapper::toJobDTO)
                 .collect(Collectors.toList());
         return ResponseEntity.ok(jobDTOs);
     }

     // Get the job by job id
    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> getJobById(@PathVariable("id") String jobId) {
        return jobRepository.findById(jobId)
                .map(job -> ResponseEntity.ok(jobMapper.toJobDTO(job)))
                .orElse(ResponseEntity.notFound().build());
    }


    //update the job by id
    @PutMapping("/{id}")
    public ResponseEntity<JobDTO> updateJob(@PathVariable("id") String jobId,
                                            @RequestBody JobDTO jobDTO) {
        return jobRepository.findById(jobId)
                .map(existingJob -> {
                    existingJob.setJobTitle(jobDTO.getJobTitle());
                    existingJob.setMinSalary(jobDTO.getMinSalary());
                    existingJob.setMaxSalary(jobDTO.getMaxSalary());
                    Job updatedJob = jobRepository.save(existingJob);
                    return ResponseEntity.ok(jobMapper.toJobDTO(updatedJob));
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
