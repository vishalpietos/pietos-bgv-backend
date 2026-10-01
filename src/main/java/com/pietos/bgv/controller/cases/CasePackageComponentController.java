package com.pietos.bgv.controller.cases;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pietos.bgv.dto.request.cases.CaseFutureCheckRequest;
import com.pietos.bgv.dto.response.ApiResponse;
import com.pietos.bgv.dto.response.cases.CasePackageComponentResponse;
import com.pietos.bgv.service.cases.CasePackageComponentService;

@RestController
@RequestMapping("/api/case-package-components")
public class CasePackageComponentController {

    private final CasePackageComponentService
            casePackageComponentService;

    public CasePackageComponentController(
            CasePackageComponentService
                    casePackageComponentService) {

        this.casePackageComponentService =
                casePackageComponentService;
    }

    // =====================================================
    // GET COMPONENTS FOR CASE PACKAGE
    // =====================================================

    @GetMapping("/case-package/{casePackageId}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<
            ApiResponse<List<CasePackageComponentResponse>>>
            getComponentsByCasePackageId(
                    @PathVariable Long casePackageId) {

        List<CasePackageComponentResponse> response =
                casePackageComponentService
                        .getComponentsByCasePackageId(
                                casePackageId);

        ApiResponse<List<CasePackageComponentResponse>>
                apiResponse =
                new ApiResponse<>(
                        true,
                        "Case package components fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    // =====================================================
    // DELETE COMPONENTS FOR CASE PACKAGE
    // =====================================================

    @DeleteMapping("/case-package/{casePackageId}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<Void>>
            deleteComponentsByCasePackageId(
                    @PathVariable Long casePackageId) {

        casePackageComponentService
                .deleteComponentsByCasePackageId(
                        casePackageId);

        ApiResponse<Void> apiResponse =
                new ApiResponse<>(
                        true,
                        "Case package components deleted successfully.",
                        null);

        return ResponseEntity.ok(apiResponse);
    }
    
    @PostMapping("/case/{caseId}/future-check")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<
            ApiResponse<CasePackageComponentResponse>>
            createFutureCheck(
                    @PathVariable Long caseId,
                    @RequestBody CaseFutureCheckRequest request) {

        CasePackageComponentResponse response =
                casePackageComponentService.createFutureCheck(
                        caseId,
                        request.getComponentId());

        ApiResponse<CasePackageComponentResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Future check component created successfully.",
                        response);

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.CREATED);
    }
}