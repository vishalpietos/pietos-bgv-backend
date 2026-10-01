package com.pietos.bgv.controller.cases;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pietos.bgv.dto.response.cases.QCComponentLevelResponse;
import com.pietos.bgv.service.cases.QCComponentLevelService;

@RestController
@RequestMapping("/api/qc")
public class QCComponentLevelController {

    private final QCComponentLevelService qcComponentLevelService;

    public QCComponentLevelController(
            QCComponentLevelService qcComponentLevelService) {

        this.qcComponentLevelService =
                qcComponentLevelService;
    }

    @GetMapping("/component-level/cases")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'QC_COMPONENT_MEMBER','QC_CASE_MEMBER')")
    public ResponseEntity<List<QCComponentLevelResponse>>
            getComponentLevelCases() {

        List<QCComponentLevelResponse> response =
                qcComponentLevelService.getComponentLevelCases();

        return ResponseEntity.ok(response);
    }
}