package com.pietos.bgv.dto.request.cases;

public class CaseCallLogRequest {

    private Long casePackageComponentId;

    private String subCaseId;

    private Boolean considerAsAttempt;

    private String activity;


    public Long getCasePackageComponentId() {
        return casePackageComponentId;
    }

    public void setCasePackageComponentId(Long casePackageComponentId) {
        this.casePackageComponentId = casePackageComponentId;
    }

    public String getSubCaseId() {
        return subCaseId;
    }

    public void setSubCaseId(String subCaseId) {
        this.subCaseId = subCaseId;
    }

    public Boolean getConsiderAsAttempt() {
        return considerAsAttempt;
    }

    public void setConsiderAsAttempt(Boolean considerAsAttempt) {
        this.considerAsAttempt = considerAsAttempt;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}