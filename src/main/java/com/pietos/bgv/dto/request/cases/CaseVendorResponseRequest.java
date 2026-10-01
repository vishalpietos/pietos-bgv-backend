package com.pietos.bgv.dto.request.cases;

public class CaseVendorResponseRequest {

    private String selectedField;

    private String value;

    public String getSelectedField() {
        return selectedField;
    }

    public void setSelectedField(String selectedField) {
        this.selectedField = selectedField;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}