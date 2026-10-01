package com.pietos.bgv.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pietos.bgv.dto.request.client.ClientPackageRequest;
import com.pietos.bgv.dto.response.ApiResponse;
import com.pietos.bgv.dto.response.client.ClientPackageResponse;
import com.pietos.bgv.service.ClientPackageService;

import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(
	    name = "Client Packages",
	    description = "APIs for managing client packages"
	)
@RestController
@RequestMapping("/api/client-packages")
public class ClientPackageController {

    private final ClientPackageService clientPackageService;

    public ClientPackageController(
            ClientPackageService clientPackageService) {

        this.clientPackageService = clientPackageService;
    }


    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<ApiResponse<ClientPackageResponse>> createPackage(
            @RequestBody ClientPackageRequest request) {

        ClientPackageResponse response =
                clientPackageService.saveClientPackage(request);

        ApiResponse<ClientPackageResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Client package created successfully.",
                        response);

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.CREATED);
    }

 

    @PutMapping("/{packageId}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<ApiResponse<ClientPackageResponse>> updatePackage(
            @PathVariable Long packageId,
            @RequestBody ClientPackageRequest request) {

        ClientPackageResponse response =
                clientPackageService.updateClientPackage(
                        packageId,
                        request);

        ApiResponse<ClientPackageResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Client package updated successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }


    @GetMapping("/{packageId}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<ApiResponse<ClientPackageResponse>> getPackageById(
            @PathVariable Long packageId) {

        ClientPackageResponse response =
                clientPackageService.getClientPackageById(
                        packageId);

        ApiResponse<ClientPackageResponse> apiResponse =
                new ApiResponse<>(
                        true,	
                        "Client package fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }


    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<List<ClientPackageResponse>>> getPackagesByClient(
            @PathVariable Long clientId) {

        List<ClientPackageResponse> response =
                clientPackageService.getPackagesByClient(
                        clientId);

        ApiResponse<List<ClientPackageResponse>> apiResponse =
                new ApiResponse<>(
                        true,
                        "Client packages fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }
}