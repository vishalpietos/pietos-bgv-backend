package com.pietos.bgv.dto.request.cases;

import java.math.BigDecimal;

public class CaseEducationRequest {

    private Long caseId;
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
	
	
	
    
}