package com.hr_management.hr_management.model.dto.country;

import java.math.BigDecimal;

public interface countryCountInterface {
    BigDecimal getRegionId();     // or Long, depending on your Region's ID type
    Long getCountryCount();
}
