package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.mapper.JobMapper;
import com.hr_management.hr_management.model.dto.ApiResponseDto;
import com.hr_management.hr_management.model.dto.JobDTO;
import com.hr_management.hr_management.model.entity.Job;
import com.hr_management.hr_management.repository.JobRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class JobControllerTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private JobMapper jobMapper;

    @InjectMocks
    private JobController jobController;

    @Mock
    private HttpServletRequest request;

    private Job testJob;
    private JobDTO testJobDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testJob = new Job();
        testJob.setJobId("HR_REP");
        testJob.setJobTitle("HR Representative");
        testJob.setMinSalary(BigDecimal.valueOf(3000));
        testJob.setMaxSalary(BigDecimal.valueOf(6000));

        testJobDTO = new JobDTO("HR_REP", "HR Representative", BigDecimal.valueOf(3000), BigDecimal.valueOf(6000));
    }

    @Test
    void testFindAllJobs() {
        List<Job> jobs = List.of(testJob);
        when(jobRepository.findAll()).thenReturn(jobs);
        when(jobMapper.toJobDTO(testJob)).thenReturn(testJobDTO);
        when(request.getRequestURI()).thenReturn("/api/jobs");

        ResponseEntity<ApiResponseDto> response = jobController.findAll(request);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("List of all Jobs", response.getBody().getMessage());
        verify(jobRepository, times(1)).findAll();
    }

    @Test
    void testGetJobById_Success() {
        when(jobRepository.findById("HR_REP")).thenReturn(Optional.of(testJob));
        when(jobMapper.toJobDTO(testJob)).thenReturn(testJobDTO);
        when(request.getRequestURI()).thenReturn("/api/jobs/HR_REP");

        ResponseEntity<ApiResponseDto> response = jobController.getJobById("HR_REP", request);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Job fetched successfully", response.getBody().getMessage());
        assertEquals("HR_REP", ((JobDTO) response.getBody().getData()).getJobId());
    }

    @Test
    void testUpdateJob_Success() {
        Job updated = new Job("HR_REP", "Updated Title", BigDecimal.valueOf(3500), BigDecimal.valueOf(6500), null, null);
        JobDTO updatedDTO = new JobDTO("HR_REP", "Updated Title", BigDecimal.valueOf(3500), BigDecimal.valueOf(6500));

        when(jobRepository.findById("HR_REP")).thenReturn(Optional.of(testJob));
        when(jobRepository.save(any(Job.class))).thenReturn(updated);
        when(jobMapper.toJobDTO(updated)).thenReturn(updatedDTO);
        when(request.getRequestURI()).thenReturn("/api/jobs/HR_REP");

        ResponseEntity<ApiResponseDto> response = jobController.updateJob("HR_REP", updatedDTO, request);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Job updated successfully", response.getBody().getMessage());
        assertEquals("Updated Title", ((JobDTO) response.getBody().getData()).getJobTitle());
    }

    @Test
    void testCreateJob_Success() {
        when(jobRepository.existsById("HR_REP")).thenReturn(false);
        when(jobRepository.save(any(Job.class))).thenReturn(testJob);
        when(jobMapper.toJobDTO(testJob)).thenReturn(testJobDTO);
        when(jobMapper.toJobEntity(testJobDTO)).thenReturn(testJob);
        when(request.getRequestURI()).thenReturn("/api/jobs");

        ResponseEntity<ApiResponseDto> response = jobController.createJob(testJobDTO, request);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Job created successfully", response.getBody().getMessage());
        assertEquals("HR_REP", ((JobDTO) response.getBody().getData()).getJobId());
    }

    @Test
    void testGetJobByTitle_Success() {
        when(jobRepository.findByJobTitle("HR Representative")).thenReturn(Optional.of(testJob));
        when(jobMapper.toJobDTO(testJob)).thenReturn(testJobDTO);
        when(request.getRequestURI()).thenReturn("/api/jobs/title/HR Representative");

        ResponseEntity<ApiResponseDto> response = jobController.getJobByTitle("HR Representative", request);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Job fetched successfully by title", response.getBody().getMessage());
        assertEquals("HR_REP", ((JobDTO) response.getBody().getData()).getJobId());
    }

}

