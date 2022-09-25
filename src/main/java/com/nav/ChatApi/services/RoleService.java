package com.nav.ChatApi.services;

import com.nav.ChatApi.entities.Role;
import com.nav.ChatApi.exceptions.BadRequestException;
import com.nav.ChatApi.exceptions.NotFoundException;
import com.nav.ChatApi.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role findOrCreateRole(Role.Rolename name) {
        Optional<Role> optionalRole = roleRepository.findByName(name);
        if(optionalRole.isPresent()) return optionalRole.get();
        Role newRole = new Role(name);
        return roleRepository.save(newRole);
    }

    public Role findRoleByName(Role.Rolename name) throws NotFoundException {
        return roleRepository.findByName(name).orElseThrow(() -> new NotFoundException("Role not found!"));
    }

    public Role addRole(Role.Rolename name) throws BadRequestException {
        boolean isRolePresent = roleRepository.findByName(name).isPresent();
        if(isRolePresent){
            throw new BadRequestException("Role already exists!");
        }
        return roleRepository.save(new Role(name));
    }
}
