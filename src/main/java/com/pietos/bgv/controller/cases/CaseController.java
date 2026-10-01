package com.pietos.bgv.controller.cases;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pietos.bgv.dto.request.cases.CaseRequest;
import com.pietos.bgv.dto.response.ApiResponse;
import com.pietos.bgv.dto.response.cases.CaseResponse;
import com.pietos.bgv.service.cases.CaseService;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    private final CaseService caseService;

    public CaseController(
            CaseService caseService) {

        this.caseService = caseService;
    }

    // =========================
    // CREATE CASE
    // =========================
    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<CaseResponse>>
            createCase(
                    @RequestBody CaseRequest request) {

        CaseResponse response = caseService.createCase(request);

        ApiResponse<CaseResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Case created successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    // =========================
    // GET MY CASES
    // =========================
    @GetMapping("/my")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER','PROCESS_TEAM_LEADER','QC_CASE_MEMBER')")
    public ResponseEntity<ApiResponse<List<CaseResponse>>>
            getMyCases() {

        List<CaseResponse> response =
                caseService.getMyCases();

        ApiResponse<List<CaseResponse>> apiResponse =
                new ApiResponse<>(
                        true,
                        "My cases fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    // =========================
    // GET CASE BY ID
    // =========================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER','PROCESS_TEAM_LEADER','QC_CASE_MEMBER')")
    public ResponseEntity<ApiResponse<CaseResponse>>
            getCaseById(
                    @PathVariable Long id) {

        CaseResponse response =
                caseService.getCaseById(id);

        ApiResponse<CaseResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Case fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    // =========================
    // GET ALL CASES
    // =========================
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER','PROCESS_TEAM_LEADER','QC_CASE_MEMBER')")
    public ResponseEntity<ApiResponse<List<CaseResponse>>>
            getAllCases() {

        List<CaseResponse> response =
                caseService.getAllCases();

        ApiResponse<List<CaseResponse>> apiResponse =
                new ApiResponse<>(
                        true,
                        "Cases fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    // =========================
    // UPDATE CASE
    // =========================
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER', 'PROCESS_TEAM_LEADER')")
    public ResponseEntity<ApiResponse<CaseResponse>>
            updateCase(
                    @PathVariable Long id,
                    @RequestBody CaseRequest request) {

        CaseResponse response =
                caseService.updateCase(
                        id,
                        request);

        ApiResponse<CaseResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Case updated successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }
}