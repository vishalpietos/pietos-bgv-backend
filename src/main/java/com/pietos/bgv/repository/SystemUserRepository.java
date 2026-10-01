package com.pietos.bgv.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pietos.bgv.entity.SystemUser;

public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {

    Optional<SystemUser> findByEmail(String email);

    Optional<SystemUser> findByMobileNumber(String mobileNumber);

    boolean existsByEmail(String email);

    boolean existsByMobileNumber(String mobileNumber);
    
    List<SystemUser> findByRoleRoleNameAndIsActiveTrue(String roleName);
    
    boolean existsByEmailAndIdNot(String email, Long id);
    
    boolean existsByMobileNumberAndIdNot(String mobileNumber, Long id);
}