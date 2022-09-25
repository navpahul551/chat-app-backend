package com.nav.ChatApi.models;

import com.nav.ChatApi.entities.Role;
import lombok.Data;

import java.util.List;

@Data
public class SignUpModel {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private List<Role> roles;
}
