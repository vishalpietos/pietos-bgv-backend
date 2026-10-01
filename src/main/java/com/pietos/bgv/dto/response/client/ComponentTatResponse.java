package com.pietos.bgv.dto.response.client;

public class ComponentTatResponse {
	
	private Long id;

    private Long componentId;

    private String componentName;

    private Integer tatDays;


    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }


    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }


    public Integer getTatDays() {
        return tatDays;
    }

    public void setTatDays(Integer tatDays) {
        this.tatDays = tatDays;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
    
    
}