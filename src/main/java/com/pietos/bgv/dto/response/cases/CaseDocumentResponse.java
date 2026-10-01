package com.pietos.bgv.dto.response.cases;

import java.time.LocalDateTime;

public class CaseDocumentResponse {

    private Long id;

    private Long caseId;

    private String caseRef;

    private Long casePackageComponentsId;

    private String fileName;

    private String description;

    private Long uploadedByUserId;

    private String uploadedByUserName;

    private LocalDateTime uploadedAt;

    public CaseDocumentResponse() {
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

    public String getCaseRef() {
        return caseRef;
    }

    public void setCaseRef(String caseRef) {
        this.caseRef = caseRef;
    }

    public Long getCasePackageComponentsId() {
        return casePackageComponentsId;
    }

    public void setCasePackageComponentsId(Long casePackageComponentsId) {
        this.casePackageComponentsId = casePackageComponentsId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUploadedByUserId() {
        return uploadedByUserId;
    }

    public void setUploadedByUserId(Long uploadedByUserId) {
        this.uploadedByUserId = uploadedByUserId;
    }

    public String getUploadedByUserName() {
        return uploadedByUserName;
    }

    public void setUploadedByUserName(String uploadedByUserName) {
        this.uploadedByUserName = uploadedByUserName;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}