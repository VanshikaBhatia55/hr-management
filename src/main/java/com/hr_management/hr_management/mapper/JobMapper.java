package com.hr_management.hr_management.mapper;

import com.hr_management.hr_management.model.dto.JobDTO;
import com.hr_management.hr_management.model.entity.Job;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    public JobDTO toJobDTO(Job job) {
        JobDTO jobDTO = new JobDTO();
        jobDTO.setJobId(job.getJobId());
        jobDTO.setJobTitle(job.getJobTitle());
        jobDTO.setMaxSalary(job.getMaxSalary());
        jobDTO.setMinSalary(job.getMinSalary());

        return jobDTO;
    }

    public Job toJobEntity(JobDTO jobDTO) {
        if (jobDTO == null) return null;

        Job job = new Job();
        job.setJobId(jobDTO.getJobId());
        job.setJobTitle(jobDTO.getJobTitle());
        job.setMinSalary(jobDTO.getMinSalary());
        job.setMaxSalary(jobDTO.getMaxSalary());
        return job;
    }

}
