package com.pietos.bgv.dto.response.cases;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.pietos.bgv.enums.EmploymentType;

public class CaseEmploymentVendorResponseDTO {

    private Long id;

    private Long caseEmploymentId;

    private String companyName;

    private String location;

    private String employmentId;

    private String designation;

    private LocalDate employmentStartDate;

    private LocalDate employmentEndDate;

    private BigDecimal salary;

    private EmploymentType employmentType;

    private String reasonForLeaving;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCaseEmploymentId() {
        return caseEmploymentId;
    }

    public void setCaseEmploymentId(Long caseEmploymentId) {
        this.caseEmploymentId = caseEmploymentId;
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
}