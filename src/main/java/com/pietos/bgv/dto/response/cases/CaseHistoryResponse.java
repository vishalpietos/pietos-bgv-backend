package com.pietos.bgv.dto.response.cases;

import java.time.LocalDateTime;

public class CaseHistoryResponse {

    private Long id;

    private Long caseId;

    private Long casePackageComponentId;

    private String subCaseId;

    private String status;

    private String activity;

    private Long performedBy;

    private String performedByName;

    private LocalDateTime createdAt;


    public CaseHistoryResponse() {

    }


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


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }


    public Long getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(Long performedBy) {
        this.performedBy = performedBy;
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