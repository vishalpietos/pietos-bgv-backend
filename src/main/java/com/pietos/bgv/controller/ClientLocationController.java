package com.pietos.bgv.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pietos.bgv.dto.request.client.ClientLocationCreateRequest;
import com.pietos.bgv.dto.request.client.ClientLocationPatchRequest;
import com.pietos.bgv.dto.request.client.ClientLocationSearchRequest;
import com.pietos.bgv.dto.request.client.ClientLocationUpdateRequest;
import com.pietos.bgv.dto.response.ApiResponse;
import com.pietos.bgv.dto.response.client.ClientLocationDropdownResponse;
import com.pietos.bgv.dto.response.client.ClientLocationResponse;
import com.pietos.bgv.service.ClientLocationService;

import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(
	    name = "Client Locations",
	    description = "APIs for managing client locations"
	)
@RestController
@RequestMapping("/api/client-locations")
public class ClientLocationController {

    private final ClientLocationService locationService;

    public ClientLocationController(ClientLocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ClientLocationResponse>> createLocation(
            @RequestBody ClientLocationCreateRequest request) {

        ClientLocationResponse response =
                locationService.createLocation(request);

        ApiResponse<ClientLocationResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Location created successfully.",
                        response);

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ClientLocationResponse>>> getAllLocations() {

        List<ClientLocationResponse> response =
                locationService.getAllLocations();

        ApiResponse<List<ClientLocationResponse>> apiResponse =
                new ApiResponse<>(
                        true,
                        "Locations fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ClientLocationResponse>> getLocationById(
            @PathVariable Long id) {

        ClientLocationResponse response =
                locationService.getLocationById(id);

        ApiResponse<ClientLocationResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Location fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ClientLocationResponse>> updateLocation(
            @PathVariable Long id,
            @RequestBody ClientLocationUpdateRequest request) {

        ClientLocationResponse response =
                locationService.updateLocation(id, request);

        ApiResponse<ClientLocationResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Location updated successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<String>> activateLocation(
            @PathVariable Long id) {

        locationService.activateLocation(id);

        ApiResponse<String> apiResponse =
                new ApiResponse<>(
                        true,
                        "Location activated successfully.",
                        null);

        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<String>> deactivateLocation(
            @PathVariable Long id) {

        locationService.deactivateLocation(id);

        ApiResponse<String> apiResponse =
                new ApiResponse<>(
                        true,
                        "Location deactivated successfully.",
                        null);

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ClientLocationResponse>>> getLocationsByClient(
            @PathVariable Long clientId) {

        List<ClientLocationResponse> response =
                locationService.getLocationsByClientId(clientId);

        ApiResponse<List<ClientLocationResponse>> apiResponse =
                new ApiResponse<>(
                        true,
                        "Locations fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }
    
    @GetMapping("/client/dropdown/{clientId}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<List<ClientLocationDropdownResponse>>> getLocationsByClientdropdown(
            @PathVariable Long clientId) {

        List<ClientLocationDropdownResponse> response =
                locationService.getLocationsByClientdropdown(clientId);

        ApiResponse<List<ClientLocationDropdownResponse>> apiResponse =
                new ApiResponse<>(
                        true,
                        "Locations fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ClientLocationResponse>>> searchLocations(
            @ModelAttribute ClientLocationSearchRequest request) {

        List<ClientLocationResponse> response =
                locationService.searchLocations(request);

        ApiResponse<List<ClientLocationResponse>> apiResponse =
                new ApiResponse<>(
                        true,
                        "Locations fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }
    
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ClientLocationResponse>> patchLocation(
            @PathVariable Long id,
            @RequestBody ClientLocationPatchRequest request) {

        ClientLocationResponse response =
                locationService.patchLocation(id, request);

        ApiResponse<ClientLocationResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Location updated successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

}