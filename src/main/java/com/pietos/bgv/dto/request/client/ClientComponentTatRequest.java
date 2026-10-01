package com.pietos.bgv.dto.request.client;

import java.time.LocalDate;
import java.util.List;

public class ClientComponentTatRequest {

    private Long clientId;

    private Long packageId;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    private List<ComponentTatDetailRequest> components;


    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
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

    public List<ComponentTatDetailRequest> getComponents() {
        return components;
    }

    public void setComponents(
            List<ComponentTatDetailRequest> components) {
        this.components = components;
    }
}