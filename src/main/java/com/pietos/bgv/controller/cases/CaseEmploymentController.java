package com.pietos.bgv.controller.cases;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pietos.bgv.dto.request.cases.CaseAssignmentRequest;
import com.pietos.bgv.dto.request.cases.CaseEmploymentRequest;
import com.pietos.bgv.dto.request.cases.CaseProcessingStatusUpdateRequest;
import com.pietos.bgv.dto.request.cases.CaseRejectRequest;
import com.pietos.bgv.dto.response.cases.CaseEmploymentMyCaseResponse;
import com.pietos.bgv.dto.response.cases.CaseEmploymentOpenCaseResponse;
import com.pietos.bgv.dto.response.cases.CaseEmploymentReassignResponse;
import com.pietos.bgv.dto.response.cases.CaseEmploymentResponse;
import com.pietos.bgv.service.cases.CaseEmploymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/case-employment")
public class CaseEmploymentController {

    private final CaseEmploymentService caseEmploymentService;

    public CaseEmploymentController(
            CaseEmploymentService caseEmploymentService) {

        this.caseEmploymentService =
                caseEmploymentService;
    }

    // =====================================================
    // CREATE EMPLOYMENT
    // =====================================================

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<CaseEmploymentResponse> createEmployment(
            @RequestBody CaseEmploymentRequest request) {

        CaseEmploymentResponse response =
                caseEmploymentService.createEmployment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =====================================================
    // UPDATE EMPLOYMENT
    // =====================================================

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<CaseEmploymentResponse> updateEmployment(
            @PathVariable Long id,
            @RequestBody CaseEmploymentRequest request) {

        CaseEmploymentResponse response =
                caseEmploymentService.updateEmployment(
                        id,
                        request);

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // GET EMPLOYMENT BY ID
    // =====================================================

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER', 'PROCESS_TEAM_MEMBER', 'PROCESS_TEAM_LEADER','QC_CASE_MEMBER')")
    public ResponseEntity<CaseEmploymentResponse> getEmploymentById(
            @PathVariable Long id) {

        CaseEmploymentResponse response = caseEmploymentService.getEmploymentById(id);

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // GET EMPLOYMENT BY CASE ID
    // =====================================================

    @GetMapping("/case/{caseId}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<List<CaseEmploymentResponse>>
            getEmploymentByCaseId(
                    @PathVariable Long caseId) {

        List<CaseEmploymentResponse> response =
                caseEmploymentService
                        .getEmploymentByCaseId(caseId);

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // GET EMPLOYMENT BY CASE REF
    // =====================================================

    @GetMapping("/case-ref/{caseRef}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<List<CaseEmploymentResponse>>
            getEmploymentByCaseRef(
                    @PathVariable String caseRef) {

        List<CaseEmploymentResponse> response =
                caseEmploymentService
                        .getEmploymentByCaseRef(caseRef);

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // GET EMPLOYMENT BY CASE PACKAGE COMPONENT
    // =====================================================

    @GetMapping("/component/{casePackageComponentId}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<List<CaseEmploymentResponse>>
            getEmploymentByCasePackageComponentId(
                    @PathVariable Long casePackageComponentId) {

        List<CaseEmploymentResponse> response =
                caseEmploymentService
                        .getEmploymentByCasePackageComponentId(
                                casePackageComponentId);

        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/assign")
    @PreAuthorize(
        "hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'PROCESS_TEAM_LEADER')"
    )
    public ResponseEntity<Void> assignEmploymentCases(
            @RequestBody CaseAssignmentRequest request) {

        caseEmploymentService.assignEmploymentCases(request);

        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/open")
    @PreAuthorize(
            "hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'PROCESS_TEAM_LEADER')"
    )
    public ResponseEntity<List<CaseEmploymentOpenCaseResponse>>
    getOpenEmploymentCases() {

        return ResponseEntity.ok(
                caseEmploymentService.getOpenEmploymentCases()
        );
    }
    
    @GetMapping("/reassign")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'PROCESS_TEAM_LEADER')")
    public ResponseEntity<List<CaseEmploymentReassignResponse>> getAssignedEmploymentCases() {

        return ResponseEntity.ok(
                caseEmploymentService.getAssignedEmploymentCases()
        );
    }
    
    @PutMapping("/reassign")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','PROCESS_TEAM_LEADER')")
    public ResponseEntity<Void> reassignEmploymentCases(
            @RequestBody CaseAssignmentRequest request) {

        caseEmploymentService.reassignEmploymentCases(request);

        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/my-cases")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','PROCESS_TEAM_LEADER','QC_CASE_MEMBER')")
    public ResponseEntity<Page<CaseEmploymentMyCaseResponse>>
    getMyEmploymentCases(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        if (page < 0) {
            page = 0;
        }

        if (size < 1) {
            size = 50;
        }

        if (size > 100) {
            size = 100;
        }

        Pageable pageable =  PageRequest.of(page, size);

        return ResponseEntity.ok(
                caseEmploymentService.getMyEmploymentCases(
                        pageable
                )
        );
    }
    
    @PatchMapping("/refuse/{id}")
    @PreAuthorize( "hasAnyAuthority('SUPER_ADMIN','ADMIN','PROCESS_TEAM_LEADER')")
    public ResponseEntity<String> refuseEmploymentCase(
            @PathVariable Long id,
            @RequestBody CaseRejectRequest request) {

        String rejectionComment =
                caseEmploymentService.refuseEmploymentCase(
                        id,
                        request.getRejectionComment());

        return ResponseEntity.ok(rejectionComment);
    }
    
    
    @PutMapping("/{id}/selected-fields")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'PROCESS_TEAM_LEADER', 'PROCESS_TEAM_MEMBER','QC_CASE_MEMBER')" )
    public ResponseEntity<CaseEmploymentResponse>
            updateSelectedEmploymentFields(
                    @PathVariable Long id,
                    @RequestBody CaseEmploymentRequest request) {

        CaseEmploymentResponse response =
                caseEmploymentService.updateSelectedEmploymentFields(
                        id,
                        request);

        return ResponseEntity.ok(response);
    
}
    
    @PatchMapping("/{id}/start-processing")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'PROCESS_TEAM_LEADER', 'PROCESS_TEAM_MEMBER')" )
    public ResponseEntity<?> startProcessing(@PathVariable Long id) {

        caseEmploymentService.startProcessing(id);

        return ResponseEntity.ok(
                "Employment processing started successfully."
        );
    }
    
    @PatchMapping("/{id}/processing-status")
    @PreAuthorize(
            "hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'PROCESS_TEAM_MEMBER', 'PROCESS_TEAM_LEADER','QC_CASE_MEMBER')"
    )
    public ResponseEntity<?> updateProcessingStatus(
            @PathVariable Long id,
            @RequestBody CaseProcessingStatusUpdateRequest request) {

        caseEmploymentService.updateProcessingStatus(
                id,
                request.getStatus(),
                request.getDispositionStatus(),
                request.getActivity()
        );

        return ResponseEntity.ok(
                "Employment processing status updated successfully."
        );
    }
}