package com.pietos.bgv.controller.cases;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pietos.bgv.dto.request.cases.CaseCallLogRequest;
import com.pietos.bgv.dto.response.cases.CaseCallLogResponse;
import com.pietos.bgv.service.cases.CaseCallLogService;

@RestController
@RequestMapping("/api/cases/{caseId}/call-logs")
public class CaseCallLogController {

    private final CaseCallLogService caseCallLogService;

    public CaseCallLogController(
            CaseCallLogService caseCallLogService) {

        this.caseCallLogService = caseCallLogService;
    }



    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'PROCESS_TEAM_MEMBER', 'PROCESS_TEAM_LEADER','QC_CASE_MEMBER')")
    public ResponseEntity<CaseCallLogResponse> createCallLog(
            @PathVariable Long caseId,
            @RequestBody CaseCallLogRequest request) {

        CaseCallLogResponse response =
                caseCallLogService.createCallLog(
                        caseId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'PROCESS_TEAM_MEMBER', 'PROCESS_TEAM_LEADER','QC_CASE_MEMBER')")
    public ResponseEntity<List<CaseCallLogResponse>> getCallLogsByCaseId(
            @PathVariable Long caseId) {

        List<CaseCallLogResponse> response =
                caseCallLogService.getCallLogsByCaseId(
                        caseId
                );

        return ResponseEntity.ok(response);
    }
}