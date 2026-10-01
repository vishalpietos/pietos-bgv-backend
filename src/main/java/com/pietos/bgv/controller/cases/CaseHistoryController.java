package com.pietos.bgv.controller.cases;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pietos.bgv.dto.response.ApiResponse;
import com.pietos.bgv.dto.response.cases.CaseHistoryResponse;
import com.pietos.bgv.service.cases.CaseHistoryService;

@RestController
@RequestMapping("/api/cases")
public class CaseHistoryController {

    private final CaseHistoryService caseHistoryService;


    public CaseHistoryController(
            CaseHistoryService caseHistoryService) {

        this.caseHistoryService = caseHistoryService;
    }


    // =========================================================
    // 1. GET COMPLETE CASE HISTORY
    // =========================================================

    @GetMapping("/{caseId}/history")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'PROCESS_TEAM_LEADER','DATA_ENTRY_TEAM_MEMBER','QC_CASE_MEMBER')")
    public ResponseEntity<
            ApiResponse<List<CaseHistoryResponse>>>
            getCaseHistory(
                    @PathVariable Long caseId) {

        List<CaseHistoryResponse> history =
                caseHistoryService
                        .getHistoryByCaseId(caseId);

        ApiResponse<List<CaseHistoryResponse>> response =
                new ApiResponse<>(
                        true,
                        history.isEmpty()
                                ? "No case history found."
                                : "Case history fetched successfully.",
                        history);

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // 2. GET HISTORY FOR PARTICULAR CHECK
    // =========================================================

    @GetMapping("/{caseId}/history/check/{subCaseId}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'PROCESS_TEAM_LEADER','DATA_ENTRY_TEAM_MEMBER','QC_CASE_MEMBER')")
    public ResponseEntity<
            ApiResponse<List<CaseHistoryResponse>>>
            getCheckHistory(
                    @PathVariable Long caseId,
                    @PathVariable String subCaseId) {

        List<CaseHistoryResponse> history =
                caseHistoryService
                        .getHistoryByCaseIdAndSubCaseId(
                                caseId,
                                subCaseId);

        ApiResponse<List<CaseHistoryResponse>> response =
                new ApiResponse<>(
                        true,
                        history.isEmpty()
                                ? "No check history found."
                                : "Check history fetched successfully.",
                        history);

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // 3. GET ONLY CASE-LEVEL HISTORY
    // =========================================================

    @GetMapping("/{caseId}/history/case-level")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'PROCESS_TEAM_LEADER','DATA_ENTRY_TEAM_MEMBER','QC_CASE_MEMBER')")
    public ResponseEntity<
            ApiResponse<List<CaseHistoryResponse>>>
            getCaseLevelHistory(
                    @PathVariable Long caseId) {

        List<CaseHistoryResponse> history =
                caseHistoryService
                        .getCaseLevelHistory(caseId);

        ApiResponse<List<CaseHistoryResponse>> response =
                new ApiResponse<>(
                        true,
                        history.isEmpty()
                                ? "No case-level history found."
                                : "Case-level history fetched successfully.",
                        history);

        return ResponseEntity.ok(response);
    }
}