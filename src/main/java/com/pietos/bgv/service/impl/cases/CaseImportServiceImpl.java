package com.pietos.bgv.service.impl.cases;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pietos.bgv.dto.imports.cases.CaseImportError;
import com.pietos.bgv.dto.imports.cases.CaseImportResult;
import com.pietos.bgv.dto.imports.cases.CaseImportRow;
import com.pietos.bgv.service.cases.CaseExcelReaderService;
import com.pietos.bgv.service.cases.CaseImportService;
import com.pietos.bgv.service.cases.CaseImportValidationService;

@Service
public class CaseImportServiceImpl
        implements CaseImportService {

    private final CaseExcelReaderService
            caseExcelReaderService;

    private final CaseImportValidationService
            caseImportValidationService;


    public CaseImportServiceImpl(
            CaseExcelReaderService caseExcelReaderService,
            CaseImportValidationService caseImportValidationService) {

        this.caseExcelReaderService =
                caseExcelReaderService;

        this.caseImportValidationService =
                caseImportValidationService;
    }


    @Override
    public CaseImportResult validateExcel(
            MultipartFile file) {

        try {

            if (file == null || file.isEmpty()) {

                throw new IllegalArgumentException(
                        "Excel file is empty."
                );
            }

            String fileName =
                    file.getOriginalFilename();

            if (fileName == null
                    || !fileName
                    .toLowerCase()
                    .endsWith(".xlsx")) {

                throw new IllegalArgumentException(
                        "Only .xlsx Excel files are allowed."
                );
            }


            List<CaseImportRow> rows =
                    caseExcelReaderService
                            .readExcel(file);


            List<CaseImportError> errors =
                    caseImportValidationService
                            .validate(rows);


            CaseImportResult result =
                    new CaseImportResult();

            result.setTotalRows(rows.size());

            result.setInvalidRows(
                    errors.stream()
                            .map(CaseImportError::getRowNumber)
                            .distinct()
                            .toList()
                            .size()
            );

            result.setValidRows(
                    result.getTotalRows()
                            - result.getInvalidRows()
            );

            result.setErrors(errors);

            return result;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to process Excel file: "
                            + e.getMessage(),
                    e
            );
        }
    }
}