package com.pietos.bgv.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pietos.bgv.entity.ClientContract;

public interface ClientContractRepository
        extends JpaRepository<ClientContract, Long> {

    // Get all contracts of a client
    List<ClientContract> findByClientInformationId(
            Long clientId);

    // Check overlapping contracts
    boolean existsByClientInformationIdAndWefLessThanEqualAndWetGreaterThanEqual(
            Long clientId,
            LocalDate wet,
            LocalDate wef);
 // Active contracts
    List<ClientContract> findByWefLessThanEqualAndWetGreaterThanEqual(
            LocalDate currentDate1,
            LocalDate currentDate2);

    // Expired contracts
    List<ClientContract> findByWetBefore(
            LocalDate currentDate);

    // Contracts expiring before a date
    List<ClientContract> findByWetBetween(
            LocalDate startDate,
            LocalDate endDate);
    
    boolean existsByClientInformationIdAndWefLessThanEqualAndWetGreaterThanEqualAndIdNot(
            Long clientId,
            LocalDate wet,
            LocalDate wef,
            Long contractId);
}