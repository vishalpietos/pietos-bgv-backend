package com.pietos.bgv.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pietos.bgv.dto.request.client.ClientPackageComponentRequest;
import com.pietos.bgv.dto.response.client.ClientPackageComponentResponse;
import com.pietos.bgv.entity.ClientComponent;
import com.pietos.bgv.entity.ClientPackage;
import com.pietos.bgv.entity.ClientPackageComponent;
import com.pietos.bgv.entity.Component;
import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.exception.DuplicateResourceException;
import com.pietos.bgv.exception.ResourceNotFoundException;
import com.pietos.bgv.repository.ClientComponentRepository;
import com.pietos.bgv.repository.ClientPackageComponentRepository;
import com.pietos.bgv.repository.ClientPackageRepository;
import com.pietos.bgv.repository.ComponentRepository;
import com.pietos.bgv.security.LoggedInUserService;
import com.pietos.bgv.service.ClientPackageComponentService;

@Service
@Transactional
public class ClientPackageComponentServiceImpl
        implements ClientPackageComponentService {

    private final ClientPackageComponentRepository clientPackageComponentRepository;

    private final ClientPackageRepository clientPackageRepository;

    private final ComponentRepository componentRepository;

    private final ClientComponentRepository clientComponentRepository;

    private final LoggedInUserService loggedInUserService;

    public ClientPackageComponentServiceImpl(
            ClientPackageComponentRepository clientPackageComponentRepository,
            ClientPackageRepository clientPackageRepository,
            ComponentRepository componentRepository,
            ClientComponentRepository clientComponentRepository,
            LoggedInUserService loggedInUserService) {

        this.clientPackageComponentRepository =
                clientPackageComponentRepository;

        this.clientPackageRepository =
                clientPackageRepository;

        this.componentRepository =
                componentRepository;

        this.clientComponentRepository =
                clientComponentRepository;

        this.loggedInUserService =
                loggedInUserService;
    }

    // =========================================================
    // ADD COMPONENT TO PACKAGE
    // =========================================================

    @Override
    public ClientPackageComponentResponse addComponent(
            ClientPackageComponentRequest request) {

        // -----------------------------------------------------
        // LOGGED-IN USER
        // -----------------------------------------------------

        SystemUser loggedInUser = loggedInUserService.getLoggedInUser();

        // -----------------------------------------------------
        // VALIDATE PACKAGE ID
        // -----------------------------------------------------

        if (request.getClientPackageId() == null) {

            throw new IllegalArgumentException(
                    "Client package id is required.");
        }

        // -----------------------------------------------------
        // FIND ACTIVE PACKAGE
        // -----------------------------------------------------

        ClientPackage clientPackage =
                clientPackageRepository
                        .findById(request.getClientPackageId())
                        .filter(ClientPackage::getIsActive)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Active client package not found with id : "
                                                + request.getClientPackageId()));

        // -----------------------------------------------------
        // VALIDATE COMPONENT ID
        // -----------------------------------------------------

        if (request.getComponentId() == null) {

            throw new IllegalArgumentException(
                    "Component id is required.");
        }

        // -----------------------------------------------------
        // FIND COMPONENT
        // -----------------------------------------------------

        Component component =
                componentRepository
                        .findById(request.getComponentId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Component not found with id : "
                                                + request.getComponentId()));

        // -----------------------------------------------------
        // CHECK COMPONENT ACTIVE
        // -----------------------------------------------------

        if (!Boolean.TRUE.equals(
                component.getIsActive())) {

            throw new ResourceNotFoundException(
                    "Component is inactive with id : "
                            + request.getComponentId());
        }

       

     // REMOVE THIS
        boolean alreadyExists =
                clientPackageComponentRepository
                        .existsByClientPackageIdAndComponentIdAndIsActiveTrue(
                                clientPackage.getId(),
                                request.getComponentId());

        if (alreadyExists) {
            throw new DuplicateResourceException(
                    "Component is already active in this package.");
        }

        // -----------------------------------------------------
        // CREATE PACKAGE COMPONENT
        // -----------------------------------------------------

        ClientPackageComponent packageComponent =  new ClientPackageComponent();

        packageComponent.setClientPackage(clientPackage);

        packageComponent.setComponent(component);

        packageComponent.setIsActive(true);

        packageComponent.setCreatedBy(loggedInUser);

        packageComponent.setUpdatedBy(loggedInUser);

        packageComponent.setCreatedAt(
                LocalDateTime.now());

        packageComponent.setUpdatedAt(
                LocalDateTime.now());
        
        LocalDate effectiveFrom = calculateEffectiveFrom();

        packageComponent.setEffectiveFrom(effectiveFrom);
        
        Integer componentTatDays = request.getTat();

        if (componentTatDays == null || componentTatDays <= 0) {
            throw new IllegalArgumentException(
                    "Component TAT is required.");
        }

        packageComponent.setTatDays(componentTatDays);

        // -----------------------------------------------------
        // SAVE
        // -----------------------------------------------------

        ClientPackageComponent saved =clientPackageComponentRepository .save(packageComponent);

        // -----------------------------------------------------
        // RESPONSE
        // -----------------------------------------------------

        return mapToResponse(saved);
    }

    // =========================================================
    // GET COMPONENTS BY PACKAGE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<ClientPackageComponentResponse>
            getComponentsByPackageId(Long packageId) {

        // -----------------------------------------------------
        // VALIDATE PACKAGE
        // -----------------------------------------------------

        clientPackageRepository
                .findById(packageId)
                .filter(ClientPackage::getIsActive)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Active client package not found with id : "
                                        + packageId));

        // -----------------------------------------------------
        // GET ACTIVE COMPONENTS
        // -----------------------------------------------------

        List<ClientPackageComponent> components =
                clientPackageComponentRepository
                        .findByClientPackageIdAndIsActiveTrue(
                                packageId);

        // -----------------------------------------------------
        // MAP RESPONSE
        // -----------------------------------------------------

        return components.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================================================
    // DEACTIVATE COMPONENT
    // =========================================================

    @Override
    public void deactivateComponent(Long id) {

        // -----------------------------------------------------
        // LOGGED-IN USER
        // -----------------------------------------------------

        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();

        // -----------------------------------------------------
        // FIND PACKAGE COMPONENT
        // -----------------------------------------------------

        ClientPackageComponent packageComponent =
                clientPackageComponentRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Package component not found with id : "
                                                + id));

        // -----------------------------------------------------
        // CHECK ACTIVE
        // -----------------------------------------------------

        if (!Boolean.TRUE.equals(
                packageComponent.getIsActive())) {

            throw new IllegalArgumentException(
                    "Package component is already inactive.");
        }

        // -----------------------------------------------------
        // SOFT DELETE
        // -----------------------------------------------------

        packageComponent.setIsActive(false);

        packageComponent.setUpdatedBy(
                loggedInUser);

        packageComponent.setUpdatedAt(
                LocalDateTime.now());

        clientPackageComponentRepository.save(
                packageComponent);
    }

    // =========================================================
    // MAP ENTITY → RESPONSE
    // =========================================================

    private ClientPackageComponentResponse mapToResponse(
            ClientPackageComponent entity) {

        ClientPackageComponentResponse response =
                new ClientPackageComponentResponse();

        response.setId(
                entity.getId());

        response.setPackageId(
                entity.getClientPackage()
                        .getId());

        response.setComponentId(
                entity.getComponent()
                        .getId());

        response.setComponentName(
                entity.getComponent()
                        .getComponentName());

        response.setIsActive(
                entity.getIsActive());

        return response;
    }
    
    private LocalDate calculateEffectiveFrom() {

        LocalDateTime now = LocalDateTime.now();

        LocalDate effectiveFrom = now.toLocalDate();

        // If package is created at or after 4:30 PM,
        // TAT calculation starts from the next day
        if (!now.toLocalTime().isBefore(
                java.time.LocalTime.of(16, 30))) {

            effectiveFrom = effectiveFrom.plusDays(1);
        }

        return effectiveFrom;
    }
}