package com.harmony.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "location")
public class Locations {

    @Id
    @Column(name = "location_name", nullable = false)
    private String locationName;

    public Locations(){}
    public String getLocationName() {
        return locationName;
    }
}
