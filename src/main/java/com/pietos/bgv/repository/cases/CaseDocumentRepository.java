package com.pietos.bgv.repository.cases;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pietos.bgv.entity.cases.CaseDocument;

public interface CaseDocumentRepository
        extends JpaRepository<CaseDocument, Long> {

    List<CaseDocument> findByCaseIdAndIsActiveTrue(
            Long caseId);

    List<CaseDocument> findByCaseRefAndIsActiveTrue(
            String caseRef);

    List<CaseDocument> findByCasePackageComponentsIdAndIsActiveTrue(
            Long casePackageComponentsId);
}