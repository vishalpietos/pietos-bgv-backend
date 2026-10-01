package com.pietos.bgv.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pietos.bgv.dto.request.client.ClientInformationRequest;
import com.pietos.bgv.dto.request.client.ClientInformationSearchRequest;
import com.pietos.bgv.dto.response.ApiResponse;
import com.pietos.bgv.dto.response.client.ClientDropdownResponse;
import com.pietos.bgv.dto.response.client.ClientInformationResponse;
import com.pietos.bgv.service.ClientInformationService;

import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(
	    name = "Client Information",
	    description = "APIs for managing client information"
	)
@RestController
@RequestMapping("/api/client-information")
public class ClientInformationController {

    private final ClientInformationService clientService;

    public ClientInformationController(ClientInformationService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ClientInformationResponse>> createClient(
            @RequestBody ClientInformationRequest request) {

        ClientInformationResponse response = clientService.createClient(request);

        ApiResponse<ClientInformationResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Client created successfully.",
                        response);

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ClientInformationResponse>>> getAllClients() {

        List<ClientInformationResponse> response = clientService.getAllClients();

        ApiResponse<List<ClientInformationResponse>> apiResponse =
                new ApiResponse<>(
                        true,
                        "Clients fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }
    
    @GetMapping("/dropdown")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<List<ClientDropdownResponse>>> getClientDropdown() {

        List<ClientDropdownResponse> response =
                clientService.getClientDropdown();

        ApiResponse<List<ClientDropdownResponse>> apiResponse =
                new ApiResponse<>(
                        true,
                        "Clients fetched successfully.",
                        response
                );

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ClientInformationResponse>> getClientById(
            @PathVariable Long id) {

        ClientInformationResponse response = clientService.getClientById(id);

        ApiResponse<ClientInformationResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Client fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ClientInformationResponse>> updateClient(
            @PathVariable Long id,
            @RequestBody ClientInformationRequest request) {

        ClientInformationResponse response =
                clientService.updateClient(id, request);

        ApiResponse<ClientInformationResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Client updated successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<String>> activateClient(
            @PathVariable Long id) {

        clientService.activateClient(id);

        ApiResponse<String> apiResponse =
                new ApiResponse<>(
                        true,
                        "Client activated successfully.",
                        null);

        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<String>> deactivateClient(
            @PathVariable Long id) {

        clientService.deactivateClient(id);

        ApiResponse<String> apiResponse =
                new ApiResponse<>(
                        true,
                        "Client deactivated successfully.",
                        null);

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ClientInformationResponse>>> searchClients(
            @ModelAttribute ClientInformationSearchRequest request){

        List<ClientInformationResponse> response =
                clientService.searchClients(request);

        ApiResponse<List<ClientInformationResponse>> apiResponse =
                new ApiResponse<>(
                        true,
                        "Clients fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }
    
}