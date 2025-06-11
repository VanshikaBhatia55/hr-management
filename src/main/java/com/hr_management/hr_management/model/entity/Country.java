package com.hr_management.hr_management.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List ;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "countries")
public class Country {
    @Id
    @Column(name = "country_id", nullable = false, length = 4)
    private String countryId;

    @Column(name = "country_name", length = 60)
    private String countryName;

    @JoinColumn(name = "region_id")
    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    private Region region;
}
