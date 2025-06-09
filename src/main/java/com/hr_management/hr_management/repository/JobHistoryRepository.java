package com.hr_management.hr_management.repository;

import com.hr_management.hr_management.model.entity.JobHistory;
import com.hr_management.hr_management.model.key.JobHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobHistoryRepository extends JpaRepository<JobHistory, JobHistoryId> {

}
