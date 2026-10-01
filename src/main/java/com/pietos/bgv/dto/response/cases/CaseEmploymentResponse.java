package com.pietos.bgv.dto.response.cases;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.pietos.bgv.enums.DataEntryStatus;
import com.pietos.bgv.enums.EmploymentType;

public class CaseEmploymentResponse {

    private Long id;

    private Long caseId;

    private String subCaseId;

    private String caseRef;

    private Long casePackageComponentId;

    private Long assignedTo;

    private String companyName;

    private String location;

    private String employmentId;

    private String designation;

    private LocalDate employmentStartDate;

    private LocalDate employmentEndDate;

    private BigDecimal salary;

    private EmploymentType employmentType;

    private String reasonForLeaving;

    private Boolean international;

    private BigDecimal internationalAmount;

    private Boolean additionalFeesRequired;

    private BigDecimal additionalFeesRequiredAmount;

    private DataEntryStatus status;
    
    private String processingStatus;

   
    private String remarks;

    private Long createdBy;

    private Long updatedBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    // =====================================================
    // GETTERS / SETTERS
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

    public String getSubCaseId() {
        return subCaseId;
    }

    public void setSubCaseId(String subCaseId) {
        this.subCaseId = subCaseId;
    }

    public String getCaseRef() {
        return caseRef;
    }

    public void setCaseRef(String caseRef) {
        this.caseRef = caseRef;
    }

    public Long getCasePackageComponentId() {
        return casePackageComponentId;
    }

    public void setCasePackageComponentId(Long casePackageComponentId) {
        this.casePackageComponentId = casePackageComponentId;
    }

    public Long getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Long assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmploymentId() {
        return employmentId;
    }

    public void setEmploymentId(String employmentId) {
        this.employmentId = employmentId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public LocalDate getEmploymentStartDate() {
        return employmentStartDate;
    }

    public void setEmploymentStartDate(LocalDate employmentStartDate) {
        this.employmentStartDate = employmentStartDate;
    }

    public LocalDate getEmploymentEndDate() {
        return employmentEndDate;
    }

    public void setEmploymentEndDate(LocalDate employmentEndDate) {
        this.employmentEndDate = employmentEndDate;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public String getReasonForLeaving() {
        return reasonForLeaving;
    }

    public void setReasonForLeaving(String reasonForLeaving) {
        this.reasonForLeaving = reasonForLeaving;
    }

    public Boolean getInternational() {
        return international;
    }

    public void setInternational(Boolean international) {
        this.international = international;
    }

    public BigDecimal getInternationalAmount() {
        return internationalAmount;
    }

    public void setInternationalAmount(BigDecimal internationalAmount) {
        this.internationalAmount = internationalAmount;
    }

    public Boolean getAdditionalFeesRequired() {
        return additionalFeesRequired;
    }

    public void setAdditionalFeesRequired(Boolean additionalFeesRequired) {
        this.additionalFeesRequired = additionalFeesRequired;
    }

    public BigDecimal getAdditionalFeesRequiredAmount() {
        return additionalFeesRequiredAmount;
    }

    public void setAdditionalFeesRequiredAmount(
            BigDecimal additionalFeesRequiredAmount) {
        this.additionalFeesRequiredAmount =
                additionalFeesRequiredAmount;
    }

    public DataEntryStatus getStatus() {
        return status;
    }

    public void setStatus(DataEntryStatus status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

	public String getProcessingStatus() {
		return processingStatus;
	}

	public void setProcessingStatus(String processingStatus) {
		this.processingStatus = processingStatus;
	}
    
    
    
    
}