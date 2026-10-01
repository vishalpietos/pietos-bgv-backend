package com.pietos.bgv.dto.request.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SubComponentRequest {

    @NotNull(message = "Component is required.")
    private Long componentId;

    @NotBlank(message = "Sub Component is required.")
    private String subComponentName;

	public Long getComponentId() {
		return componentId;
	}

	public void setComponentId(Long componentId) {
		this.componentId = componentId;
	}

	public String getSubComponentName() {
		return subComponentName;
	}

	public void setSubComponentName(String subComponentName) {
		this.subComponentName = subComponentName;
	}

    
}