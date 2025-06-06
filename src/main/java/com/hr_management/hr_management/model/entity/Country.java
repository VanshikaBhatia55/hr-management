package com.hr_management.hr_management.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List ;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    @Id
    @Column(name = "country_id", nullable = false, length = 4)
    private String countryId;

    @Column(name = "country_name", length = 60)
    private String countryName;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @OneToMany(mappedBy = "country")
    private List<Location> locations;
}