package com.nav.ChatApi.models;

import com.nav.ChatApi.entities.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserPrincipal implements UserDetails {
    private Long id;
    private String email;

    private String username;
    private String password;
    private boolean enabled;
    private boolean locked;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities == null ? new ArrayList<>() : new ArrayList<>(authorities);
    }

    public UserPrincipal(Long id, String email, String password, Collection<? extends GrantedAuthority> authorities){
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities == null ? new ArrayList<>() : authorities;
    }

    public static UserPrincipal create(User user){
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
        return new UserPrincipal(user.getId(), user.getEmail(), user.getPassword(), authorities);
    }

    public Long getId(){
        return this.id;
    }

    @Override
    public String getUsername(){
        return this.email;
    }

    @Override
    public String getPassword(){
        return this.password;
    }

    @Override
    public boolean isEnabled(){
        return this.enabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(username).append(", ")
                .append(password).append(", ")
                .append(email).append(", ")
                .append(enabled).append(", ")
                .append(locked).append(", ")
                .append(authorities);
        return stringBuilder.toString();
    }
}
