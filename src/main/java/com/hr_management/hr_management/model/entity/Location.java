package com.hr_management.hr_management.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @Column(name = "location_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigDecimal locationId;

    @Column(name = "street_address", length = 40)
    private String streetAddress;

    @Column(name = "postal_code", length = 12)
    private String postalCode;

    @Column(name = "city", nullable = false, length = 30)
    private String city;

    @Column(name = "state_province", length = 25)
    private String stateProvince;

    @JsonManagedReference(value = "country-locations")
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @JsonBackReference(value = "location-departments")
    @OneToMany(mappedBy = "location")
    private List<Department> departments;
}
