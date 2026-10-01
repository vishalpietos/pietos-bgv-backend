package com.pietos.bgv.dto.response.cases;

import java.util.List;

public class CasePackageResponse {

    private Long id;

    private Long caseId;

    private String caseRef;

    private Long packageId;

    private String packageName;

    private List<CasePackageComponentResponse> components;


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

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<CasePackageComponentResponse> getComponents() {
        return components;
    }

    public void setComponents(
            List<CasePackageComponentResponse> components) {

        this.components = components;
    }
}