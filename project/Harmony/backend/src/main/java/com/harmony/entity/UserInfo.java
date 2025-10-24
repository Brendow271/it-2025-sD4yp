package com.harmony.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "genres", nullable = false)
    private String[] genres;

    @Column(name = "instrument", nullable = false)
    private String[] instrument;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "about", nullable = true)
    private String about;

    public UserInfo(){}

    public UserInfo(Integer age, String about){
        this.age = age;
        this.about = about;
    }

    public UserInfo(Integer age, String[] genres, String[] instrument, String location){
        this.age = age;
        this.genres = genres;
        this.instrument = instrument;
        this.location = location;
    }

    public UserInfo(Integer age, String[] genres, String[] instrument, String location, String about){
        this.age = age;
        this.genres = genres;
        this.instrument = instrument;
        this.location = location;
        this.about = about;
    }

    public Long getUserId(){
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public String[] getInstrument() {
        return instrument;
    }

    public void setInstrument(String[] instrument) {
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
