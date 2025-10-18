package com.harmony.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "instruments")
public class Instruments {

    @Id
    @Column(name = "instrument_name", nullable = false)
    private String instrumentName;

    @Column(name = "instrument_type", nullable = false)
    private String instrumentType;

    public Instruments() {}
    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }
}
