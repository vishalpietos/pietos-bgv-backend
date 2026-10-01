package com.pietos.bgv.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pietos.bgv.dto.request.ManageUser.ManageUserCreateRequest;
import com.pietos.bgv.dto.request.ManageUser.ManageUserPatchRequest;
import com.pietos.bgv.dto.request.ManageUser.ManageUserSearchRequest;
import com.pietos.bgv.dto.request.ManageUser.ManageUserUpdateRequest;
import com.pietos.bgv.dto.response.ApiResponse;
import com.pietos.bgv.dto.response.ManageUserResponse;
import com.pietos.bgv.dto.response.client.ClientLocationDataResponse;
import com.pietos.bgv.service.ManageUserService;

@RestController
@RequestMapping("/api/manage-users")
public class ManageUserController {

    private final ManageUserService manageUserService;

    public ManageUserController(ManageUserService manageUserService) {
        this.manageUserService = manageUserService;
    }


    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','CLIENT_ADMIN')")
    public ResponseEntity<ApiResponse<ManageUserResponse>> createUser(
            @RequestBody ManageUserCreateRequest request) {

        ManageUserResponse response =
                manageUserService.createUser(request);

        ApiResponse<ManageUserResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "User created successfully.",
                        response);

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }


    @GetMapping
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ManageUserResponse>>> getAllUsers() {

        List<ManageUserResponse> response =
                manageUserService.getAllUsers();

        ApiResponse<List<ManageUserResponse>> apiResponse =
                new ApiResponse<>(
                        true,
                        "Users fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ManageUserResponse>> getUserById(
            @PathVariable Long id) {

        ManageUserResponse response =
                manageUserService.getUserById(id);

        ApiResponse<ManageUserResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "User fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ManageUserResponse>> updateUser(
            @PathVariable Long id,
            @RequestBody ManageUserUpdateRequest request) {

        ManageUserResponse response =
                manageUserService.updateUser(id, request);

        ApiResponse<ManageUserResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "User updated successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }


    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ManageUserResponse>> patchUser(
            @PathVariable Long id,
            @RequestBody ManageUserPatchRequest request) {

        ManageUserResponse response =
                manageUserService.patchUser(id, request);

        ApiResponse<ManageUserResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "User updated successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }


    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<String>> activateUser(
            @PathVariable Long id) {

        manageUserService.activateUser(id);

        ApiResponse<String> apiResponse =
                new ApiResponse<>(
                        true,
                        "User activated successfully.",
                        null);

        return ResponseEntity.ok(apiResponse);
    }


    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<String>> deactivateUser(
            @PathVariable Long id) {

        manageUserService.deactivateUser(id);

        ApiResponse<String> apiResponse =
                new ApiResponse<>(
                        true,
                        "User deactivated successfully.",
                        null);

        return ResponseEntity.ok(apiResponse);
    }


    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ManageUserResponse>>> getUsersByClientId(
            @PathVariable Long clientId) {

        List<ManageUserResponse> response =
                manageUserService.getUsersByClientId(clientId);

        ApiResponse<List<ManageUserResponse>> apiResponse =
                new ApiResponse<>(
                        true,
                        "Users fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }


    @GetMapping("/location/{locationId}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ManageUserResponse>>> getUsersByLocationId(
            @PathVariable Long locationId) {

        List<ManageUserResponse> response =
                manageUserService.getUsersByLocationId(locationId);

        ApiResponse<List<ManageUserResponse>> apiResponse =
                new ApiResponse<>(
                        true,
                        "Users fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }


    @GetMapping("/search")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ManageUserResponse>>> searchUsers(
            @ModelAttribute ManageUserSearchRequest request) {

        List<ManageUserResponse> response =
                manageUserService.searchUsers(request);

        ApiResponse<List<ManageUserResponse>> apiResponse =
                new ApiResponse<>(
                        true,
                        "Users fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }
    
    @GetMapping("/client/{clientId}/location-data")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ClientLocationDataResponse>>
    getClientLocationData(
            @PathVariable Long clientId) {

        ClientLocationDataResponse response =
                manageUserService.getClientLocationData(clientId);

        ApiResponse<ClientLocationDataResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Client location data fetched successfully.",
                        response
                );

        return ResponseEntity.ok(apiResponse);
    }

}
