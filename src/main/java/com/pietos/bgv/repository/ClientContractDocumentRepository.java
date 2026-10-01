package com.pietos.bgv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pietos.bgv.entity.ClientContractDocument;

public interface ClientContractDocumentRepository
        extends JpaRepository<ClientContractDocument, Long> {

    List<ClientContractDocument> findByClientContractId(
            Long contractId);
}