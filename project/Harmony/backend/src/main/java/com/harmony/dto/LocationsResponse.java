package com.harmony.dto;

public class LocationsResponse {
    private String name;
    public LocationsResponse() {}
    public LocationsResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
