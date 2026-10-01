package com.pietos.bgv.service.cases;

import org.springframework.web.multipart.MultipartFile;

import com.pietos.bgv.dto.imports.cases.CaseImportResult;

public interface CaseImportService {

    CaseImportResult validateExcel(
            MultipartFile file);

}