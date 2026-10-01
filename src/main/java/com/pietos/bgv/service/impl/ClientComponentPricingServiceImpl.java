package com.pietos.bgv.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pietos.bgv.dto.request.client.ClientComponentPricingItemRequest;
import com.pietos.bgv.dto.request.client.ClientComponentPricingRequest;
import com.pietos.bgv.dto.response.client.ClientComponentPricingResponse;
import com.pietos.bgv.entity.ClientComponentPricing;
import com.pietos.bgv.entity.ClientPackage;
import com.pietos.bgv.entity.ClientPackageComponent;
import com.pietos.bgv.entity.Component;
import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.exception.DuplicateResourceException;
import com.pietos.bgv.exception.ResourceNotFoundException;
import com.pietos.bgv.repository.ClientComponentPricingRepository;
import com.pietos.bgv.repository.ClientPackageComponentRepository;
import com.pietos.bgv.repository.ClientPackageRepository;
import com.pietos.bgv.security.LoggedInUserService;
import com.pietos.bgv.service.ClientComponentPricingService;

@Service
public class ClientComponentPricingServiceImpl
        implements ClientComponentPricingService {

    private final ClientComponentPricingRepository clientComponentPricingRepository;

    private final ClientPackageRepository clientPackageRepository;

    private final ClientPackageComponentRepository clientPackageComponentRepository;

    private final LoggedInUserService loggedInUserService;

    public ClientComponentPricingServiceImpl(
            ClientComponentPricingRepository clientComponentPricingRepository,
            ClientPackageRepository clientPackageRepository,
            ClientPackageComponentRepository clientPackageComponentRepository,
            LoggedInUserService loggedInUserService) {

        this.clientComponentPricingRepository =
                clientComponentPricingRepository;

        this.clientPackageRepository =
                clientPackageRepository;

        this.clientPackageComponentRepository =
                clientPackageComponentRepository;

        this.loggedInUserService =
                loggedInUserService;
    }

    // =========================================================
    // CREATE PRICING
    // =========================================================

    @Transactional
    @Override
    public ClientComponentPricingResponse createPricing(
            ClientComponentPricingRequest request) {

        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();

        // =====================================================
        // VALIDATE PACKAGE
        // =====================================================

        if (request.getClientPackageId() == null) {

            throw new IllegalArgumentException(
                    "Client package id is required.");
        }

        ClientPackage clientPackage =
                clientPackageRepository
                        .findById(request.getClientPackageId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Client package not found with id : "
                                                + request.getClientPackageId()));

        if (!Boolean.TRUE.equals(
                clientPackage.getIsActive())) {

            throw new ResourceNotFoundException(
                    "Client package is inactive.");
        }

        // =====================================================
        // VALIDATE DATES
        // W.E.F <= W.E.T
        // =====================================================

        validateDates(
                request.getEffectiveFrom(),
                request.getEffectiveTo());

        // =====================================================
        // VALIDATE COMPONENTS
        // =====================================================

        if (request.getComponents() == null
                || request.getComponents().isEmpty()) {

            throw new IllegalArgumentException(
                    "Please select at least one component.");
        }

        ClientComponentPricing lastSavedPricing = null;

        // =====================================================
        // PROCESS COMPONENTS
        // =====================================================

        for (ClientComponentPricingItemRequest item :
                request.getComponents()) {

            // -------------------------------------------------
            // VALIDATE PACKAGE COMPONENT ID
            // -------------------------------------------------

            if (item.getClientPackageComponentId() == null) {

                throw new IllegalArgumentException(
                        "Client package component id is required.");
            }

            // -------------------------------------------------
            // VALIDATE PRICE
            // -------------------------------------------------

            if (item.getPrice() == null) {

                throw new IllegalArgumentException(
                        "Price is required.");
            }

            if (item.getPrice().signum() < 0) {

                throw new IllegalArgumentException(
                        "Price cannot be negative.");
            }

            // =================================================
            // FIND EXACT PACKAGE COMPONENT / CHECK
            // =================================================

            ClientPackageComponent packageComponent =
                    clientPackageComponentRepository
                            .findByIdAndIsActiveTrue(
                                    item.getClientPackageComponentId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Active package component not found with id : "
                                                    + item.getClientPackageComponentId()));

            // =================================================
            // VERIFY PACKAGE BELONGS TO REQUESTED PACKAGE
            // =================================================

            if (!packageComponent.getClientPackage()
                    .getId()
                    .equals(clientPackage.getId())) {

                throw new ResourceNotFoundException(
                        "Package component does not belong to this client package.");
            }

            // =================================================
            // GET COMPONENT
            // =================================================

            Component component =
                    packageComponent.getComponent();

            if (component == null) {

                throw new ResourceNotFoundException(
                        "Component not found for package component id : "
                                + packageComponent.getId());
            }

            // =================================================
            // MASTER COMPONENT ACTIVE CHECK
            // =================================================

            if (!Boolean.TRUE.equals(
                    component.getIsActive())) {

                throw new ResourceNotFoundException(
                        "Component is inactive with id : "
                                + component.getId());
            }

            // =================================================
            // CHECK DUPLICATE ACTIVE PRICING
            // FOR EXACT CHECK
            // =================================================

            boolean overlapping =
                    hasOverlappingPricing(
                            packageComponent.getId(),
                            request.getEffectiveFrom(),
                            request.getEffectiveTo(),
                            null);

            if (overlapping) {

                throw new DuplicateResourceException(
                        "Pricing period overlaps with existing "
                                + "active pricing for "
                                + component.getComponentName()
                                + " check.");
            }

            // =================================================
            // CREATE PRICING
            // =================================================

            ClientComponentPricing pricing =
                    new ClientComponentPricing();

            pricing.setClientPackage(
                    clientPackage);

            pricing.setClientPackageComponent(
                    packageComponent);

            pricing.setComponent(
                    component);

            pricing.setPrice(
                    item.getPrice());

            pricing.setEffectiveFrom(
                    request.getEffectiveFrom());

            pricing.setEffectiveTo(
                    request.getEffectiveTo());

            pricing.setIsActive(true);

            pricing.setCreatedBy(
                    loggedInUser);

            pricing.setUpdatedBy(
                    loggedInUser);

            LocalDateTime now =
                    LocalDateTime.now();

            pricing.setCreatedAt(now);
            pricing.setUpdatedAt(now);

            lastSavedPricing =
                    clientComponentPricingRepository.save(
                            pricing);
        }

        return mapToResponse(lastSavedPricing);
    }

    // =========================================================
    // GET ALL PRICING
    // =========================================================

    @Override
    public List<ClientComponentPricingResponse> getPricing() {

        return clientComponentPricingRepository
                .findAll()
                .stream()
                .filter(pricing ->
                        Boolean.TRUE.equals(
                                pricing.getIsActive()))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================================================
    // GET PRICING BY ID
    // =========================================================

    @Override
    public ClientComponentPricingResponse getPricingById(
            Long id) {

        ClientComponentPricing pricing =
                clientComponentPricingRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Pricing not found with id : "
                                                + id));

        if (!Boolean.TRUE.equals(
                pricing.getIsActive())) {

            throw new ResourceNotFoundException(
                    "Pricing is inactive with id : "
                            + id);
        }

        return mapToResponse(pricing);
    }

    // =========================================================
    // GET PRICING BY CLIENT
    // =========================================================

    @Override
    public List<ClientComponentPricingResponse> getPricingByClientId(
            Long clientId) {

        return clientComponentPricingRepository
                .findAll()
                .stream()
                .filter(pricing ->
                        Boolean.TRUE.equals(
                                pricing.getIsActive()))
                .filter(pricing ->
                        pricing.getClientPackage() != null)
                .filter(pricing ->
                        pricing.getClientPackage()
                                .getClientInformation() != null)
                .filter(pricing ->
                        pricing.getClientPackage()
                                .getClientInformation()
                                .getId()
                                .equals(clientId))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================================================
    // UPDATE PRICING
    // =========================================================

    @Transactional
    @Override
    public ClientComponentPricingResponse updatePricing(
            Long id,
            ClientComponentPricingRequest request) {

        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();

        // =====================================================
        // FIND EXISTING PRICING
        // =====================================================

        ClientComponentPricing pricing =
                clientComponentPricingRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Pricing not found with id : "
                                                + id));

        if (!Boolean.TRUE.equals(
                pricing.getIsActive())) {

            throw new ResourceNotFoundException(
                    "Cannot update inactive pricing.");
        }

        // =====================================================
        // VALIDATE PACKAGE
        // =====================================================

        if (request.getClientPackageId() == null) {

            throw new IllegalArgumentException(
                    "Client package id is required.");
        }

        ClientPackage clientPackage =
                clientPackageRepository
                        .findById(request.getClientPackageId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Client package not found with id : "
                                                + request.getClientPackageId()));

        if (!Boolean.TRUE.equals(
                clientPackage.getIsActive())) {

            throw new ResourceNotFoundException(
                    "Client package is inactive.");
        }

        // =====================================================
        // VALIDATE DATES
        // =====================================================

        validateDates(
                request.getEffectiveFrom(),
                request.getEffectiveTo());

        // =====================================================
        // VALIDATE COMPONENT
        // =====================================================

        if (request.getComponents() == null
                || request.getComponents().isEmpty()) {

            throw new IllegalArgumentException(
                    "Please select at least one component.");
        }

        // This endpoint updates one pricing record
        ClientComponentPricingItemRequest item =
                request.getComponents().get(0);

        if (item.getClientPackageComponentId() == null) {

            throw new IllegalArgumentException(
                    "Client package component id is required.");
        }

        if (item.getPrice() == null) {

            throw new IllegalArgumentException(
                    "Price is required.");
        }

        if (item.getPrice().signum() < 0) {

            throw new IllegalArgumentException(
                    "Price cannot be negative.");
        }

        // =====================================================
        // FIND EXACT PACKAGE COMPONENT
        // =====================================================

        ClientPackageComponent packageComponent =
                clientPackageComponentRepository
                        .findByIdAndIsActiveTrue(
                                item.getClientPackageComponentId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Active package component not found with id : "
                                                + item.getClientPackageComponentId()));

        // =====================================================
        // VERIFY PACKAGE
        // =====================================================

        if (!packageComponent.getClientPackage()
                .getId()
                .equals(clientPackage.getId())) {

            throw new ResourceNotFoundException(
                    "Package component does not belong to this client package.");
        }

        // =====================================================
        // GET COMPONENT
        // =====================================================

        Component component =
                packageComponent.getComponent();

        if (component == null) {

            throw new ResourceNotFoundException(
                    "Component not found.");
        }

        if (!Boolean.TRUE.equals(
                component.getIsActive())) {

            throw new ResourceNotFoundException(
                    "Component is inactive with id : "
                            + component.getId());
        }

        // =====================================================
        // CHECK OVERLAP
        // =====================================================

        boolean overlapping =
                hasOverlappingPricing(
                        packageComponent.getId(),
                        request.getEffectiveFrom(),
                        request.getEffectiveTo(),
                        id);

        if (overlapping) {

            throw new DuplicateResourceException(
                    "Pricing period overlaps with existing "
                            + "active pricing for this check.");
        }

        // =====================================================
        // UPDATE SAME PRICING ROW
        // =====================================================

        pricing.setClientPackage(
                clientPackage);

        pricing.setClientPackageComponent(
                packageComponent);

        pricing.setComponent(
                component);

        pricing.setPrice(
                item.getPrice());

        pricing.setEffectiveFrom(
                request.getEffectiveFrom());

        pricing.setEffectiveTo(
                request.getEffectiveTo());

        pricing.setIsActive(true);

        pricing.setUpdatedBy(
                loggedInUser);

        pricing.setUpdatedAt(
                LocalDateTime.now());

        ClientComponentPricing updatedPricing =
                clientComponentPricingRepository
                        .save(pricing);

        return mapToResponse(updatedPricing);
    }

    // =========================================================
    // DEACTIVATE PRICING
    // =========================================================

    @Override
    @Transactional
    public void deactivatePricing(Long id) {

        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();

        ClientComponentPricing pricing =
                clientComponentPricingRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Pricing not found with id : "
                                                + id));

        if (!Boolean.TRUE.equals(
                pricing.getIsActive())) {

            throw new IllegalArgumentException(
                    "Pricing is already inactive.");
        }

        pricing.setIsActive(false);

        pricing.setUpdatedBy(
                loggedInUser);

        pricing.setUpdatedAt(
                LocalDateTime.now());

        clientComponentPricingRepository.save(
                pricing);
    }

    // =========================================================
    // OVERLAP CHECK
    // =========================================================
    //
    // IMPORTANT:
    // Check by clientPackageComponentId,
    // NOT componentId.
    //
    // This allows:
    //
    // Education Check 1
    // Education Check 2
    //
    // to have independent pricing.
    // =========================================================

    private boolean hasOverlappingPricing(
            Long clientPackageComponentId,
            LocalDate newFrom,
            LocalDate newTo,
            Long excludePricingId) {

        List<ClientComponentPricing> existingPricing =
                clientComponentPricingRepository
                        .findAll()
                        .stream()
                        .filter(pricing ->
                                Boolean.TRUE.equals(
                                        pricing.getIsActive()))
                        .filter(pricing ->
                                pricing.getClientPackageComponent()
                                        != null)
                        .filter(pricing ->
                                clientPackageComponentId.equals(
                                        pricing
                                                .getClientPackageComponent()
                                                .getId()))
                        .filter(pricing ->
                                excludePricingId == null
                                        || !excludePricingId.equals(
                                                pricing.getId()))
                        .collect(Collectors.toList());

        for (ClientComponentPricing existing :
                existingPricing) {

            LocalDate existingFrom =
                    existing.getEffectiveFrom();

            LocalDate existingTo =
                    existing.getEffectiveTo();

            boolean startsBeforeExistingEnds =
                    existingTo == null
                            || !newFrom.isAfter(
                                    existingTo);

            boolean newEndsAfterExistingStarts =
                    newTo == null
                            || !newTo.isBefore(
                                    existingFrom);

            if (startsBeforeExistingEnds
                    && newEndsAfterExistingStarts) {

                return true;
            }
        }

        return false;
    }

    // =========================================================
    // DATE VALIDATION
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
    // MAP RESPONSE
    // =========================================================

    private ClientComponentPricingResponse mapToResponse(
            ClientComponentPricing pricing) {

        ClientComponentPricingResponse response =
                new ClientComponentPricingResponse();

        response.setId(
                pricing.getId());

        // -----------------------------------------------------
        // PACKAGE
        // -----------------------------------------------------

        response.setClientPackageId(
                pricing.getClientPackage() != null
                        ? pricing.getClientPackage().getId()
                        : null);

        // -----------------------------------------------------
        // EXACT CHECK
        // -----------------------------------------------------

        response.setClientPackageComponentId(
                pricing.getClientPackageComponent() != null
                        ? pricing
                                .getClientPackageComponent()
                                .getId()
                        : null);

        // -----------------------------------------------------
        // COMPONENT
        // -----------------------------------------------------

        response.setComponentId(
                pricing.getComponent() != null
                        ? pricing.getComponent().getId()
                        : null);

        response.setComponentName(
                pricing.getComponent() != null
                        ? pricing.getComponent()
                                .getComponentName()
                        : null);

        // -----------------------------------------------------
        // PRICE
        // -----------------------------------------------------

        response.setPrice(
                pricing.getPrice());

        // -----------------------------------------------------
        // DATES
        // -----------------------------------------------------

        response.setEffectiveFrom(
                pricing.getEffectiveFrom());

        response.setEffectiveTo(
                pricing.getEffectiveTo());

        // -----------------------------------------------------
        // STATUS
        // -----------------------------------------------------

        response.setIsActive(
                pricing.getIsActive());

        return response;
    }
}