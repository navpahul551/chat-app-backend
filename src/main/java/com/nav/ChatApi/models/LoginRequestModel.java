package com.nav.ChatApi.models;

import lombok.Data;

@Data
public class LoginRequestModel {
    private String email;
    private String password;

    public LoginRequestModel(){}

    public LoginRequestModel(String email, String password){
        this.email = email;
        this.password = password;
    }
}
