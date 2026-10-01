package com.pietos.bgv.entity.cases;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.enums.ComponentSubStatus;
import com.pietos.bgv.enums.DataEntryStatus;
import com.pietos.bgv.enums.DispositionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "case_education_table")
public class CaseEducationTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "sub_case_id", unique = true, length = 50)
    private String subCaseId;

    @Column(name = "case_id", nullable = false)
    private Long caseId;
    
    @Column(name = "case_ref", nullable = false)
    private String caseRef;

    @Column(name = "case_package_component_id", nullable = false)
    private Long casePackageComponentId;

    @Column(name = "university")
    private String university;

    @Column(name = "institution")
    private String institution;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "international")
    private Boolean international;

    @Column(name = "international_amount", precision = 15, scale = 2)
    private BigDecimal internationalAmount;

    @Column(name = "additional_fees_required")
    private Boolean additionalFeesRequired;

    @Column(name = "additional_fees_required_amount", precision = 15, scale = 2)
    private BigDecimal additionalFeesRequiredAmount;

    @Column(name = "qualification")
    private String qualification;

    @Column(name = "year_of_passing")
    private String yearOfPassing;
    
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_entry_status")
    private DataEntryStatus status;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "processing_status")
    private ComponentSubStatus processingStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "disposition_status", length = 50)
    private DispositionStatus  dispositionStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "qc_disposition_status")
    private DispositionStatus qcDispositionStatus;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private SystemUser assignedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by")
    private SystemUser assignedBy;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    // =====================================================
    // REJECTION
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rejected_by")
    private SystemUser rejectedBy;
    
    @Column(name = "rejection_comment", columnDefinition = "TEXT")
    private String rejectionComment;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private SystemUser createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private SystemUser updatedBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @Column(name = "component_due_date")
    private LocalDate componentDueDate;

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

	public SystemUser getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(SystemUser createdBy) {
		this.createdBy = createdBy;
	}

	public SystemUser getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(SystemUser updatedBy) {
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

	public String getCaseRef() {
		return caseRef;
	}

	public void setCaseRef(String caseRef) {
		this.caseRef = caseRef;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public DataEntryStatus getStatus() {
		return status;
	}

	public void setStatus(DataEntryStatus status) {
		this.status = status;
	}

	public String getSubCaseId() {
		return subCaseId;
	}

	public void setSubCaseId(String subCaseId) {
		this.subCaseId = subCaseId;
	}

	public LocalDate getComponentDueDate() {
		return componentDueDate;
	}

	public void setComponentDueDate(LocalDate componentDueDate) {
		this.componentDueDate = componentDueDate;
	}

	public SystemUser getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(SystemUser assignedTo) {
		this.assignedTo = assignedTo;
	}

	public SystemUser getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(SystemUser assignedBy) {
		this.assignedBy = assignedBy;
	}

	public LocalDateTime getAssignedAt() {
		return assignedAt;
	}

	public void setAssignedAt(LocalDateTime assignedAt) {
		this.assignedAt = assignedAt;
	}

	public SystemUser getRejectedBy() {
		return rejectedBy;
	}

	public void setRejectedBy(SystemUser rejectedBy) {
		this.rejectedBy = rejectedBy;
	}

	public LocalDateTime getRejectedAt() {
		return rejectedAt;
	}

	public void setRejectedAt(LocalDateTime rejectedAt) {
		this.rejectedAt = rejectedAt;
	}


	public DispositionStatus getDispositionStatus() {
		return dispositionStatus;
	}

	public void setDispositionStatus(DispositionStatus dispositionStatus) {
		this.dispositionStatus = dispositionStatus;
	}

	public ComponentSubStatus getProcessingStatus() {
		return processingStatus;
	}

	public void setProcessingStatus(ComponentSubStatus processingStatus) {
		this.processingStatus = processingStatus;
	}

	public String getRejectionComment() {
		return rejectionComment;
	}

	public void setRejectionComment(String rejectionComment) {
		this.rejectionComment = rejectionComment;
	}

	public DispositionStatus getQcDispositionStatus() {
		return qcDispositionStatus;
	}

	public void setQcDispositionStatus(DispositionStatus qcDispositionStatus) {
		this.qcDispositionStatus = qcDispositionStatus;
	}  
	
	
    
}