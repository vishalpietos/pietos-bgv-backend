package com.pietos.bgv.dto.response.cases;

import java.time.LocalDate;

public class CaseEducationOpenCaseResponse {

    private Long id;

    private Long caseId;

    private String caseRef;

    private String subRefNo;

    private Long casePackageComponentId;

    private String location;

    private String universityName;

    private String collegeName;

    private String yearOfPassing;

    private String degree;

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

    


    public CaseEducationOpenCaseResponse() {
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


    public String getSubRefNo() {
        return subRefNo;
    }

    public void setSubRefNo(String subRefNo) {
        this.subRefNo = subRefNo;
    }


    public Long getCasePackageComponentId() {
        return casePackageComponentId;
    }

    public void setCasePackageComponentId(
            Long casePackageComponentId) {
        this.casePackageComponentId =
                casePackageComponentId;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }


    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }


    public String getYearOfPassing() {
        return yearOfPassing;
    }

    public void setYearOfPassing(String yearOfPassing) {
        this.yearOfPassing = yearOfPassing;
    }


    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }


    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }


    public LocalDate getCaseReceivedDate() {
        return caseReceivedDate;
    }

    public void setCaseReceivedDate(
            LocalDate caseReceivedDate) {
        this.caseReceivedDate =
                caseReceivedDate;
    }


    public LocalDate getCaseInDate() {
        return caseInDate;
    }

    public void setCaseInDate(
            LocalDate caseInDate) {
        this.caseInDate = caseInDate;
    }


    public LocalDate getCaseDueDate() {
        return caseDueDate;
    }

    public void setCaseDueDate(
            LocalDate caseDueDate) {
        this.caseDueDate = caseDueDate;
    }


    public LocalDate getComponentDueDate() {
        return componentDueDate;
    }

    public void setComponentDueDate(
            LocalDate componentDueDate) {
        this.componentDueDate =
                componentDueDate;
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

    public void setClientEmployeeId(
            String clientEmployeeId) {
        this.clientEmployeeId =
                clientEmployeeId;
    }


	public String getClientLocation() {
		return clientLocation;
	}


	public void setClientLocation(String clientLocation) {
		this.clientLocation = clientLocation;
	}
    
    
}