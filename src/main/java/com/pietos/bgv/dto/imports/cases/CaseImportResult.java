package com.pietos.bgv.dto.imports.cases;

import java.util.ArrayList;
import java.util.List;

public class CaseImportResult {

    private int totalRows;

    private int validRows;

    private int invalidRows;

    private List<CaseImportError> errors = new ArrayList<>();


    public CaseImportResult() {
    }


    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }


    public int getValidRows() {
        return validRows;
    }

    public void setValidRows(int validRows) {
        this.validRows = validRows;
    }


    public int getInvalidRows() {
        return invalidRows;
    }

    public void setInvalidRows(int invalidRows) {
        this.invalidRows = invalidRows;
    }


    public List<CaseImportError> getErrors() {
        return errors;
    }

    public void setErrors(List<CaseImportError> errors) {
        this.errors = errors;
    }
}