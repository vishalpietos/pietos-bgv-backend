package com.pietos.bgv.dto.response.client;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ClientPackageResponse {

    private Long id;

    private Long clientId;

    private String clientName;

    private String packageName;

    private String packageType;

    private Integer packageTat;

    private Integer internalTat;

    private BigDecimal packageRate;

    private String holidayType;

    private Boolean isActive;

    private List<ClientPackageComponentResponse> components;
    
    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    private LocalDate internalEffectiveTo;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public Integer getPackageTat() {
        return packageTat;
    }

    public void setPackageTat(Integer packageTat) {
        this.packageTat = packageTat;
    }

    public Integer getInternalTat() {
        return internalTat;
    }

    public void setInternalTat(Integer internalTat) {
        this.internalTat = internalTat;
    }

    public BigDecimal getPackageRate() {
        return packageRate;
    }

    public void setPackageRate(BigDecimal packageRate) {
        this.packageRate = packageRate;
    }

    public String getHolidayType() {
        return holidayType;
    }

    public void setHolidayType(String holidayType) {
        this.holidayType = holidayType;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<ClientPackageComponentResponse> getComponents() {
        return components;
    }

    public void setComponents(
            List<ClientPackageComponentResponse> components) {
        this.components = components;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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

	public LocalDate getInternalEffectiveTo() {
		return internalEffectiveTo;
	}

	public void setInternalEffectiveTo(LocalDate internalEffectiveTo) {
		this.internalEffectiveTo = internalEffectiveTo;
	}
    
    
}