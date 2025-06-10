package com.hr_management.hr_management.model.dto;

import com.hr_management.hr_management.model.entity.Country;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostLocationDTO {
    private BigDecimal locationId;
    private String streetAddress;
    private String postalCode;
    private String city;
    private String stateProvince;
    private Country country;
}
