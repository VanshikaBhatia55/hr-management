package com.hr_management.hr_management.model.projection;

import java.math.BigDecimal;

public interface DepartmentSalaryProjection {
    String getDepartmentName();
    BigDecimal getAverageSalary();
}