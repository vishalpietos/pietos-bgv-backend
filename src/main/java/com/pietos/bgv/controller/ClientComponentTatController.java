package com.pietos.bgv.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pietos.bgv.dto.request.client.ClientComponentTatRequest;
import com.pietos.bgv.dto.response.ApiResponse;
import com.pietos.bgv.dto.response.client.ClientComponentTatResponse;
import com.pietos.bgv.service.ClientComponentTatService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "Client Contract TAT",
        description = "APIs for managing client contract turnaround time"
)
@RestController
@RequestMapping("/api/client-component-tats")
public class ClientComponentTatController {

    private final ClientComponentTatService clientComponentTatService;

    public ClientComponentTatController(
            ClientComponentTatService clientComponentTatService) {

        this.clientComponentTatService = clientComponentTatService;
    }

    // =========================================
    // CREATE / SAVE TAT
    // =========================================

    @PostMapping
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ClientComponentTatResponse>>>
            saveOrUpdateClientComponentTat(
                    @RequestBody ClientComponentTatRequest request) {

        List<ClientComponentTatResponse> response =
                clientComponentTatService
                        .saveOrUpdateClientComponentTat(request);

        ApiResponse<List<ClientComponentTatResponse>> apiResponse =
                new ApiResponse<>(
                        true,
                        "Component TAT saved successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    // =========================================
    // UPDATE SINGLE TAT RECORD
    // =========================================

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ClientComponentTatResponse>>>
            updateClientComponentTat(
                    @PathVariable Long id,
                    @RequestBody ClientComponentTatRequest request) {

        List<ClientComponentTatResponse> response =
                clientComponentTatService
                        .updateClientComponentTat(id, request);

        ApiResponse<List<ClientComponentTatResponse>> apiResponse =
                new ApiResponse<>(
                        true,
                        "Component TAT updated successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    // =========================================
    // GET ALL TAT BY CLIENT
    // =========================================

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ClientComponentTatResponse>>>
            getTatByClientId(
                    @PathVariable Long clientId) {

        List<ClientComponentTatResponse> response =
                clientComponentTatService
                        .getTatByClientId(clientId);

        ApiResponse<List<ClientComponentTatResponse>> apiResponse =
                new ApiResponse<>(
                        true,
                        "Component TAT fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    // =========================================
    // DELETE / DEACTIVATE TAT
    // =========================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<String>>
            deleteTatById(
                    @PathVariable Long id) {

        clientComponentTatService.deleteTatById(id);

        ApiResponse<String> apiResponse =
                new ApiResponse<>(
                        true,
                        "Component TAT deleted successfully.",
                        "Success");

        return ResponseEntity.ok(apiResponse);
    }
}