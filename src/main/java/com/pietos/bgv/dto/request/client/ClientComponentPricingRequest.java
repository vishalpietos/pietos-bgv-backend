package com.pietos.bgv.dto.request.client;

import java.time.LocalDate;
import java.util.List;

public class ClientComponentPricingRequest {
	
	private Long clientPackageId;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    private List<ClientComponentPricingItemRequest> components;

    
	public Long getClientPackageId() {
		return clientPackageId;
	}

	public void setClientPackageId(Long clientPackageId) {
		this.clientPackageId = clientPackageId;
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

    public List<ClientComponentPricingItemRequest> getComponents() {
        return components;
    }

    public void setComponents(List<ClientComponentPricingItemRequest> components) {
        this.components = components;
    }
}