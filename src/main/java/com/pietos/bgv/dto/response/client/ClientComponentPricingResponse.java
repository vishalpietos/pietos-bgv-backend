package com.pietos.bgv.dto.response.client;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ClientComponentPricingResponse {
	
	private Long id;
	
	private Long clientPackageId;
	
	private Long clientPackageComponentId;
	
	private Long componentId;
	
	private String componentName;
	
	private BigDecimal price;
	
	private LocalDate effectiveFrom;
	
	private LocalDate effectiveTo;
	
	private Boolean isActive;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public Long getClientPackageId() {
		return clientPackageId;
	}

	public void setClientPackageId(Long clientPackageId) {
		this.clientPackageId = clientPackageId;
	}

	public Long getClientPackageComponentId() {
		return clientPackageComponentId;
	}

	public void setClientPackageComponentId(Long clientPackageComponentId) {
		this.clientPackageComponentId = clientPackageComponentId;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	public Long getComponentId() {
	    return componentId;
	}

	public void setComponentId(Long componentId) {
	    this.componentId = componentId;
	}
	

}
