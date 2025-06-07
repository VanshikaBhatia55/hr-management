package com.hr_management.hr_management.model.key;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobHistoryId implements Serializable {
    private BigDecimal employeeId;
    private LocalDate startDate;
}