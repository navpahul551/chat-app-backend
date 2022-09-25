package com.nav.ChatApi.models;

import com.nav.ChatApi.entities.User;
import lombok.Data;

@Data
public class Token {
    private String type;
    private String token;

    private User user;

    public Token(String token, User user){
        this.type = "Bearer";
        this.token = token;
        this.user = user;
    }
}
