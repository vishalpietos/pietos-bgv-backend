package com.pietos.bgv.dto.request;

public class UserComponentAccessRequest {

    private Long userId;

    private Long componentId;


    public UserComponentAccessRequest() {
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }
}