package com.pietos.bgv.dto.request.client;

public class ComponentTatDetailRequest {

    private Long componentId;

    private Integer tatDays;


    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    public Integer getTatDays() {
        return tatDays;
    }

    public void setTatDays(Integer tatDays) {
        this.tatDays = tatDays;
    }
}