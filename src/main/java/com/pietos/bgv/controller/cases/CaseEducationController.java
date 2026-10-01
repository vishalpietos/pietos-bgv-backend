package com.pietos.bgv.controller.cases;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pietos.bgv.dto.request.cases.CaseAssignmentRequest;
import com.pietos.bgv.dto.request.cases.CaseEducationRequest;
import com.pietos.bgv.dto.request.cases.CaseProcessingStatusUpdateRequest;
import com.pietos.bgv.dto.request.cases.CaseRejectRequest;
import com.pietos.bgv.dto.response.cases.CaseEducationMyCaseResponse;
import com.pietos.bgv.dto.response.cases.CaseEducationOpenCaseResponse;
import com.pietos.bgv.dto.response.cases.CaseEducationReassignResponse;
import com.pietos.bgv.dto.response.cases.CaseEducationResponse;
import com.pietos.bgv.service.cases.CaseEducationService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/case-education")
public class CaseEducationController {

    private final CaseEducationService caseEducationService;

    public CaseEducationController(
            CaseEducationService caseEducationService) {
        this.caseEducationService = caseEducationService;
    }

    /**
     * Create Education Verification
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<CaseEducationResponse> createEducation(
            @RequestBody CaseEducationRequest request) {

        CaseEducationResponse response =
                caseEducationService.createEducation(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Get all Education verifications for a Case
     */
    @GetMapping("/case/{caseId}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER','QC_CASE_MEMBER')")
    public ResponseEntity<List<CaseEducationResponse>> getEducationByCaseId(
            @PathVariable Long caseId) {

        return ResponseEntity.ok(
                caseEducationService.getEducationByCaseId(caseId)
        );
    }

    /**
     * Get Education verifications by Case Reference
     */
    @GetMapping("/case-ref/{caseRef}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<List<CaseEducationResponse>> getEducationByCaseRef(
            @PathVariable String caseRef) {

        return ResponseEntity.ok(
                caseEducationService.getEducationByCaseRef(caseRef)
        );
    }

    /**
     * Get Education verifications assigned to a user
     */
   
    /**
     * Get Education verification by case package component
     */
    @GetMapping("/component/{casePackageComponentId}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<List<CaseEducationResponse>>
            getEducationByCasePackageComponentId(
                    @PathVariable Long casePackageComponentId) {

        return ResponseEntity.ok(
                caseEducationService
                        .getEducationByCasePackageComponentId(
                                casePackageComponentId)
        );
    }

    /**
     * Update Education Verification
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<CaseEducationResponse> updateEducation(
            @PathVariable Long id,
            @RequestBody CaseEducationRequest request) {

        return ResponseEntity.ok(
                caseEducationService.updateEducation(id, request)
        );
    }
    
    
    @PutMapping("/{id}/selected-fields")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'PROCESS_TEAM_LEADER', 'PROCESS_TEAM_MEMBER','QC_CASE_MEMBER')")
    public ResponseEntity<?> updateSelectedEducationFields(
            @PathVariable Long id,
            @RequestBody CaseEducationRequest request) {

        CaseEducationResponse response =
                caseEducationService.updateSelectedEducationFields(id, request);

        return ResponseEntity.ok(response);
    }
    
    
    @GetMapping("/open")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'PROCESS_TEAM_LEADER')")
    public ResponseEntity<List<CaseEducationOpenCaseResponse>>
    getOpenEducationCases() {

        return ResponseEntity.ok(
                caseEducationService.getOpenEducationCases()
        );
    }
    
    @PutMapping("/assign")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'PROCESS_TEAM_LEADER')")
    public ResponseEntity<Void> assignEducationCases(
            @RequestBody CaseAssignmentRequest request) {

        caseEducationService.assignEducationCases(request);

        return ResponseEntity.ok().build();
    }
   
    @GetMapping("/reassign")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','PROCESS_TEAM_LEADER')")
    public ResponseEntity<List<CaseEducationReassignResponse>> getAssignedEducationCases() {

        return ResponseEntity.ok(
                caseEducationService.getAssignedEducationCases()
        );
    }
    
    @PutMapping("/reassign")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','PROCESS_TEAM_LEADER')")
    public ResponseEntity<Void> reassignEducationCases(
            @RequestBody CaseAssignmentRequest request) {

        caseEducationService.reassignEducationCases(request);

        return ResponseEntity.ok().build();
    }
    
 // =====================================================
 // GET MY EDUCATION CASES
 // =====================================================
    @GetMapping("/my-cases")
    @PreAuthorize(
            "hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'PROCESS_TEAM_LEADER','QC_CASE_MEMBER')"
    )
    public ResponseEntity<Page<CaseEducationMyCaseResponse>> getMyEducationCases(
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

        Pageable pageable = PageRequest.of(page,size);

        return ResponseEntity.ok(caseEducationService.getMyEducationCases(pageable));
    }

    
    @PatchMapping("/refuse/{id}")
    @PreAuthorize(
            "hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'PROCESS_TEAM_LEADER')"
    )
    public ResponseEntity<String> refuseEducationCase(
            @PathVariable Long id,
            @RequestBody CaseRejectRequest request) {

        String rejectionComment =
                caseEducationService.refuseEducationCase(
                        id,
                        request.getRejectionComment()
                );

        return ResponseEntity.ok(rejectionComment);
    }
 
 /**
  * Get Education Verification by ID
  */
 @GetMapping("/{id}")
 @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'PROCESS_TEAM_MEMBER', 'PROCESS_TEAM_LEADER','QC_CASE_MEMBER')")
 public ResponseEntity<CaseEducationResponse> getEducationById(
         @PathVariable Long id) {

     return ResponseEntity.ok(
             caseEducationService.getEducationById(id)
     );
 }
 
 
 @PatchMapping("/{id}/start-processing")
 @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'PROCESS_TEAM_MEMBER', 'PROCESS_TEAM_LEADER')")
 public ResponseEntity<?> startProcessing(@PathVariable Long id) {

     caseEducationService.startProcessing(id);

     return ResponseEntity.ok("Education processing started successfully.");
    
 }
 
 @PatchMapping("/{id}/processing-status")
 @PreAuthorize(
         "hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'PROCESS_TEAM_MEMBER', 'PROCESS_TEAM_LEADER','QC_CASE_MEMBER')"
 )
 public ResponseEntity<?> updateProcessingStatus(
         @PathVariable Long id,
         @RequestBody CaseProcessingStatusUpdateRequest request) {

     caseEducationService.updateProcessingStatus(
             id,
             request.getStatus(),
             request.getDispositionStatus(),
             request.getActivity()
     );

     return ResponseEntity.ok(
             "Education processing status updated successfully."
     );
 }
}