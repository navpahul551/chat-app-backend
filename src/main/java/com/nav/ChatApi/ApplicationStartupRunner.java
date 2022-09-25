package com.nav.ChatApi;

import com.nav.ChatApi.entities.Role;
import com.nav.ChatApi.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

@Component
public class ApplicationStartupRunner implements CommandLineRunner {

    @Autowired
    private RoleService roleService;

    @Override
    public void run(String... args) throws Exception {
        EnumSet.allOf(Role.Rolename.class).forEach(roleName -> roleService.findOrCreateRole(roleName));
    }
}
