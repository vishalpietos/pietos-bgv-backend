package com.pietos.bgv.dto.request.client;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ClientComponentItemRequest {

    @NotNull(message = "Component ID is required.")
    private Long componentId;

   

    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

}