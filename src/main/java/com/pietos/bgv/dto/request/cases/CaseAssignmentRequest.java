package com.pietos.bgv.dto.request.cases;

import java.util.List;

public class CaseAssignmentRequest {

    private Long componentId;

    private List<Long> verificationIds;

    private Long assignedTo;

    public CaseAssignmentRequest() {
    }

    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    public List<Long> getVerificationIds() {
        return verificationIds;
    }

    public void setVerificationIds(List<Long> verificationIds) {
        this.verificationIds = verificationIds;
    }

    public Long getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Long assignedTo) {
        this.assignedTo = assignedTo;
    }
}