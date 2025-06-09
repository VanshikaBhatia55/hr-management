package com.hr_management.hr_management.model.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeBasicDTO(BigDecimal id, String fullName, LocalDate hireDate, String jobTitle) {
}
