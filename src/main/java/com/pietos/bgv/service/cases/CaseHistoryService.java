package com.pietos.bgv.service.cases;

import java.util.List;

import com.pietos.bgv.dto.response.cases.CaseHistoryResponse;
import com.pietos.bgv.entity.cases.Case;
import com.pietos.bgv.entity.cases.CasePackageComponent;

public interface CaseHistoryService {

    // =========================================================
    // CASE-LEVEL HISTORY
    // =========================================================

    // Create history for overall case action
    // Example:
    // NEW -> Case Created
    // DATA_ENTRY_DONE -> All data entry completed
    // WIP -> Case assigned for processing
    void createHistory(
            Case caseEntity,
            String status,
            String activity);


    // =========================================================
    // CHECK-LEVEL HISTORY
    // =========================================================

    // Create history for a particular check instance
    // Example:
    // PT/ED/0001
    // PT/EM/0002
    void createHistory(
            Case caseEntity,
            CasePackageComponent casePackageComponent,
            String subCaseId,
            String status,
            String activity);


    // =========================================================
    // GET HISTORY
    // =========================================================

    // Get complete history of a case
    // Includes:
    // - Case-level history
    // - Check-level history
    List<CaseHistoryResponse> getHistoryByCaseId(
            Long caseId);


    // Get history of one particular check
    List<CaseHistoryResponse> getHistoryByCaseIdAndSubCaseId(
            Long caseId,
            String subCaseId);


    // Get only case-level history
    List<CaseHistoryResponse> getCaseLevelHistory(
            Long caseId);
}