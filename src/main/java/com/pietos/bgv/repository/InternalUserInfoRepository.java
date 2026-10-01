package com.pietos.bgv.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pietos.bgv.entity.InternalUserInfo;

@Repository
public interface InternalUserInfoRepository
        extends JpaRepository<InternalUserInfo, Long> {

    Optional<InternalUserInfo> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);
    
    boolean existsByMobileNumber(String mobileNumber);
}