package com.pietos.bgv.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pietos.bgv.dto.request.client.ClientHolidayConfigurationRequest;
import com.pietos.bgv.dto.response.ApiResponse;
import com.pietos.bgv.dto.response.client.ClientHolidayConfigurationResponse;
import com.pietos.bgv.service.ClientHolidayConfigurationService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
	    name = "Client Holiday Configuration",
	    description = "APIs for managing client holiday configurations"
	)
@RestController
@RequestMapping("/api/client-holiday-configurations")
public class ClientHolidayConfigurationController {

    private final ClientHolidayConfigurationService holidayConfigurationService;

    public ClientHolidayConfigurationController(
            ClientHolidayConfigurationService holidayConfigurationService) {

        this.holidayConfigurationService = holidayConfigurationService;
    }

    // Save or Update Holiday Configuration
    @PutMapping
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ClientHolidayConfigurationResponse>>
            saveOrUpdate(
                    @RequestBody ClientHolidayConfigurationRequest request) {

        ClientHolidayConfigurationResponse response =
                holidayConfigurationService.saveOrUpdate(request);

        ApiResponse<ClientHolidayConfigurationResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Holiday configuration saved successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    // Get Holiday Configuration By Client
    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ClientHolidayConfigurationResponse>>
            getByClientId(
                    @PathVariable Long clientId) {

        ClientHolidayConfigurationResponse response =
                holidayConfigurationService.getByClientId(clientId);

        ApiResponse<ClientHolidayConfigurationResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Holiday configuration fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    // Delete Holiday Configuration
    @DeleteMapping("/client/{clientId}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<String>>
            deleteByClientId(
                    @PathVariable Long clientId) {

        holidayConfigurationService.deleteByClientId(clientId);

        ApiResponse<String> apiResponse =
                new ApiResponse<>(
                        true,
                        "Holiday configuration deleted successfully.",
                        "Success");

        return ResponseEntity.ok(apiResponse);
    }
}