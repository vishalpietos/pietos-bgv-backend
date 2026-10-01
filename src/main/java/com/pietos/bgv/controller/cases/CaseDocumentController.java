package com.pietos.bgv.controller.cases;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pietos.bgv.dto.response.ApiResponse;
import com.pietos.bgv.dto.response.cases.CaseDocumentResponse;
import com.pietos.bgv.service.cases.CaseDocumentService;

@RestController
@RequestMapping("/api/case-documents")
public class CaseDocumentController {

    private final CaseDocumentService caseDocumentService;

    public CaseDocumentController(
            CaseDocumentService caseDocumentService) {

        this.caseDocumentService = caseDocumentService;
    }

    
    @PostMapping(
            value = "/case/{caseId}/component/{casePackageComponentsId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<
            ApiResponse<CaseDocumentResponse>>
            uploadDocument(

                    @PathVariable Long caseId,

                    @PathVariable Long casePackageComponentsId,

                    @RequestParam("description")
                    String description,

                    @RequestParam("file")
                    org.springframework.web.multipart.MultipartFile file) {

        CaseDocumentResponse response =caseDocumentService.uploadDocument(
                        caseId,
                        casePackageComponentsId,
                        description,
                        file);

        ApiResponse<CaseDocumentResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Document uploaded successfully.",
                        response);

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.CREATED);
    }

    /**
     * Get all active documents for a case.
     */
    @GetMapping("/case/{caseId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<
            ApiResponse<List<CaseDocumentResponse>>>
            getDocumentsByCaseId(
                    @PathVariable Long caseId) {

        List<CaseDocumentResponse> response =
                caseDocumentService
                        .getDocumentsByCaseId(caseId);

        String message = response.isEmpty()
                ? "No documents found for this case."
                : "Case documents fetched successfully.";

        ApiResponse<List<CaseDocumentResponse>> apiResponse =
                new ApiResponse<>(
                        true,
                        message,
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    /**
     * Get documents by case reference.
     */
    @GetMapping("/case-ref/{caseRef}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<
            ApiResponse<List<CaseDocumentResponse>>>
            getDocumentsByCaseRef(
                    @PathVariable String caseRef) {

        List<CaseDocumentResponse> response =
                caseDocumentService
                        .getDocumentsByCaseRef(caseRef);

        String message = response.isEmpty()
                ? "No documents found for this case."
                : "Case documents fetched successfully.";

        ApiResponse<List<CaseDocumentResponse>> apiResponse =
                new ApiResponse<>(
                        true,
                        message,
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    /**
     * Get documents for a specific
     * case package component.
     */
    @GetMapping(
            "/case/{caseId}/component/{casePackageComponentsId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<
            ApiResponse<List<CaseDocumentResponse>>>
            getDocumentsByCasePackageComponentsId(

                    @PathVariable Long caseId,

                    @PathVariable Long casePackageComponentsId) {

        List<CaseDocumentResponse> response =
                caseDocumentService
                        .getDocumentsByCasePackageComponentsId(
                                casePackageComponentsId);

        String message = response.isEmpty()
                ? "No documents found for this component."
                : "Component documents fetched successfully.";

        ApiResponse<List<CaseDocumentResponse>> apiResponse =
                new ApiResponse<>(
                        true,
                        message,
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<
            ApiResponse<CaseDocumentResponse>>
            getDocumentById(
                    @PathVariable Long id) {

        CaseDocumentResponse response =
                caseDocumentService
                        .getDocumentById(id);

        ApiResponse<CaseDocumentResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Document fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    /**
     * Deactivate document.
     */
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<Void>>
            deactivateDocument(
                    @PathVariable Long id) {

        caseDocumentService
                .deactivateDocument(id);

        ApiResponse<Void> apiResponse =
                new ApiResponse<>(
                        true,
                        "Document deactivated successfully.",
                        null);

        return ResponseEntity.ok(apiResponse);
    }
}