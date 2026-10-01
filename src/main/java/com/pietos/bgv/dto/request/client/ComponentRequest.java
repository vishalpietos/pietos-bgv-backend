package com.pietos.bgv.dto.request.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ComponentRequest {

    @NotBlank(message = "Component name is required.")
    @Size(max = 150, message = "Component name cannot exceed 150 characters.")
    private String componentName;

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }
}