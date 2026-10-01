package com.pietos.bgv.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.pietos.bgv.dto.request.client.ClientContractRequest;
import com.pietos.bgv.dto.response.client.ClientContractDocumentResponse;
import com.pietos.bgv.dto.response.client.ClientContractResponse;
import com.pietos.bgv.service.ClientContractService;

@RestController
@RequestMapping("/api/client-contracts")
public class ClientContractController {

    private final ClientContractService clientContractService;

    public ClientContractController(
            ClientContractService clientContractService) {

        this.clientContractService = clientContractService;
    }

    // =========================================================
    // CREATE CLIENT CONTRACT
    // =========================================================

    @PostMapping
    public ResponseEntity<ClientContractResponse> createContract(
            @RequestBody ClientContractRequest request) {

        ClientContractResponse response =
                clientContractService.saveClientContract(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================================================
    // UPLOAD CONTRACT DOCUMENTS
    // =========================================================

    @PostMapping(
            value = "/{contractId}/documents",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ClientContractResponse> uploadContractDocuments(
            @PathVariable Long contractId,
            @RequestPart("contractDocuments") List<MultipartFile> contractDocuments) {

        ClientContractResponse response =
                clientContractService.uploadContractDocuments(
                        contractId,
                        contractDocuments);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================================================
    // UPDATE CLIENT CONTRACT
    // =========================================================

    @PutMapping("/{contractId}")
    public ResponseEntity<ClientContractResponse> updateContract(
            @PathVariable Long contractId,
            @RequestBody ClientContractRequest request) {

        ClientContractResponse response =
                clientContractService.updateClientContract(
                        contractId,
                        request);

        return ResponseEntity
                .ok(response);
    }

    // =========================================================
    // GET CONTRACTS BY CLIENT
    // =========================================================

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ClientContractResponse>> getContractsByClient(
            @PathVariable Long clientId) {

        List<ClientContractResponse> response =
                clientContractService.getContractsByClient(clientId);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET ACTIVE CONTRACTS
    // =========================================================

    @GetMapping("/active")
    public ResponseEntity<List<ClientContractResponse>> getActiveContracts() {

        List<ClientContractResponse> response =
                clientContractService.getActiveContracts();

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET EXPIRED CONTRACTS
    // =========================================================

    @GetMapping("/expired")
    public ResponseEntity<List<ClientContractResponse>> getExpiredContracts() {

        List<ClientContractResponse> response =
                clientContractService.getExpiredContracts();

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET CONTRACTS EXPIRING WITHIN DAYS
    // =========================================================

    @GetMapping("/expiring")
    public ResponseEntity<List<ClientContractResponse>> getContractsExpiringWithin(
            @RequestParam int days) {

        List<ClientContractResponse> response =
                clientContractService.getContractsExpiringWithin(days);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // OPEN / VIEW CONTRACT DOCUMENT
    // =========================================================

    @GetMapping("/documents/{documentId}")
    public ResponseEntity<byte[]> getDocument(
            @PathVariable Long documentId) {

        ClientContractDocumentResponse document =
                clientContractService.getDocumentData(documentId);

        MediaType mediaType;

        try {
            mediaType = MediaType.parseMediaType(
                    document.getDocumentType());
        } catch (Exception e) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity
                .ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" +
                                document.getDocumentName() + "\"")
                .body(document.getDocumentData());
    }

    // =========================================================
    // DELETE CLIENT CONTRACT
    // =========================================================

    @DeleteMapping("/{contractId}")
    public ResponseEntity<Void> deleteContract(
            @PathVariable Long contractId) {

        clientContractService.deleteClientContract(contractId);

        return ResponseEntity.noContent().build();
    }
}