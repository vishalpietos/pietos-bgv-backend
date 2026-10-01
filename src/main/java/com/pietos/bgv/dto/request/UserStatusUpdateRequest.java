package com.pietos.bgv.dto.request;

public class UserStatusUpdateRequest {

    private Boolean isActive;

    public UserStatusUpdateRequest() {
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}