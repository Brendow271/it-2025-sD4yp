package com.harmony.dto;

public class InstrumentsResponse {

    private String name;
    private String type;

    public InstrumentsResponse() {}

    public InstrumentsResponse(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
