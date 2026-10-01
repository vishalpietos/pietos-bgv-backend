package com.pietos.bgv.dto.response.cases;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CaseEducationResponse {

    private Long id;

    private Long caseId;
    private String caseRef;
    private String SubRefNo;
    private Long casePackageComponentId;
    private Long assignedTo;

    private String university;
    private String institution;
    private String registrationNumber;

    private Boolean international;
    private BigDecimal internationalAmount;

    private Boolean additionalFeesRequired;
    private BigDecimal additionalFeesRequiredAmount;

    private String qualification;
    private String yearOfPassing;
    
    private String remarks;
    private String status;

    private Long createdBy;
    private Long updatedBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
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
        this.additionalFeesRequiredAmount = additionalFeesRequiredAmount;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

   

    public String getYearOfPassing() {
		return yearOfPassing;
	}

	public void setYearOfPassing(String yearOfPassing) {
		this.yearOfPassing = yearOfPassing;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSubRefNo() {
		return SubRefNo;
	}

	public void setSubRefNo(String subRefNo) {
		SubRefNo = subRefNo;
	}
    
	
	
}