package com.pietos.bgv.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pietos.bgv.dto.request.client.ClientComponentPricingRequest;
import com.pietos.bgv.dto.response.ApiResponse;
import com.pietos.bgv.dto.response.client.ClientComponentPricingResponse;
import com.pietos.bgv.service.ClientComponentPricingService;

import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(
	    name = "Client Contract Pricing",
	    description = "APIs for managing client contract pricing"
	)
@RestController
@RequestMapping("/api/client-component-pricing")
public class ClientComponentPricingController {
	
	private final ClientComponentPricingService
				  clientComponentPricingService;
	
	public ClientComponentPricingController(
			ClientComponentPricingService clientComponentPricingService) {
		this.clientComponentPricingService =
                clientComponentPricingService;
	}
	
	   @PostMapping
	   @PreAuthorize("hasAuthority('SUPER_ADMIN')")
	    public ResponseEntity<ApiResponse<ClientComponentPricingResponse>>
	            createPricing( @RequestBody ClientComponentPricingRequest request) {

	        ClientComponentPricingResponse response =clientComponentPricingService.createPricing(request);

	        ApiResponse<ClientComponentPricingResponse> apiResponse =
	                new ApiResponse<>(true,
	                        "Client component pricing created successfully.",
	                        response);

	        return new ResponseEntity<>(
	                apiResponse,
	                HttpStatus.CREATED);
	    }
	   
	   @GetMapping
	    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
	    public ResponseEntity<
	            ApiResponse<List<ClientComponentPricingResponse>>>
	            getAllPricing() {

	        List<ClientComponentPricingResponse> response =
	                clientComponentPricingService.getPricing();

	        ApiResponse<List<ClientComponentPricingResponse>> apiResponse =
	                new ApiResponse<>(
	                        true,
	                        "Client component pricing fetched successfully.",
	                        response);

	        return ResponseEntity.ok(apiResponse);
	    }   
	   
	   @GetMapping("/{id}")
	    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
	    public ResponseEntity<
	            ApiResponse<ClientComponentPricingResponse>>
	            getPricingById(
	                    @PathVariable Long id) {

	        ClientComponentPricingResponse response =
	                clientComponentPricingService
	                        .getPricingById(id);

	        ApiResponse<ClientComponentPricingResponse> apiResponse =
	                new ApiResponse<>(
	                        true,
	                        "Client component pricing fetched successfully.",
	                        response);

	        return ResponseEntity.ok(apiResponse);
	    }
	   
	   @GetMapping("/client/{clientId}")
	    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
	    public ResponseEntity<
	            ApiResponse<List<ClientComponentPricingResponse>>>
	            getPricingByClientId(
	                    @PathVariable Long clientId) {

	        List<ClientComponentPricingResponse> response =
	                clientComponentPricingService
	                        .getPricingByClientId(clientId);

	        ApiResponse<List<ClientComponentPricingResponse>> apiResponse =
	                new ApiResponse<>(
	                        true,
	                        "Client pricing fetched successfully.",
	                        response);

	        return ResponseEntity.ok(apiResponse);
	    }
	   
	   @PutMapping("/{id}")
	    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
	    public ResponseEntity<
	            ApiResponse<ClientComponentPricingResponse>>
	            updatePricing(
	                    @PathVariable Long id,
	                    @RequestBody ClientComponentPricingRequest request) {

	        ClientComponentPricingResponse response =
	                clientComponentPricingService
	                        .updatePricing(id, request);

	        ApiResponse<ClientComponentPricingResponse> apiResponse =
	                new ApiResponse<>(
	                        true,
	                        "Client component pricing updated successfully.",
	                        response);

	        return ResponseEntity.ok(apiResponse);
	    } 
	   
	   @DeleteMapping("/{id}")
	    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
	    public ResponseEntity<ApiResponse<Void>>
	            deactivatePricing(
	                    @PathVariable Long id) {

	        clientComponentPricingService
	                .deactivatePricing(id);

	        ApiResponse<Void> apiResponse =
	                new ApiResponse<>(
	                        true,
	                        "Client component pricing deactivated successfully.",
	                        null);

	        return ResponseEntity.ok(apiResponse);
	    }
}
