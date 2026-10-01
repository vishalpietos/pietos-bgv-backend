package com.pietos.bgv.dto.request.client;

import java.math.BigDecimal;

public class ClientComponentPricingItemRequest {

	private Long clientPackageComponentId;

    private BigDecimal price;


    public Long getClientPackageComponentId() {
		return clientPackageComponentId;
	}

	public void setClientPackageComponentId(Long clientPackageComponentId) {
		this.clientPackageComponentId = clientPackageComponentId;
	}

	public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}