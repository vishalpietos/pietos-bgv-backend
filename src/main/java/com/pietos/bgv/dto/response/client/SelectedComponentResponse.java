package com.pietos.bgv.dto.response.client;

public class SelectedComponentResponse {

    private Long componentId;

    private String componentName;

    private Integer componentCount;


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


    public Integer getComponentCount() {
        return componentCount;
    }

    public void setComponentCount(Integer componentCount) {
        this.componentCount = componentCount;
    }
}