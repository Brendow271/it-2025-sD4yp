package com.harmony.dto;

public class UserInfoRequest {

    private Long userId;
    private Integer age;
    private String[] genres;
    private String[] instruments;
    private String location;
    private String about;
    public UserInfoRequest(){}

    public UserInfoRequest(Long userId, Integer age, String[] genres, String[]instruments, String location, String about){
        this.userId = userId;
        this.age = age;
        this.genres = genres;
        this.instruments = instruments;
        this.location = location;
        this.about = about;
    }

    public Long getUserId() {return userId;}
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

    public String[] getInstruments() {
        return instruments;
    }

    public void setInstruments(String[] instruments) {
        this.instruments = instruments;
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
