package com.pietos.bgv.dto.response.cases;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class QCComponentLevelResponse {

    private Long checkId;
    private Long caseId;

    private String caseRef;
    private String subRefNo;

    private String clientNameLocation;
    private String candidateName;
    private String fatherName;
    private String clientEmployeeId;

    private LocalDate caseReceivedDate;
    private LocalDate caseInDate;
    private LocalDate caseDueDate;
    private LocalDate componentDueDate;

    private String componentName;
    private String status;
    private String dispositionStatus;

   

    public Long getCheckId() {
		return checkId;
	}

	public void setCheckId(Long checkId) {
		this.checkId = checkId;
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

    public String getSubRefNo() {
        return subRefNo;
    }

    public void setSubRefNo(String subRefNo) {
        this.subRefNo = subRefNo;
    }

    public String getClientNameLocation() {
        return clientNameLocation;
    }

    public void setClientNameLocation(String clientNameLocation) {
        this.clientNameLocation = clientNameLocation;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getClientEmployeeId() {
        return clientEmployeeId;
    }

    public void setClientEmployeeId(String clientEmployeeId) {
        this.clientEmployeeId = clientEmployeeId;
    }

    
   
	public LocalDate getCaseReceivedDate() {
		return caseReceivedDate;
	}

	public void setCaseReceivedDate(LocalDate caseReceivedDate) {
		this.caseReceivedDate = caseReceivedDate;
	}

	
	public LocalDate getCaseInDate() {
		return caseInDate;
	}

	public void setCaseInDate(LocalDate caseInDate) {
		this.caseInDate = caseInDate;
	}

	public LocalDate getCaseDueDate() {
		return caseDueDate;
	}

	public void setCaseDueDate(LocalDate caseDueDate) {
		this.caseDueDate = caseDueDate;
	}

	public LocalDate getComponentDueDate() {
		return componentDueDate;
	}

	public void setComponentDueDate(LocalDate componentDueDate) {
		this.componentDueDate = componentDueDate;
	}

	public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public String getDispositionStatus() {
		return dispositionStatus;
	}

	public void setDispositionStatus(String dispositionStatus) {
		this.dispositionStatus = dispositionStatus;
	}
    
    
}