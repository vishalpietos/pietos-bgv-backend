package com.pietos.bgv.dto.response.cases;

import java.time.LocalDateTime;

public class CaseCallLogResponse {

    private Long id;

    private Long caseId;

    private Long casePackageComponentId;

    private String subCaseId;

    private String componentName;

    private Boolean considerAsAttempt;

    private String activity;

    private String performedByName;

    private LocalDateTime createdAt;


    // =====================================================
    // GETTERS AND SETTERS
    // =====================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public Long getCasePackageComponentId() {
        return casePackageComponentId;
    }

    public void setCasePackageComponentId(Long casePackageComponentId) {
        this.casePackageComponentId = casePackageComponentId;
    }

    public String getSubCaseId() {
        return subCaseId;
    }

    public void setSubCaseId(String subCaseId) {
        this.subCaseId = subCaseId;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public Boolean getConsiderAsAttempt() {
        return considerAsAttempt;
    }

    public void setConsiderAsAttempt(Boolean considerAsAttempt) {
        this.considerAsAttempt = considerAsAttempt;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getPerformedByName() {
        return performedByName;
    }

    public void setPerformedByName(String performedByName) {
        this.performedByName = performedByName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}