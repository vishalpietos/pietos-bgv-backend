package com.pietos.bgv.dto.response.cases;

import java.time.LocalDate;

public class CaseEmploymentOpenCaseResponse {
	
	 private Long id;

	    private Long caseId;

	    private String caseRef;

	    private String subRefNo;
	    
	    private String location;

	    private Long casePackageComponentId;
	    
	    private String clientName;
	    
	    private String clientLocation;

	    private LocalDate caseReceivedDate;

	    private LocalDate caseInDate;

	    private LocalDate caseDueDate;

	    private LocalDate componentDueDate;
	    
	    private String candidateName;

	    private String fatherName;

	    private LocalDate dob;

	    private String clientEmployeeId;

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

		public String getSubRefNo() {
			return subRefNo;
		}

		public void setSubRefNo(String subRefNo) {
			this.subRefNo = subRefNo;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

		public Long getCasePackageComponentId() {
			return casePackageComponentId;
		}

		public void setCasePackageComponentId(Long casePackageComponentId) {
			this.casePackageComponentId = casePackageComponentId;
		}

		public String getClientName() {
			return clientName;
		}

		public void setClientName(String clientName) {
			this.clientName = clientName;
		}

		public String getClientLocation() {
			return clientLocation;
		}

		public void setClientLocation(String clientLocation) {
			this.clientLocation = clientLocation;
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

		public LocalDate getDob() {
			return dob;
		}

		public void setDob(LocalDate dob) {
			this.dob = dob;
		}

		public String getClientEmployeeId() {
			return clientEmployeeId;
		}

		public void setClientEmployeeId(String clientEmployeeId) {
			this.clientEmployeeId = clientEmployeeId;
		}
	    
	    

}
