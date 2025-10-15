package com.harmony.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_info")
public class UserInfo {

    @Id
    @GeneratedValue
    private Long userId;

    @Column(name = "age", nullable = true)
    private Long age;

    @Column(name = "genres", nullable = false)
    private String genres;

    @Column(name = "instrument", nullable = false)
    private String instrument;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "about", nullable = true)
    private String about;

    public UserInfo(){}

    public UserInfo(Long age, String about){
        this.age = age;
        this.about = about;
    }

    public Long getUserId(){
        return userId;
    }
    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
