package com.pietos.bgv.controller.cases;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pietos.bgv.dto.request.cases.CaseVendorResponseRequest;
import com.pietos.bgv.dto.response.cases.CaseEducationVendorResponseDTO;
import com.pietos.bgv.dto.response.cases.CaseEmploymentVendorResponseDTO;
import com.pietos.bgv.service.cases.CaseVendorResponseService;

@RestController
@RequestMapping("/api/cases")
public class CaseVendorResponseController {

    private final CaseVendorResponseService caseVendorResponseService;

    public CaseVendorResponseController(
            CaseVendorResponseService caseVendorResponseService) {

        this.caseVendorResponseService =
                caseVendorResponseService;
    }

    // =====================================================
    // EDUCATION - SAVE / UPDATE VENDOR RESPONSE
    // =====================================================

    @PutMapping("/education/{caseEducationId}/vendor-response")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', " + "'PROCESS_TEAM_MEMBER', 'PROCESS_TEAM_LEADER')")
    public ResponseEntity<CaseEducationVendorResponseDTO>
            saveEducationVendorResponse(
                    @PathVariable Long caseEducationId,
                    @RequestBody CaseVendorResponseRequest request) {

        CaseEducationVendorResponseDTO response =
                caseVendorResponseService
                        .saveEducationVendorResponse(
                                caseEducationId,
                                request);

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // EMPLOYMENT - SAVE / UPDATE VENDOR RESPONSE
    // =====================================================

    @PutMapping("/employment/{caseEmploymentId}/vendor-response")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', "+ "'PROCESS_TEAM_MEMBER', 'PROCESS_TEAM_LEADER')")
    public ResponseEntity<CaseEmploymentVendorResponseDTO>
            saveEmploymentVendorResponse(
                    @PathVariable Long caseEmploymentId,
                    @RequestBody CaseVendorResponseRequest request) {

        CaseEmploymentVendorResponseDTO response =
                caseVendorResponseService
                        .saveEmploymentVendorResponse(
                                caseEmploymentId,
                                request);

        return ResponseEntity.ok(response);
    }
    
 // =====================================================
 // GET EDUCATION VENDOR RESPONSE
 // =====================================================

			 @GetMapping("/education/{caseEducationId}/vendor-response")
			 @PreAuthorize(
			         "hasAnyAuthority('SUPER_ADMIN', 'ADMIN', "
			         + "'PROCESS_TEAM_MEMBER', 'PROCESS_TEAM_LEADER','QC_CASE_MEMBER')"
			 )
			 public ResponseEntity<CaseEducationVendorResponseDTO>
			         getEducationVendorResponse(
			                 @PathVariable Long caseEducationId) {
			
			     CaseEducationVendorResponseDTO response =
			             caseVendorResponseService.getEducationVendorResponse(caseEducationId);
			
			     return ResponseEntity.ok(response);
			 }
			 
 
			
			@GetMapping("/employment/{caseEmploymentId}/vendor-response")
			@PreAuthorize(
			      "hasAnyAuthority('SUPER_ADMIN', 'ADMIN', "
			      + "'PROCESS_TEAM_MEMBER', 'PROCESS_TEAM_LEADER','QC_CASE_MEMBER')"
			)
			public ResponseEntity<CaseEmploymentVendorResponseDTO>
			      getEmploymentVendorResponse(
			              @PathVariable Long caseEmploymentId) {
			
			  CaseEmploymentVendorResponseDTO response =
			          caseVendorResponseService
			                  .getEmploymentVendorResponse(
			                          caseEmploymentId
			                  );
			
			  return ResponseEntity.ok(response);
			}
}