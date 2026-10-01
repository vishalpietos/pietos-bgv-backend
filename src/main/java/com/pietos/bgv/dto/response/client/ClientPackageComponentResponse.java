package com.pietos.bgv.dto.response.client;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ClientPackageComponentResponse {

    private Long id;

    private Long packageId;

    private Long componentId;

    private String componentName;

    private BigDecimal ratePerCheck;

    private Integer tat;

    private Boolean isActive;
    
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    


    // ==========================
    // Getters and Setters
    // ==========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

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

    public BigDecimal getRatePerCheck() {
        return ratePerCheck;
    }

    public void setRatePerCheck(BigDecimal ratePerCheck) {
        this.ratePerCheck = ratePerCheck;
    }

    public Integer getTat() {
        return tat;
    }

    public void setTat(Integer tat) {
        this.tat = tat;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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
    
}