package com.nav.ChatApi.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Entity
@Table(name="users")
@Getter
@Setter
public class User implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name="email", nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @NotNull
    @Column(name="password", nullable = false)
    private String password;

    @NotNull
    @Column(name="first_name", nullable = false)
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @JsonIgnore
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="user_role", joinColumns=@JoinColumn(name="user_id", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="role_id", referencedColumnName="id"))
    private List<Role> roles;

    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    private List<Group> groups = new ArrayList<>();

    public User() {}

    public User(String email, String password, String firstName, String lastName, List<Role> roles){
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }

    public boolean isEnabled() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }
}
