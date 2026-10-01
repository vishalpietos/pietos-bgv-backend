package com.pietos.bgv.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pietos.bgv.dto.request.client.ClientPackageComponentRequest;
import com.pietos.bgv.dto.response.ApiResponse;
import com.pietos.bgv.dto.response.client.ClientPackageComponentResponse;
import com.pietos.bgv.service.ClientPackageComponentService;

@RestController
@RequestMapping("/api/client-package-components")
public class ClientPackageComponentController {

    private final ClientPackageComponentService
            clientPackageComponentService;

    public ClientPackageComponentController(
            ClientPackageComponentService clientPackageComponentService) {

        this.clientPackageComponentService =
                clientPackageComponentService;
    }

  

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ClientPackageComponentResponse>>
            addComponent(
                    @RequestBody ClientPackageComponentRequest request) {

        ClientPackageComponentResponse response =
                clientPackageComponentService.addComponent(request);

        ApiResponse<ClientPackageComponentResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Component added to package successfully.",
                        response);

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.CREATED);
    }

    @GetMapping("/package/{packageId}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','DATA_ENTRY_TEAM_MEMBER')")
    public ResponseEntity<
            ApiResponse<List<ClientPackageComponentResponse>>>
            getComponentsByPackageId(
                    @PathVariable Long packageId) {

        List<ClientPackageComponentResponse> response =
                clientPackageComponentService
                        .getComponentsByPackageId(packageId);

        ApiResponse<List<ClientPackageComponentResponse>> apiResponse =
                new ApiResponse<>(
                        true,
                        "Package components fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>>
            deactivateComponent(
                    @PathVariable Long id) {

        clientPackageComponentService
                .deactivateComponent(id);

        ApiResponse<Void> apiResponse =
                new ApiResponse<>(
                        true,
                        "Package component deactivated successfully.",
                        null);

        return ResponseEntity.ok(apiResponse);
    }
}