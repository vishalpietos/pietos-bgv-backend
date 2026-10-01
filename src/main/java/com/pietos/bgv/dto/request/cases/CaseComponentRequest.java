package com.pietos.bgv.dto.request.cases;

public class CaseComponentRequest {

    private Long componentId;

    private String report;

    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}