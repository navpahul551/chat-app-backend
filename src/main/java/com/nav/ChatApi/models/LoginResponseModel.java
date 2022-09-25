package com.nav.ChatApi.models;

import lombok.Data;

@Data
public class LoginResponseModel {
    public String accessToken;
    public int expiresIn;

    public LoginResponseModel(){}

    public LoginResponseModel(String accessToken, int expiresIn){
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }
}
