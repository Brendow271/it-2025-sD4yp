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

    @Column(name = "genres", nullable = true)
    private String genres;

    @Column(name = "instrument", nullable = true)
    private String instrument;

    @Column(name = "location", nullable = true)
    private String location;

    @Column(name = "about", nullable = false)
    private String about;

    public UserInfo(){}

    public UserInfo(Integer age, String about){
        this.age = age;
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
