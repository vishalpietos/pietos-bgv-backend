package com.pietos.bgv.entity.cases;

import java.time.LocalDateTime;

import com.pietos.bgv.entity.SystemUser;

import jakarta.persistence.*;

@Entity
@Table(name = "case_call_logs")
public class CaseCallLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Column(name = "case_package_component_id")
    private Long casePackageComponentId;

    @Column(name = "sub_case_id", length = 100)
    private String subCaseId;

    @Column(name = "consider_as_attempt", nullable = false)
    private Boolean considerAsAttempt = false;

    @Column(name = "activity", nullable = false, columnDefinition = "TEXT")
    private String activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by", nullable = false)
    private SystemUser performedBy;

    @Column(name = "created_at", nullable = false)
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