package com.hr_management.hr_management.model.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;


@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode // for composite keys
public class JobHistoryId implements Serializable {
    @Column(name = "employee_id")
    private BigDecimal employeeId;

    @Column(name = "start_date")
    private LocalDate startDate;
}