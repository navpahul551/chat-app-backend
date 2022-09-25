package com.nav.ChatApi.repositories;

import com.nav.ChatApi.entities.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(Role.Rolename name);
}
