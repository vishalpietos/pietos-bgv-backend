package com.pietos.bgv.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pietos.bgv.entity.ClientInformation;
import com.pietos.bgv.entity.ManageUser;
import com.pietos.bgv.entity.SystemUser;

@Repository
public interface ManageUserRepository extends
        JpaRepository<ManageUser, Long>,
        JpaSpecificationExecutor<ManageUser> {

	Optional<ManageUser> findBySystemUser_Email(String email);

	boolean existsBySystemUser_Email(String email);

    boolean existsByMobile(String mobile);
    
    List<ManageUser> findByClientInformationId(Long clientId);
    
    List<ManageUser> findByClientLocationId(Long locationId);

    boolean existsByEmail(String email);
    
    
    
    
}