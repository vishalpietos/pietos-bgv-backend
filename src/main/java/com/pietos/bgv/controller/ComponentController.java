package com.pietos.bgv.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pietos.bgv.dto.request.client.ComponentRequest;
import com.pietos.bgv.dto.response.ApiResponse;
import com.pietos.bgv.dto.response.client.ComponentResponse;
import com.pietos.bgv.service.ComponentService;

@RestController
@RequestMapping("/api/components")
public class ComponentController {

    private final ComponentService componentService;

    public ComponentController(ComponentService componentService) {
        this.componentService = componentService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ComponentResponse>> createComponent(
            @RequestBody ComponentRequest request) {

        ComponentResponse response =
                componentService.createComponent(request);

        ApiResponse<ComponentResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Component created successfully.",
                        response);

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ComponentResponse>>> getAllComponents() {

        List<ComponentResponse> response =
                componentService.getAllComponents();

        ApiResponse<List<ComponentResponse>> apiResponse =
                new ApiResponse<>(
                        true,
                        "Components fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ComponentResponse>> getComponentById(
            @PathVariable Long id) {

        ComponentResponse response =
                componentService.getComponentById(id);

        ApiResponse<ComponentResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Component fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ComponentResponse>> updateComponent(
            @PathVariable Long id,
            @RequestBody ComponentRequest request) {

        ComponentResponse response =
                componentService.updateComponent(id, request);

        ApiResponse<ComponentResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Component updated successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }


    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<String>> activateComponent(
            @PathVariable Long id) {

        componentService.activateComponent(id);

        ApiResponse<String> apiResponse =
                new ApiResponse<>(
                        true,
                        "Component activated successfully.",
                        null);

        return ResponseEntity.ok(apiResponse);
    }


    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<String>> deactivateComponent(
            @PathVariable Long id) {

        componentService.deactivateComponent(id);

        ApiResponse<String> apiResponse =
                new ApiResponse<>(
                        true,
                        "Component deactivated successfully.",
                        null);

        return ResponseEntity.ok(apiResponse);
    }
}
