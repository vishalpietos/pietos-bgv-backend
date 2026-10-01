package com.pietos.bgv.controller.cases;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.pietos.bgv.dto.imports.cases.CaseImportResult;
import com.pietos.bgv.service.cases.CaseImportService;

@RestController
@RequestMapping("/cases/import")
public class CaseImportController {

    private final CaseImportService caseImportService;


    public CaseImportController(
            CaseImportService caseImportService) {

        this.caseImportService =
                caseImportService;
    }


    @PostMapping("/validate")
    public ResponseEntity<CaseImportResult> validateExcel(
            @RequestParam("file") MultipartFile file) {

        return ResponseEntity.ok(
                caseImportService.validateExcel(file)
        );
    }
}