package com.pietos.bgv.service.cases;

import java.util.List;

import com.pietos.bgv.dto.imports.cases.CaseImportError;
import com.pietos.bgv.dto.imports.cases.CaseImportRow;

public interface CaseImportValidationService {

    List<CaseImportError> validate(
            List<CaseImportRow> rows);
}