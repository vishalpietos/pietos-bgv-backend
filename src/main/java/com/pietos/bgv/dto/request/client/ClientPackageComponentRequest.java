package com.pietos.bgv.dto.request.client;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ClientPackageComponentRequest {

    private Long componentId;
    
    private Long clientPackageId;

    private BigDecimal ratePerCheck;

    private Integer tat;
    
    private Long clientPackageComponentId;
    
    
    

    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
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

}