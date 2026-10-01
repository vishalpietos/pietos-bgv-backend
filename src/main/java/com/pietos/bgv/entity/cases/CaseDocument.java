package com.pietos.bgv.entity.cases;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "case_documents")
public class CaseDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Column(name = "case_ref", nullable = false, length = 255)
    private String caseRef;

    @Column(name = "case_package_components_id", nullable = false)
    private Long casePackageComponentsId;

    @Column(
        name = "file_name",
        nullable = false,
        length = 255
    )
    private String fileName;

    @Column(
        name = "description",
        length = 500
    )
    private String description;

    @Column(
        name = "file_path",
        nullable = false,
        length = 500
    )
    private String filePath;

    @Column(name = "uploaded_by")
    private Long uploadedBy;

    @Column(
        name = "uploaded_at",
        nullable = false
    )
    private LocalDateTime uploadedAt;

    @Column(
        name = "is_active",
        nullable = false
    )
    private Boolean isActive = true;

    @Column(name = "deactivated_by")
    private Long deactivatedBy;

    @Column(name = "deactivated_at")
    private LocalDateTime deactivatedAt;

    public CaseDocument() {
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(Long uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getDeactivatedBy() {
        return deactivatedBy;
    }

    public void setDeactivatedBy(Long deactivatedBy) {
        this.deactivatedBy = deactivatedBy;
    }

    public LocalDateTime getDeactivatedAt() {
        return deactivatedAt;
    }

    public void setDeactivatedAt(LocalDateTime deactivatedAt) {
        this.deactivatedAt = deactivatedAt;
    }
}