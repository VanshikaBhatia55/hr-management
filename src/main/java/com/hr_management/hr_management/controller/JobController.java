package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.exception.ResourceAlreadyExistsException;
import com.hr_management.hr_management.exception.ResourceNotFoundException;
import com.hr_management.hr_management.mapper.EmployeeMapper;
import com.hr_management.hr_management.mapper.JobMapper;
import com.hr_management.hr_management.model.dto.ApiResponseDto;
import com.hr_management.hr_management.model.dto.EmployeeJobDTO;
import com.hr_management.hr_management.model.dto.JobDTO;
import com.hr_management.hr_management.model.entity.Employee;
import com.hr_management.hr_management.model.entity.Job;
import com.hr_management.hr_management.repository.JobRepository;
import com.hr_management.hr_management.utils.BuildResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")  // Base path for job-related endpoints
public class JobController {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final EmployeeMapper employeeMapper;

    // Constructor injection
    public JobController(JobRepository jobRepository, JobMapper jobMapper , EmployeeMapper employeeMapper) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
        this.employeeMapper = employeeMapper;
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


    // Get list of employees in a particular salary range
    @GetMapping("/salary_range/{min}/{max}")
    public ResponseEntity<ApiResponseDto> getEmployeesBySalaryRange(@PathVariable("min") BigDecimal min,
                                                                    @PathVariable("max") BigDecimal max,
                                                                    HttpServletRequest request) {
        List<Job> jobs = jobRepository.findByMinSalaryLessThanEqualAndMaxSalaryGreaterThanEqual(max, min);

        List<EmployeeJobDTO> employees = jobs.stream()
                .flatMap(job -> job.getEmployees().stream())
                .map(employeeMapper::toEmployeeJobDTO)
                .collect(Collectors.toList());

        return BuildResponse.success(employees, "Employees in salary range", request.getRequestURI());
    }


    // create a job
    @PostMapping
    public ResponseEntity<ApiResponseDto> createJob(@RequestBody JobDTO jobDTO, HttpServletRequest request) {
        if (jobRepository.existsById(jobDTO.getJobId())) {
            throw new ResourceAlreadyExistsException("Job with id " + jobDTO.getJobId() + " already exists");
        }

        Job job = jobMapper.toJobEntity(jobDTO);
        Job savedJob = jobRepository.save(job);
        JobDTO savedJobDTO = jobMapper.toJobDTO(savedJob);

        return BuildResponse.success(savedJobDTO, "Job created successfully", request.getRequestURI());
    }

    //Get job by job title
    @GetMapping("/title/{jobTitle}")
    public ResponseEntity<ApiResponseDto> getJobByTitle(@PathVariable("jobTitle") String jobTitle,
                                                        HttpServletRequest request) {
        Job job = jobRepository.findByJobTitle(jobTitle)
                .orElseThrow(() -> new ResourceNotFoundException("Job with title '" + jobTitle + "' does not exist"));

        JobDTO jobDTO = jobMapper.toJobDTO(job);
        return BuildResponse.success(jobDTO, "Job fetched successfully by title", request.getRequestURI());
    }

    // list of emoployes in perticular jobs

    @GetMapping("/{job_id}/employees")
    public ResponseEntity<ApiResponseDto> getEmployeesForJob(
            @PathVariable("job_id") String jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeePage = jobRepository.findEmployeesByJobId(jobId, pageable);

        List<EmployeeJobDTO> employeeDTOs = employeePage
                .getContent()
                .stream()
                .map(employeeMapper::toEmployeeJobDTO)
                .toList();

        // Wrap pagination metadata if you want
        var responseData = new java.util.HashMap<String, Object>();
        responseData.put("content", employeeDTOs);
        responseData.put("pageNumber", employeePage.getNumber());
        responseData.put("totalPages", employeePage.getTotalPages());
        responseData.put("totalElements", employeePage.getTotalElements());
        responseData.put("pageSize", employeePage.getSize());

        return BuildResponse.success(responseData, "Paginated employee list", request.getRequestURI());
    }

}
