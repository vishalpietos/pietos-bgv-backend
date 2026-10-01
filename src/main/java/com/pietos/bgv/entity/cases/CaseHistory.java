package com.pietos.bgv.entity.cases;

import java.time.LocalDateTime;

import com.pietos.bgv.entity.SystemUser;

import jakarta.persistence.*;

@Entity
@Table(name = "case_history")
public class CaseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // Overall Case
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "case_id",
        nullable = false
    )
    private Case caseEntity;

    // Particular check instance
    // NULL for case-level history
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "case_package_component_id",
        nullable = true
    )
    private CasePackageComponent casePackageComponent;


    // Actual verification record ID
    // Example: PT/ED/0001
    // NULL for case-level history
    @Column(
        name = "sub_case_id",
        length = 100
    )
    private String subCaseId;


    @Column(
        name = "status",
        nullable = false,
        length = 100
    )
    private String status;


    @Column(
        name = "activity",
        columnDefinition = "TEXT"
    )
    private String activity;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "performed_by",
        nullable = false
    )
    private SystemUser performedBy;


    @Column(
        name = "created_at",
        nullable = false
    )
    private LocalDateTime createdAt;


    public CaseHistory() {
    }


    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Case getCaseEntity() {
        return caseEntity;
    }

    public void setCaseEntity(Case caseEntity) {
        this.caseEntity = caseEntity;
    }


    public CasePackageComponent getCasePackageComponent() {
        return casePackageComponent;
    }

    public void setCasePackageComponent(CasePackageComponent casePackageComponent) {
        this.casePackageComponent = casePackageComponent;
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


    public SystemUser getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(SystemUser performedBy) {
        this.performedBy = performedBy;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}