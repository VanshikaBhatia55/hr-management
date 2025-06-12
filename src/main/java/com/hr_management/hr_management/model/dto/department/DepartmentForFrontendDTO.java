package com.hr_management.hr_management.model.dto.department;

import com.hr_management.hr_management.model.dto.LocationDTO;
import com.hr_management.hr_management.model.entity.Country;
import com.hr_management.hr_management.model.entity.Location;
import com.hr_management.hr_management.model.entity.Region;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepartmentForFrontendDTO {
    private BigDecimal departmentId;
    private String departmentName;
    private LocationDTO location;
    private String countryName;
    private String regionName;
}
