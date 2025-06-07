package com.hr_management.hr_management.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "regions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Region {

    @Id
    @Column(name = "region_id", nullable = false)
    private BigDecimal regionId;

    @Column(name = "region_name", length = 25)
    private String regionName;

    @JsonBackReference(value = "region-countries")
    @OneToMany(mappedBy = "region")
    private List<Country> countries;
}
