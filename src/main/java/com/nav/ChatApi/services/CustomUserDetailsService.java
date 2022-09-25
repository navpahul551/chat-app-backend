package com.nav.ChatApi.services;

import com.nav.ChatApi.entities.User;
import com.nav.ChatApi.exceptions.UserNotFoundException;
import com.nav.ChatApi.models.UserPrincipal;
import com.nav.ChatApi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return UserPrincipal.create(user);
    }
}
