package com.pietos.bgv.repository.cases;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pietos.bgv.entity.cases.CaseHistory;

@Repository
public interface CaseHistoryRepository
        extends JpaRepository<CaseHistory, Long> {

    // 1. Get complete history of a case
    // Includes:
    // - Case-level history
    // - Check-level history
    List<CaseHistory> findByCaseEntityIdOrderByCreatedAtDesc(
            Long caseId);


    // 2. Get history of one particular check
    // Example:
    // caseId = 1001
    // subCaseId = PT/ED/0001
    List<CaseHistory> findByCaseEntityIdAndSubCaseIdOrderByCreatedAtDesc(
            Long caseId,
            String subCaseId);


    // 3. Get only case-level history
    // sub_case_id IS NULL
    List<CaseHistory> findByCaseEntityIdAndSubCaseIdIsNullOrderByCreatedAtDesc(
            Long caseId);
}