package com.pietos.bgv.service;

import java.util.List;

import com.pietos.bgv.dto.request.client.ClientComponentPricingRequest;
import com.pietos.bgv.dto.response.client.ClientComponentPricingResponse;

public interface ClientComponentPricingService {
	
	ClientComponentPricingResponse createPricing(ClientComponentPricingRequest request);
	
	List<ClientComponentPricingResponse> getPricing();
	
	ClientComponentPricingResponse getPricingById(Long id);
	
	List<ClientComponentPricingResponse> getPricingByClientId(Long clientId);
	
	ClientComponentPricingResponse updatePricing(Long id, ClientComponentPricingRequest request);
	
	void deactivatePricing(Long id); 
}
