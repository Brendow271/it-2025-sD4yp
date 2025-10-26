package com.harmony.dto;

import com.harmony.entity.UserInfo;

public class UserInfoResponse {
    private Long userId;
    private Integer age;
    private String[] genres;
    private String[] instruments;
    private String location;
    private String about;
    private boolean success;
    private String message;

    public UserInfoResponse() {}

    public UserInfoResponse(UserInfo userInfo) {
        this.userId = userInfo.getUserId();
        this.age = userInfo.getAge();
        this.genres = userInfo.getGenres();
        this.instruments = userInfo.getInstrument();
        this.location = userInfo.getLocation();
        this.about = userInfo.getAbout();
        this.success = true;
        this.message = "Успешно";
    }

    public UserInfoResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Long getUserId() {
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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
