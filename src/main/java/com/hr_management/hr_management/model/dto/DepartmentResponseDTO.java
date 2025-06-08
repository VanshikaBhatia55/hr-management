package com.hr_management.hr_management.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class DepartmentResponseDTO {

    @NotNull(message = "Department ID cannot be null")
    private BigDecimal departmentId;

    @NotBlank(message = "Department name is required")
    @Size(max = 30, message = "Department name must be less than 30 characters")
    private String departmentName;

    private BigDecimal managerId;

    @NotNull(message = "Location ID cannot be null")
    private BigDecimal locationId;
}
