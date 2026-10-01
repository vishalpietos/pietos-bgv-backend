package com.pietos.bgv.dto.response.client;

import java.time.LocalDate;
import java.util.List;

public class ClientComponentTatResponse {

    private Long clientId;

    private String clientName;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    private List<ComponentTatResponse> components;


    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }


    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }


    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }


    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }


    public List<ComponentTatResponse> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentTatResponse> components) {
        this.components = components;
    }
}