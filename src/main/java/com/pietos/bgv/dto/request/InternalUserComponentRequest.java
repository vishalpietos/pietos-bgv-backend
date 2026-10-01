package com.pietos.bgv.dto.request;

import java.util.List;

public class InternalUserComponentRequest {

    private List<Long> componentIds;

    public InternalUserComponentRequest() {
    }

    public InternalUserComponentRequest(List<Long> componentIds) {
        this.componentIds = componentIds;
    }

    public List<Long> getComponentIds() {
        return componentIds;
    }

    public void setComponentIds(List<Long> componentIds) {
        this.componentIds = componentIds;
    }
}