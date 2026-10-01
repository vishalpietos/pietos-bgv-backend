package com.pietos.bgv.dto.response.cases;

public class CaseEducationVendorResponseDTO {

    private Long id;

    private Long caseEducationId;

    private String university;

    private String institution;

    private String registrationNumber;

    private String qualification;

    private String yearOfPassing;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCaseEducationId() {
        return caseEducationId;
    }

    public void setCaseEducationId(Long caseEducationId) {
        this.caseEducationId = caseEducationId;
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
}