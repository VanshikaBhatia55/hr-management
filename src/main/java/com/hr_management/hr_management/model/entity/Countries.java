package com.hr_management.hr_management.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List ;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Countries {

    @Id
    @Column(name = "country_id", nullable = false, length = 4)
    private String countryId;

    @Column(name = "country_name", length = 60)
    private String countryName;

    @JsonManagedReference(value = "region-countries")
    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @JsonBackReference(value = "country-locations")
    @OneToMany(mappedBy = "country")
    private List<Location> locations;
}
