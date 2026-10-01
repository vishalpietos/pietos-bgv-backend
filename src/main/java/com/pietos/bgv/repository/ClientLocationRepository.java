package com.pietos.bgv.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pietos.bgv.entity.ClientLocation;
import com.pietos.bgv.enums.ClientLocationStatus;

@Repository
public interface ClientLocationRepository extends
        JpaRepository<ClientLocation, Long>,
        JpaSpecificationExecutor<ClientLocation> {

    List<ClientLocation> findByClientInformationId(Long clientId);
    
    List<ClientLocation> findByClientInformationIdAndStatus(
            Long clientId,  ClientLocationStatus status);

    boolean existsByClientInformationIdAndLocationNameIgnoreCase(
            Long clientId,
            String locationName);
    
    Optional<ClientLocation> findByIdAndClientInformationId(
            Long locationId,
            Long clientId);
    
    boolean existsByOfficialEmailIgnoreCase(String officialEmail);
    

}