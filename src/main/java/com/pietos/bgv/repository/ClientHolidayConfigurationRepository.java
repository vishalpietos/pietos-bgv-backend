package com.pietos.bgv.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pietos.bgv.entity.ClientHolidayConfiguration;

@Repository
public interface ClientHolidayConfigurationRepository
        extends JpaRepository<ClientHolidayConfiguration, Long> {

    Optional<ClientHolidayConfiguration> findByClientInformationId(
            Long clientId);

    boolean existsByClientInformationId(
            Long clientId);

    void deleteByClientInformationId(
            Long clientId);
}