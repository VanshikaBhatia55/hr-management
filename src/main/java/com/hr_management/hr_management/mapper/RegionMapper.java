package com.hr_management.hr_management.mapper;

import com.hr_management.hr_management.model.dto.region.RegionDTO;
import com.hr_management.hr_management.model.entity.Region;
import org.springframework.stereotype.Component;



@Component
public class RegionMapper {

    public RegionDTO toDTO(Region region) {
        if (region == null) return null;

        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setRegionId(region.getRegionId());
        regionDTO.setRegionName(region.getRegionName());

        return regionDTO;
    }

    public Region toEntity(RegionDTO dto) {
        if (dto == null) return null;
        Region region = new Region();
        region.setRegionId(dto.getRegionId());
        region.setRegionName(dto.getRegionName());
        region.setCountries(null);

        return region;
    }
}
