package com.pietos.bgv.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pietos.bgv.entity.ClientInformation;
import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.enums.ClientStatus;

@Repository
public interface ClientInformationRepository extends
JpaRepository<ClientInformation, Long>,
JpaSpecificationExecutor<ClientInformation> {
	
	

    Optional<ClientInformation> findByClientCode(String clientCode);

    Optional<ClientInformation> findByOfficialEmail(String officialEmail);

    boolean existsByClientCode(String clientCode);

    boolean existsByOfficialEmail(String officialEmail);
    
    @Query("SELECT COUNT(c) FROM ClientInformation c")
    Long getTotalClients();
    
    Optional<ClientInformation> findBySystemUser(SystemUser systemUser);
    
    List<ClientInformation> findByStatus(ClientStatus status);

}