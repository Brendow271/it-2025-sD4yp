package com.harmony.dto;

public class GenresResponse {

    private String name;

    public GenresResponse() {}

    public GenresResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
