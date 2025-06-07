package com.hr_management.hr_management.repository;

import com.hr_management.hr_management.model.key.JobHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobHistoryRepository extends JpaRepository<JobHistoryRepository, JobHistoryId> {
}
