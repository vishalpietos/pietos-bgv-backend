package com.pietos.bgv.controller.cases;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pietos.bgv.dto.request.cases.CasePackageRequest;
import com.pietos.bgv.dto.response.ApiResponse;
import com.pietos.bgv.dto.response.cases.CasePackageResponse;
import com.pietos.bgv.service.cases.CasePackageService;

@RestController
@RequestMapping("/api/case-packages")
public class CasePackageController {

    private final CasePackageService casePackageService;

    public CasePackageController(
            CasePackageService casePackageService) {

        this.casePackageService = casePackageService;
    }

    // =====================================================
    // CREATE CASE PACKAGE
    // =====================================================

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<CasePackageResponse>>
            createCasePackage(
                    @RequestBody CasePackageRequest request) {

        CasePackageResponse response =
                casePackageService.createCasePackage(request);

        ApiResponse<CasePackageResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Case package created successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    // =====================================================
    // GET CASE PACKAGE BY CASE ID
    // =====================================================

    @GetMapping("/case/{caseId}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<CasePackageResponse>>
            getCasePackageByCaseId(
                    @PathVariable Long caseId) {

        CasePackageResponse response =
                casePackageService
                        .getCasePackageByCaseId(caseId);

        ApiResponse<CasePackageResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Case package fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    // =====================================================
    // UPDATE CASE PACKAGE
    // =====================================================

    @PutMapping("/case/{caseId}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<CasePackageResponse>>
            updateCasePackage(
                    @PathVariable Long caseId,
                    @RequestBody CasePackageRequest request) {

        CasePackageResponse response =
                casePackageService.updateCasePackage(
                        caseId,
                        request);

        ApiResponse<CasePackageResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Case package updated successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }
}