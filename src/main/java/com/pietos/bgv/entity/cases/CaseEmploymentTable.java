package com.pietos.bgv.entity.cases;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.enums.ComponentSubStatus;
import com.pietos.bgv.enums.DataEntryStatus;
import com.pietos.bgv.enums.DispositionStatus;
import com.pietos.bgv.enums.EmploymentType;

import jakarta.persistence.*;

@Entity
@Table(
    name = "case_employment_table",
    indexes = {
        @Index(
            name = "idx_case_employment_case_id",
            columnList = "case_id"
        ),
        @Index(
            name = "idx_case_employment_component_id",
            columnList = "case_package_component_id"
        ),
        @Index(
            name = "idx_case_employment_status",
            columnList = "status"
        )
    }
)
public class CaseEmploymentTable {

    // =====================================================
    // PRIMARY KEY
    // =====================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // CASE INFORMATION
    // =====================================================

    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Column(
        name = "sub_case_id",
        unique = true,
        length = 50
    )
    private String subCaseId;

    @Column(name = "case_ref", length = 100)
    private String caseRef;

    @Column(
        name = "case_package_component_id",
        nullable = false
    )
    private Long casePackageComponentId;

    // =====================================================
    // EMPLOYMENT DETAILS
    // =====================================================

    @Column(name = "company_name", columnDefinition = "TEXT")
    private String companyName;

    @Column(name = "location", columnDefinition = "TEXT")
    private String location;

    @Column(name = "employment_id", columnDefinition = "TEXT")
    private String employmentId;

    @Column(name = "designation", columnDefinition = "TEXT")
    private String designation;

    @Column(name = "employment_start_date")
    private LocalDate employmentStartDate;

    @Column(name = "employment_end_date")
    private LocalDate employmentEndDate;

    @Column(
        name = "salary",
        precision = 15,
        scale = 2
    )
    private BigDecimal salary;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", length = 20)
    private EmploymentType employmentType;

    @Column(
        name = "reason_for_leaving",
        columnDefinition = "TEXT"
    )
    private String reasonForLeaving;

    // =====================================================
    // STATUS / REMARKS
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(name = "data_entry_status", length = 50)
    private DataEntryStatus status;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "disposition_status", length = 50)
    private DispositionStatus  dispositionStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "qc_disposition_status")
    private DispositionStatus qcDispositionStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "processing_status")
    private ComponentSubStatus processingStatus;
    

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    @Column(name = "international")
    private Boolean international;

    @Column(
        name = "international_amount",
        precision = 15,
        scale = 2
    )
    private BigDecimal internationalAmount;

    @Column(name = "additional_fees_required")
    private Boolean additionalFeesRequired;

    @Column(
        name = "additional_fees_required_amount",
        precision = 15,
        scale = 2
    )
    private BigDecimal additionalFeesRequiredAmount;

    @Column(name = "component_due_date")
    private LocalDate componentDueDate;

    // =====================================================
    // AUDIT
    // =====================================================

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
    
 // =====================================================
 // ASSIGNMENT
 // =====================================================

    	@ManyToOne(fetch = FetchType.LAZY)
    	@JoinColumn(name = "assigned_to")
    	private SystemUser assignedTo;

    	@ManyToOne(fetch = FetchType.LAZY)
    	@JoinColumn(name = "assigned_by")
    	private SystemUser assignedBy;

    	@Column(name = "assigned_at")
    	private LocalDateTime assignedAt;

    	@ManyToOne(fetch = FetchType.LAZY)
    	@JoinColumn(name = "rejected_by")
    	private SystemUser rejectedBy;
    	
    	@Column(name = "rejection_comment", columnDefinition = "TEXT")
    	private String rejectionComment;

    	@Column(name = "rejected_at")
    	private LocalDateTime rejectedAt;

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

	public void setAdditionalFeesRequiredAmount(BigDecimal additionalFeesRequiredAmount) {
		this.additionalFeesRequiredAmount = additionalFeesRequiredAmount;
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

	public ComponentSubStatus getProcessingStatus() {
		return processingStatus;
	}

	public void setProcessingStatus(ComponentSubStatus processingStatus) {
		this.processingStatus = processingStatus;
	}

	public DispositionStatus getDispositionStatus() {
		return dispositionStatus;
	}

	public void setDispositionStatus(DispositionStatus dispositionStatus) {
		this.dispositionStatus = dispositionStatus;
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