package com.pietos.bgv.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pietos.bgv.dto.request.client.ClientComponentTatRequest;
import com.pietos.bgv.dto.request.client.ComponentTatDetailRequest;
import com.pietos.bgv.dto.response.client.ClientComponentTatResponse;
import com.pietos.bgv.dto.response.client.ComponentTatResponse;
import com.pietos.bgv.entity.ClientComponentTat;
import com.pietos.bgv.entity.ClientInformation;
import com.pietos.bgv.entity.ClientPackageComponent;
import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.exception.DuplicateResourceException;
import com.pietos.bgv.exception.ResourceNotFoundException;
import com.pietos.bgv.repository.ClientComponentTatRepository;
import com.pietos.bgv.repository.ClientInformationRepository;
import com.pietos.bgv.repository.ClientPackageComponentRepository;
import com.pietos.bgv.security.LoggedInUserService;
import com.pietos.bgv.service.ClientComponentTatService;

@Service
@Transactional
public class ClientComponentTatServiceImpl
        implements ClientComponentTatService {

    private final ClientComponentTatRepository clientComponentTatRepository;

    private final ClientPackageComponentRepository clientPackageComponentRepository;

    private final ClientInformationRepository clientInformationRepository;

    private final LoggedInUserService loggedInUserService;


    public ClientComponentTatServiceImpl(
            ClientComponentTatRepository clientComponentTatRepository,
            ClientPackageComponentRepository clientPackageComponentRepository,
            ClientInformationRepository clientInformationRepository,
            LoggedInUserService loggedInUserService) {

        this.clientComponentTatRepository =
                clientComponentTatRepository;

        this.clientPackageComponentRepository =
                clientPackageComponentRepository;

        this.clientInformationRepository =
                clientInformationRepository;

        this.loggedInUserService =
                loggedInUserService;
    }


    // =========================================================
    // SAVE / UPDATE TAT
    // PACKAGE + COMPONENT WISE
    // =========================================================

    @Override
    public List<ClientComponentTatResponse>
    saveOrUpdateClientComponentTat(
            ClientComponentTatRequest request) {

        // =====================================================
        // LOGGED-IN USER
        // =====================================================

        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();


        // =====================================================
        // CHECK ROLE
        // =====================================================

        checkSuperAdmin(
                "You are not authorized to configure Component TAT.");


        // =====================================================
        // VALIDATE CLIENT
        // =====================================================

        ClientInformation client =
                clientInformationRepository
                        .findById(request.getClientId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Client not found with id : "
                                                + request.getClientId()));


        // =====================================================
        // VALIDATE PACKAGE
        // =====================================================

        if (request.getPackageId() == null) {

            throw new IllegalArgumentException(
                    "Package is required.");
        }


        // =====================================================
        // VALIDATE DATES
        // =====================================================

        LocalDate effectiveFrom =
                request.getEffectiveFrom();

        LocalDate effectiveTo =
                request.getEffectiveTo();

        validateDates(
                effectiveFrom,
                effectiveTo);


        // =====================================================
        // VALIDATE COMPONENT LIST
        // =====================================================

        if (request.getComponents() == null
                || request.getComponents().isEmpty()) {

            throw new IllegalArgumentException(
                    "Please select at least one component.");
        }


        // =====================================================
        // PROCESS EACH COMPONENT
        // =====================================================

        for (ComponentTatDetailRequest componentRequest :
                request.getComponents()) {

            // =================================================
            // VALIDATE COMPONENT ID
            // =================================================

            if (componentRequest.getComponentId() == null) {

                throw new IllegalArgumentException(
                        "Component is required.");
            }


            // =================================================
            // VALIDATE TAT DAYS
            // =================================================

            validateTatDays(
                    componentRequest.getTatDays());


            // =================================================
            // FIND PACKAGE COMPONENT
            // =================================================

            ClientPackageComponent packageComponent =
                    clientPackageComponentRepository
                            .findByClientPackageIdAndComponentIdAndIsActiveTrue(
                                    request.getPackageId(),
                                    componentRequest.getComponentId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Component is not assigned to this package."));


            // =================================================
            // VALIDATE PACKAGE BELONGS TO CLIENT
            // =================================================

            if (packageComponent
                    .getClientPackage()
                    .getClientInformation()
                    .getId()
                    .longValue()
                    != request.getClientId().longValue()) {

                throw new ResourceNotFoundException(
                        "Package does not belong to the selected client.");
            }


            // =================================================
            // FIND EXISTING TAT
            //
            // Same:
            // Package + Component + WEF + WET
            // =================================================

            Optional<ClientComponentTat> existingTat =
                    clientComponentTatRepository
                            .findByClientPackageComponentClientPackageIdAndClientPackageComponentComponentIdAndEffectiveFromAndEffectiveToAndIsActiveTrue(
                                    request.getPackageId(),
                                    componentRequest.getComponentId(),
                                    effectiveFrom,
                                    effectiveTo);


            // =================================================
            // EXISTING → UPDATE
            // =================================================

            if (existingTat.isPresent()) {

                ClientComponentTat tat =
                        existingTat.get();

                tat.setTatDays(
                        componentRequest.getTatDays());

                tat.setUpdatedBy(
                        loggedInUser);

                tat.setUpdatedAt(
                        LocalDateTime.now());

                tat.setIsActive(true);

                clientComponentTatRepository.save(tat);
            }


            // =================================================
            // NEW → CREATE
            // =================================================

            else {

                checkOverlap(
                        request.getPackageId(),
                        componentRequest.getComponentId(),
                        effectiveFrom,
                        effectiveTo,
                        packageComponent);


                ClientComponentTat tat =
                        new ClientComponentTat();


                tat.setClientPackageComponent(
                        packageComponent);


                tat.setEffectiveFrom(
                        effectiveFrom);


                tat.setEffectiveTo(
                        effectiveTo);


                tat.setTatDays(
                        componentRequest.getTatDays());


                tat.setIsActive(true);


                tat.setCreatedBy(
                        loggedInUser);


                tat.setUpdatedBy(
                        loggedInUser);


                tat.setCreatedAt(
                        LocalDateTime.now());


                tat.setUpdatedAt(
                        LocalDateTime.now());


                clientComponentTatRepository.save(tat);
            }
        }


        // =====================================================
        // RETURN FRESH DATA
        // =====================================================

        return getTatByClientId(
                client.getId());
    }


    // =========================================================
    // UPDATE SINGLE TAT
    // =========================================================

    @Override
    public List<ClientComponentTatResponse>
    updateClientComponentTat(
            Long id,
            ClientComponentTatRequest request) {

        // =====================================================
        // LOGGED-IN USER
        // =====================================================

        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();


        // =====================================================
        // CHECK ROLE
        // =====================================================

        checkSuperAdmin(
                "You are not authorized to update Component TAT.");


        // =====================================================
        // FIND EXISTING TAT
        // =====================================================

        ClientComponentTat tat =
                clientComponentTatRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Component TAT not found with id : "
                                                + id));


        // =====================================================
        // CHECK ACTIVE
        // =====================================================

        if (!Boolean.TRUE.equals(
                tat.getIsActive())) {

            throw new ResourceNotFoundException(
                    "Cannot update inactive Component TAT.");
        }


        // =====================================================
        // VALIDATE REQUEST
        // =====================================================

        if (request.getComponents() == null
                || request.getComponents().isEmpty()) {

            throw new IllegalArgumentException(
                    "Please select at least one component.");
        }


        // =====================================================
        // VALIDATE DATES
        // =====================================================

        LocalDate effectiveFrom =
                request.getEffectiveFrom();

        LocalDate effectiveTo =
                request.getEffectiveTo();

        validateDates(
                effectiveFrom,
                effectiveTo);


        // =====================================================
        // GET EXISTING PACKAGE COMPONENT
        // =====================================================

        ClientPackageComponent packageComponent =
                tat.getClientPackageComponent();


        if (packageComponent == null) {

            throw new ResourceNotFoundException(
                    "Package component not found.");
        }


        // =====================================================
        // GET PACKAGE
        // =====================================================

        Long packageId =
                packageComponent
                        .getClientPackage()
                        .getId();


        // =====================================================
        // GET CLIENT
        // =====================================================

        Long clientId =
                packageComponent
                        .getClientPackage()
                        .getClientInformation()
                        .getId();


        // =====================================================
        // GET COMPONENT
        // =====================================================

        Long componentId =
                packageComponent
                        .getComponent()
                        .getId();


        // =====================================================
        // VALIDATE REQUEST CLIENT
        // =====================================================

        if (request.getClientId() == null
                || !clientId.equals(
                        request.getClientId())) {

            throw new IllegalArgumentException(
                    "Selected client does not match the existing TAT.");
        }


        // =====================================================
        // VALIDATE REQUEST PACKAGE
        // =====================================================

        if (request.getPackageId() == null
                || !packageId.equals(
                        request.getPackageId())) {

            throw new IllegalArgumentException(
                    "Selected package does not match the existing TAT.");
        }


        // =====================================================
        // FIND COMPONENT REQUEST
        // =====================================================

        ComponentTatDetailRequest componentRequest =
                request.getComponents()
                        .stream()
                        .filter(item ->
                                componentId.equals(
                                        item.getComponentId()))
                        .findFirst()
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Component is required for update."));


        // =====================================================
        // VALIDATE TAT DAYS
        // =====================================================

        validateTatDays(
                componentRequest.getTatDays());


        // =====================================================
        // CHECK OVERLAP
        // EXCLUDE CURRENT TAT
        // =====================================================

        boolean overlapping;

        if (effectiveTo == null) {

            overlapping =
                    clientComponentTatRepository
                            .existsOverlappingTatWithoutEndDateForUpdate(
                                    id,
                                    packageId,
                                    componentId,
                                    effectiveFrom);

        } else {

            overlapping =
                    clientComponentTatRepository
                            .existsOverlappingTatForUpdate(
                                    id,
                                    packageId,
                                    componentId,
                                    effectiveFrom,
                                    effectiveTo);
        }


        if (overlapping) {

            throw new DuplicateResourceException(
                    "TAT period overlaps with an existing active TAT for component : "
                            + packageComponent
                                    .getComponent()
                                    .getComponentName());
        }


        // =====================================================
        // UPDATE
        // =====================================================

        tat.setEffectiveFrom(
                effectiveFrom);

        tat.setEffectiveTo(
                effectiveTo);

        tat.setTatDays(
                componentRequest.getTatDays());

        tat.setUpdatedBy(
                loggedInUser);

        tat.setUpdatedAt(
                LocalDateTime.now());


        clientComponentTatRepository.save(tat);


        // =====================================================
        // RETURN FRESH DATA
        // =====================================================

        return getTatByClientId(
                clientId);
    }


    // =========================================================
    // GET TAT BY CLIENT
    // =========================================================

    @Override
    public List<ClientComponentTatResponse>
    getTatByClientId(
            Long clientId) {

        // =====================================================
        // VALIDATE CLIENT
        // =====================================================

        ClientInformation client =
                clientInformationRepository
                        .findById(clientId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Client not found with id : "
                                                + clientId));


        // =====================================================
        // GET ALL ACTIVE TAT
        // =====================================================

        List<ClientComponentTat> tats =
                clientComponentTatRepository
                        .findAll()
                        .stream()
                        .filter(tat ->
                                Boolean.TRUE.equals(
                                        tat.getIsActive()))
                        .filter(tat ->
                                tat.getClientPackageComponent() != null)
                        .filter(tat ->
                                tat.getClientPackageComponent()
                                        .getClientPackage()
                                        .getClientInformation()
                                        .getId()
                                        .equals(clientId))
                        .toList();


        // =====================================================
        // NO TAT
        // =====================================================

        if (tats.isEmpty()) {

            return List.of();
        }


        // =====================================================
        // GROUP BY PACKAGE + WEF + WET
        //
        // This is important because the same client can have
        // multiple packages.
        // =====================================================

        Map<String, List<ClientComponentTat>> groupedTats =
                tats.stream()
                        .collect(
                                Collectors.groupingBy(
                                        tat ->
                                                tat.getClientPackageComponent()
                                                        .getClientPackage()
                                                        .getId()
                                                        + "_"
                                                        + String.valueOf(
                                                                tat.getEffectiveFrom())
                                                        + "_"
                                                        + String.valueOf(
                                                                tat.getEffectiveTo()),
                                        LinkedHashMap::new,
                                        Collectors.toList()
                                )
                        );


        // =====================================================
        // MAP GROUPS TO RESPONSE
        // =====================================================

        return groupedTats.values()
                .stream()
                .map(group ->
                        mapToResponse(
                                client,
                                group))
                .toList();
    }


    // =========================================================
    // DELETE / DEACTIVATE TAT
    // =========================================================

    @Override
    public void deleteTatById(
            Long id) {

        // =====================================================
        // LOGGED-IN USER
        // =====================================================

        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();


        // =====================================================
        // CHECK ROLE
        // =====================================================

        checkSuperAdmin(
                "You are not authorized to delete Component TAT.");


        // =====================================================
        // FIND TAT
        // =====================================================

        ClientComponentTat tat =
                clientComponentTatRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Component TAT not found with id : "
                                                + id));


        // =====================================================
        // CHECK ACTIVE
        // =====================================================

        if (!Boolean.TRUE.equals(
                tat.getIsActive())) {

            throw new ResourceNotFoundException(
                    "Component TAT is already inactive.");
        }


        // =====================================================
        // SOFT DELETE
        // =====================================================

        tat.setIsActive(false);

        tat.setUpdatedBy(
                loggedInUser);

        tat.setUpdatedAt(
                LocalDateTime.now());


        clientComponentTatRepository.save(tat);
    }


    // =========================================================
    // MAP GROUP → RESPONSE
    // =========================================================

    private ClientComponentTatResponse mapToResponse(
            ClientInformation client,
            List<ClientComponentTat> tats) {

        ClientComponentTatResponse response =
                new ClientComponentTatResponse();


        // =====================================================
        // CLIENT
        // =====================================================

        response.setClientId(
                client.getId());

        response.setClientName(
                client.getClientName());


        // =====================================================
        // EMPTY GROUP
        // =====================================================

        if (tats == null
                || tats.isEmpty()) {

            response.setEffectiveFrom(null);

            response.setEffectiveTo(null);

            response.setComponents(
                    List.of());

            return response;
        }


        // =====================================================
        // EFFECTIVE DATES
        // =====================================================

        ClientComponentTat firstTat =
                tats.get(0);


        response.setEffectiveFrom(
                firstTat.getEffectiveFrom());


        response.setEffectiveTo(
                firstTat.getEffectiveTo());


        // =====================================================
        // COMPONENTS
        // =====================================================

        List<ComponentTatResponse> componentResponses =
                tats.stream()
                        .map(this::mapComponentToResponse)
                        .toList();


        response.setComponents(
                componentResponses);


        return response;
    }


    // =========================================================
    // MAP TAT → COMPONENT RESPONSE
    // =========================================================

    private ComponentTatResponse mapComponentToResponse(
            ClientComponentTat tat) {

        ComponentTatResponse response =
                new ComponentTatResponse();


        response.setId(
                tat.getId());


        response.setComponentId(
                tat.getClientPackageComponent()
                        .getComponent()
                        .getId());


        response.setComponentName(
                tat.getClientPackageComponent()
                        .getComponent()
                        .getComponentName());


        response.setTatDays(
                tat.getTatDays());


        return response;
    }


    // =========================================================
    // CHECK ROLE
    // =========================================================

    private void checkSuperAdmin(
            String message) {

        String roleName =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElse(null);


        if (!"SUPER_ADMIN".equals(
                roleName)) {

            throw new ResourceNotFoundException(
                    message);
        }
    }


    // =========================================================
    // VALIDATE DATES
    // =========================================================

    private void validateDates(
            LocalDate effectiveFrom,
            LocalDate effectiveTo) {

        if (effectiveFrom == null) {

            throw new IllegalArgumentException(
                    "Effective From date is required.");
        }


        if (effectiveTo != null
                && effectiveFrom.isAfter(
                        effectiveTo)) {

            throw new IllegalArgumentException(
                    "Effective From cannot be later than Effective To.");
        }
    }


    // =========================================================
    // VALIDATE TAT DAYS
    // =========================================================

    private void validateTatDays(
            Integer tatDays) {

        if (tatDays == null) {

            throw new IllegalArgumentException(
                    "TAT days are required.");
        }


        if (tatDays < 0) {

            throw new IllegalArgumentException(
                    "TAT days cannot be negative.");
        }
    }


    // =========================================================
    // CHECK OVERLAPPING TAT
    // =========================================================

    private void checkOverlap(
            Long packageId,
            Long componentId,
            LocalDate effectiveFrom,
            LocalDate effectiveTo,
            ClientPackageComponent packageComponent) {

        boolean overlapping;


        if (effectiveTo == null) {

            overlapping =
                    clientComponentTatRepository
                            .existsOverlappingTatWithoutEndDate(
                                    packageId,
                                    componentId,
                                    effectiveFrom);

        } else {

            overlapping =
                    clientComponentTatRepository
                            .existsOverlappingTat(
                                    packageId,
                                    componentId,
                                    effectiveFrom,
                                    effectiveTo);
        }


        if (overlapping) {

            throw new DuplicateResourceException(
                    "TAT period overlaps with an existing active TAT for component : "
                            + packageComponent
                                    .getComponent()
                                    .getComponentName());
        }
    }
}