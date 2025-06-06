package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.model.entity.Location;
import com.hr_management.hr_management.repository.LocationJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationsController {
    @Autowired
    private LocationJPA locationsJPA;

    @GetMapping
    public List<Location> findAllLocations() {
        return locationsJPA.findAll();
    }

    @GetMapping("/{id}")
    public Location findLocationById(@PathVariable BigDecimal id) {
        return locationsJPA.findById(id).get();
    }
}
