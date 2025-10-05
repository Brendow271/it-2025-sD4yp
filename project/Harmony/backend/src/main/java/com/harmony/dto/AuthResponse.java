package com.harmony.dto;

import com.harmony.entity.UserAuth;

public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private UserAuth user;

    public AuthResponse(){};

    public AuthResponse(String token, UserAuth user){
        this.token = token;
        this.user = user;
    }

    public AuthResponse(String token, String type, UserAuth user){
        this.token = token;
        this.type = type;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserAuth getUser() {
        return user;
    }

    public void setUser(UserAuth user) {
        this.user = user;
    }
}
