package com.pietos.bgv.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.pietos.bgv.dto.request.client.ClientContractRequest;
import com.pietos.bgv.dto.response.client.ClientContractDocumentResponse;
import com.pietos.bgv.dto.response.client.ClientContractResponse;
import com.pietos.bgv.entity.ClientContract;
import com.pietos.bgv.entity.ClientContractDocument;
import com.pietos.bgv.entity.ClientInformation;
import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.exception.DuplicateResourceException;
import com.pietos.bgv.exception.ResourceNotFoundException;
import com.pietos.bgv.repository.ClientContractDocumentRepository;
import com.pietos.bgv.repository.ClientContractRepository;
import com.pietos.bgv.repository.ClientInformationRepository;
import com.pietos.bgv.security.LoggedInUserService;
import com.pietos.bgv.service.ClientContractService;

@Service
@Transactional
public class ClientContractServiceImpl implements ClientContractService {

    private final ClientContractRepository clientContractRepository;
    private final ClientInformationRepository clientRepository;
    private final ClientContractDocumentRepository clientContractDocumentRepository;
    private final LoggedInUserService loggedInUserService;

    public ClientContractServiceImpl(
            ClientContractRepository clientContractRepository,
            ClientInformationRepository clientRepository,
            ClientContractDocumentRepository clientContractDocumentRepository,
            LoggedInUserService loggedInUserService) {

        this.clientContractRepository = clientContractRepository;
        this.clientRepository = clientRepository;
        this.clientContractDocumentRepository = clientContractDocumentRepository;
        this.loggedInUserService = loggedInUserService;
    }

    // =========================================================
    // CREATE CONTRACT
    // =========================================================

    @Override
    public ClientContractResponse saveClientContract(
            ClientContractRequest request) {

        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();

        validateSuperAdmin();

        ClientInformation client =
                clientRepository.findById(request.getClientId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Client not found with id : "
                                                + request.getClientId()));

        validateDates(request.getWef(), request.getWet());

        boolean exists =
                clientContractRepository
                        .existsByClientInformationIdAndWefLessThanEqualAndWetGreaterThanEqual(
                                request.getClientId(),
                                request.getWet(),
                                request.getWef());

        if (exists) {
            throw new DuplicateResourceException(
                    "A contract already exists for the selected date range.");
        }

        ClientContract contract = new ClientContract();

        contract.setClientInformation(client);
        contract.setContractName(request.getContractName());
        contract.setWef(request.getWef());
        contract.setWet(request.getWet());

        contract.setCreatedBy(loggedInUser);
        contract.setUpdatedBy(loggedInUser);

        contract.setCreatedAt(LocalDateTime.now());
        contract.setUpdatedAt(LocalDateTime.now());

        ClientContract savedContract =
                clientContractRepository.save(contract);

        /*
         * Save documents if provided during contract creation
         */
        if (request.getContractDocuments() != null
                && !request.getContractDocuments().isEmpty()) {

            saveDocuments(
                    savedContract,
                    request.getContractDocuments(),
                    loggedInUser);
        }

