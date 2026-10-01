package com.pietos.bgv.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pietos.bgv.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(String roleName);
    
    boolean existsByRoleName(String roleName);

}