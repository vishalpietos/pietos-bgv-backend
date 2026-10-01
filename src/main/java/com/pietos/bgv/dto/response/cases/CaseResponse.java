package com.pietos.bgv.dto.response.cases;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.pietos.bgv.enums.CaseStatus;
import com.pietos.bgv.enums.EmploymentType;

public class CaseResponse {

    private Long id;

    private String caseRef;

    private Long clientId;

    private String clientName;

    private Long locationId;

    private String locationName;

    private LocalDate caseReceivedDate;

    private LocalDate caseInDate;

    private LocalDate caseDueDate;

    private EmploymentType employment;

    private String firstName;

    private String middleName;

    private String lastName;

    private LocalDate dateOfBirth;

    private String fatherName;

    private String clientEmployeeId;

    private LocalDate dateOfJoining;

    private String mobileNumber;

    private String email;

    private String gender;

    private CaseStatus status;
    
    private String report;

    private Long createdBy;

    private String createdByName;

    private Long updatedBy;

    private String updatedByName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public CaseResponse() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaseRef() {
        return caseRef;
    }

    public void setCaseRef(String caseRef) {
        this.caseRef = caseRef;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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

    public EmploymentType getEmployment() {
        return employment;
    }

    public void setEmployment(EmploymentType employment) {
        this.employment = employment;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    

    public CaseStatus getStatus() {
		return status;
	}


	public void setStatus(CaseStatus status) {
		this.status = status;
	}


	public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
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
}