        return mapToResponse(savedContract);
    }

    // =========================================================
    // UPLOAD CONTRACT DOCUMENTS
    // =========================================================

    @Override
    public ClientContractResponse uploadContractDocuments(
            Long contractId,
            List<MultipartFile> contractFiles) {

        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();

        String role = getCurrentRole();

        if (!"SUPER_ADMIN".equals(role)
                && !"CLIENT_ADMIN".equals(role)) {

            throw new ResourceNotFoundException(
                    "You are not authorized to upload contract documents.");
        }

        ClientContract contract =
                clientContractRepository.findById(contractId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Client contract not found with id : "
                                                + contractId));

        /*
         * CLIENT_ADMIN can upload documents
         * only for their own client.
         */
        if ("CLIENT_ADMIN".equals(role)) {

            ClientInformation loggedInClient =
                    clientRepository.findBySystemUser(loggedInUser)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Client not found for logged in user."));

            if (!contract.getClientInformation()
                    .getId()
                    .equals(loggedInClient.getId())) {

                throw new ResourceNotFoundException(
                        "You can upload documents only for your own client.");
            }
        }

        if (contractFiles == null
                || contractFiles.isEmpty()) {

            throw new IllegalArgumentException(
                    "Please select at least one contract document.");
        }

        saveDocuments(
                contract,
                contractFiles,
                loggedInUser);

        contract.setUpdatedBy(loggedInUser);
        contract.setUpdatedAt(LocalDateTime.now());

        clientContractRepository.save(contract);

        return mapToResponse(contract);
    }

    // =========================================================
    // UPDATE CONTRACT
    // =========================================================

    @Override
    public ClientContractResponse updateClientContract(
            Long contractId,
            ClientContractRequest request) {

        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();

        validateSuperAdmin();

        ClientContract contract =
                clientContractRepository.findById(contractId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Client contract not found with id : "
                                                + contractId));

        ClientInformation client =
                clientRepository.findById(request.getClientId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Client not found with id : "
                                                + request.getClientId()));

        validateDates(request.getWef(), request.getWet());

        boolean exists =
                clientContractRepository
                        .existsByClientInformationIdAndWefLessThanEqualAndWetGreaterThanEqualAndIdNot(
                                request.getClientId(),
                                request.getWet(),
                                request.getWef(),
                                contractId);

        if (exists) {
            throw new DuplicateResourceException(
                    "A contract already exists for the selected date range.");
        }

        contract.setClientInformation(client);
        contract.setContractName(request.getContractName());
        contract.setWef(request.getWef());
        contract.setWet(request.getWet());

        contract.setUpdatedBy(loggedInUser);
        contract.setUpdatedAt(LocalDateTime.now());

        /*
         * If new documents are provided during update,
         * add them to the existing documents.
         */
        if (request.getContractDocuments() != null
                && !request.getContractDocuments().isEmpty()) {

            saveDocuments(
                    contract,
                    request.getContractDocuments(),
                    loggedInUser);
        }

        ClientContract updatedContract =
                clientContractRepository.save(contract);

        return mapToResponse(updatedContract);
    }

    // =========================================================
    // GET CONTRACTS BY CLIENT
    // =========================================================

    @Override
    public List<ClientContractResponse> getContractsByClient(
            Long clientId) {

        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();

        String role = getCurrentRole();

        if (!"SUPER_ADMIN".equals(role)
                && !"CLIENT_ADMIN".equals(role)) {

            throw new ResourceNotFoundException(
                    "You are not authorized to view client contracts.");
        }

        if ("CLIENT_ADMIN".equals(role)) {

            ClientInformation loggedInClient =
                    clientRepository.findBySystemUser(loggedInUser)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Client not found for logged in user."));

            if (!loggedInClient.getId().equals(clientId)) {

                throw new ResourceNotFoundException(
                        "You can view only your own contracts.");
            }
        }

        List<ClientContract> contracts =
                clientContractRepository
                        .findByClientInformationId(clientId);

        if (contracts.isEmpty()) {

            throw new ResourceNotFoundException(
                    "No contracts found for client with id : "
                            + clientId);
        }

        List<ClientContractResponse> responseList =
                new ArrayList<>();

        for (ClientContract contract : contracts) {
            responseList.add(mapToResponse(contract));
        }

        return responseList;
    }

    // =========================================================
    // GET ACTIVE CONTRACTS
    // =========================================================

    @Override
    public List<ClientContractResponse> getActiveContracts() {

        validateSuperAdmin();

        LocalDate today = LocalDate.now();

        List<ClientContract> contracts =
                clientContractRepository
                        .findByWefLessThanEqualAndWetGreaterThanEqual(
                                today,
                                today);

        if (contracts.isEmpty()) {

            throw new ResourceNotFoundException(
                    "No active contracts found.");
        }

        return mapToResponseList(contracts);
    }

    // =========================================================
    // GET EXPIRED CONTRACTS
    // =========================================================

    @Override
    public List<ClientContractResponse> getExpiredContracts() {

        validateSuperAdmin();

        List<ClientContract> contracts =
                clientContractRepository
                        .findByWetBefore(LocalDate.now());

        if (contracts.isEmpty()) {

            throw new ResourceNotFoundException(
                    "No expired contracts found.");
        }

        return mapToResponseList(contracts);
    }

    // =========================================================
    // GET CONTRACTS EXPIRING WITHIN DAYS
    // =========================================================

    @Override
    public List<ClientContractResponse> getContractsExpiringWithin(
            int days) {

        validateSuperAdmin();

        LocalDate today = LocalDate.now();

        LocalDate endDate =
                today.plusDays(days);

        List<ClientContract> contracts =
                clientContractRepository
                        .findByWetBetween(
                                today,
                                endDate);

        if (contracts.isEmpty()) {

            throw new ResourceNotFoundException(
                    "No contracts are expiring within the next "
                            + days + " days.");
        }

        return mapToResponseList(contracts);
    }

    // =========================================================
    // DELETE CONTRACT
    // =========================================================

    @Override
    public void deleteClientContract(
            Long contractId) {

        validateSuperAdmin();

        ClientContract contract =
                clientContractRepository
                        .findById(contractId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Client contract not found with id : "
                                                + contractId));

        clientContractRepository.delete(contract);
    }

    // =========================================================
    // SAVE DOCUMENTS
    // =========================================================

    private void saveDocuments(
            ClientContract contract,
            List<MultipartFile> files,
            SystemUser loggedInUser) {

        List<ClientContractDocument> documents =
                new ArrayList<>();

        for (MultipartFile file : files) {

            if (file == null || file.isEmpty()) {
                continue;
            }

            try {

                ClientContractDocument document =
                        new ClientContractDocument();

                document.setClientContract(contract);

                document.setDocumentName(file.getOriginalFilename());

                document.setDocumentType(file.getContentType());
                
                document.setDocumentData(file.getBytes());

                document.setCreatedBy(loggedInUser);
                document.setUpdatedBy(loggedInUser);

                document.setCreatedAt(LocalDateTime.now());

                document.setUpdatedAt(LocalDateTime.now());

                documents.add(document);

            } catch (IOException e) {

                throw new IllegalArgumentException(
                        "Failed to read document: "
                                + file.getOriginalFilename(),
                        e);
            }
        }

        if (documents.isEmpty()) {

            throw new IllegalArgumentException(
                    "No valid documents were provided.");
        }

        clientContractDocumentRepository
                .saveAll(documents);
    }

    // =========================================================
    // MAP RESPONSE
    // =========================================================

    private ClientContractResponse mapToResponse(
            ClientContract contract) {

        ClientContractResponse response =
                new ClientContractResponse();

        response.setId(contract.getId());

        response.setClientId(
                contract.getClientInformation().getId());

        response.setContractName(
                contract.getContractName());

        response.setWef(
                contract.getWef());

        response.setWet(
                contract.getWet());

        List<ClientContractDocumentResponse>
                documentResponses = new ArrayList<>();

        List<ClientContractDocument> documents =
                clientContractDocumentRepository
                        .findByClientContractId(
                                contract.getId());

        for (ClientContractDocument document : documents) {

            ClientContractDocumentResponse
                    documentResponse =
                            new ClientContractDocumentResponse();

            documentResponse.setId(
                    document.getId());

            documentResponse.setDocumentName(
                    document.getDocumentName());

            documentResponse.setDocumentType(
                    document.getDocumentType());

            documentResponses.add(
                    documentResponse);
        }

        response.setDocuments(documentResponses);

        return response;
    }

    // =========================================================
    // MAP LIST
    // =========================================================

    private List<ClientContractResponse> mapToResponseList(
            List<ClientContract> contracts) {

        List<ClientContractResponse> responseList =
                new ArrayList<>();

        for (ClientContract contract : contracts) {

            responseList.add(
                    mapToResponse(contract));
        }

        return responseList;
    }

    // =========================================================
    // VALIDATE DATES
    // =========================================================

    private void validateDates(
            LocalDate wef,
            LocalDate wet) {

        if (wef == null || wet == null) {

            throw new IllegalArgumentException(
                    "WEF and WET are required.");
        }

        if (wef.isAfter(wet)) {

            throw new IllegalArgumentException(
                    "WEF cannot be greater than WET.");
        }
    }

    // =========================================================
    // SUPER ADMIN VALIDATION
    // =========================================================

    private void validateSuperAdmin() {

        boolean isSuperAdmin =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                "SUPER_ADMIN".equals(
                                        authority.getAuthority()));

        if (!isSuperAdmin) {

            throw new ResourceNotFoundException(
                    "You are not authorized to perform this action.");
        }
    }

    // =========================================================
    // GET CURRENT ROLE
    // =========================================================

    private String getCurrentRole() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientContractDocumentResponse getDocumentData(Long documentId) {

        ClientContractDocument document =
                clientContractDocumentRepository.findById(documentId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Contract document not found with id : "
                                        + documentId));

        ClientContractDocumentResponse response =
                new ClientContractDocumentResponse();

        response.setId(document.getId());
        response.setDocumentName(document.getDocumentName());
        response.setDocumentType(document.getDocumentType());
        response.setDocumentData(document.getDocumentData());

        return response;
    }
    }
