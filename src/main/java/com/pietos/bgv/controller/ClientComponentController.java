package com.pietos.bgv.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pietos.bgv.dto.request.client.ClientComponentRequest;
import com.pietos.bgv.dto.response.ApiResponse;
import com.pietos.bgv.dto.response.client.ClientComponentResponse;
import com.pietos.bgv.dto.response.client.ClientSelectedComponentResponse;
import com.pietos.bgv.service.ClientComponentService;

import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(
	    name = "Client Components",
	    description = "APIs for managing components assigned to clients"
	)
@RestController
@RequestMapping("/api/client-components")
public class ClientComponentController {

    private final ClientComponentService clientComponentService;

    public ClientComponentController(ClientComponentService clientComponentService) {
        this.clientComponentService = clientComponentService;
    }

    // Save Client Components
    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','CLIENT_ADMIN')")
    public ResponseEntity<ApiResponse<ClientSelectedComponentResponse>> saveClientComponents(
            @RequestBody ClientComponentRequest request) {

        ClientSelectedComponentResponse response =
                clientComponentService.saveClientComponents(request);

        ApiResponse<ClientSelectedComponentResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Client components saved successfully.",
                        response);

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    // Get Components of a Client
    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ClientSelectedComponentResponse>> getComponentsByClientId(
            @PathVariable Long clientId) {

        ClientSelectedComponentResponse response =
                clientComponentService.getComponentsByClientId(clientId);

        ApiResponse<ClientSelectedComponentResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Client components fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    // Get Clients by Component
    @GetMapping("/component/{componentId}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ClientComponentResponse>>> getClientsByComponentId(
            @PathVariable Long componentId) {

        List<ClientComponentResponse> response =
                clientComponentService.getClientsByComponentId(componentId);

        ApiResponse<List<ClientComponentResponse>> apiResponse =
                new ApiResponse<>(
                        true,
                        "Clients fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    // Delete Single Component from Client
    @DeleteMapping("/{clientId}/components/{componentId}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteComponent(
            @PathVariable Long clientId,
            @PathVariable Long componentId) {

        clientComponentService.deleteComponentByClientIdAndComponentId(
                clientId,
                componentId);

        ApiResponse<Void> apiResponse =
                new ApiResponse<>(
                        true,
                        "Component removed successfully.",
                        null);

        return ResponseEntity.ok(apiResponse);
    }

}