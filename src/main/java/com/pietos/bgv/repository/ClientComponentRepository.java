package com.pietos.bgv.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pietos.bgv.entity.ClientComponent;

@Repository
public interface ClientComponentRepository
        extends JpaRepository<ClientComponent, Long> {

    boolean existsByClientInformationIdAndComponentId(
            Long clientId,
            Long componentId);

    List<ClientComponent> findByClientInformationId(Long clientId);

    List<ClientComponent> findByComponentId(Long componentId);

    Optional<ClientComponent> findByClientInformationIdAndComponentId(
            Long clientId,
            Long componentId);
    
    List<ClientComponent>findByClientInformationIdAndIsActiveTrue(Long clientId);

    @Modifying
    @Query("DELETE FROM ClientComponent c WHERE c.clientInformation.id = :clientId")
    void deleteByClientInformationId(@Param("clientId") Long clientId);
    
    @Modifying
    void deleteByClientInformationIdAndComponentId(
            Long clientId,
            Long componentId);
    
    
}