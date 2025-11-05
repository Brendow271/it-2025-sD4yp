package com.harmony.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "genres")
public class Genres {

    @Id
    @Column(name = "genres_name", nullable = false)
    private String genresName;

    public Genres() {}

    public String getGenresName() {
        return genresName;
    }

    public void setGenresName(String genresName) {
        this.genresName = genresName;
    }
}
