 package com.pietos.bgv.dto.request.cases;

import java.time.LocalDate;

import com.pietos.bgv.enums.EmploymentType;

public class CaseRequest {

    private Long clientId;

    private Long locationId;

    private LocalDate caseReceivedDate;

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


    public CaseRequest() {
    }


    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public LocalDate getCaseReceivedDate() {
        return caseReceivedDate;
    }

    public void setCaseReceivedDate(LocalDate caseReceivedDate) {
        this.caseReceivedDate = caseReceivedDate;
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
}