package com.pietos.bgv.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pietos.bgv.dto.request.client.ClientPackageComponentRequest;
import com.pietos.bgv.dto.request.client.ClientPackageRequest;
import com.pietos.bgv.dto.response.client.ClientPackageComponentResponse;
import com.pietos.bgv.dto.response.client.ClientPackageResponse;
import com.pietos.bgv.entity.ClientComponent;
import com.pietos.bgv.entity.ClientComponentPricing;
import com.pietos.bgv.entity.ClientComponentTat;
import com.pietos.bgv.entity.ClientInformation;
import com.pietos.bgv.entity.ClientPackage;
import com.pietos.bgv.entity.ClientPackageComponent;

import com.pietos.bgv.entity.Component;
import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.enums.PackageType;
import com.pietos.bgv.exception.ResourceNotFoundException;
import com.pietos.bgv.repository.ClientComponentPricingRepository;
import com.pietos.bgv.repository.ClientComponentRepository;
import com.pietos.bgv.repository.ClientComponentTatRepository;
import com.pietos.bgv.repository.ClientInformationRepository;

import com.pietos.bgv.repository.ClientPackageComponentRepository;
import com.pietos.bgv.repository.ClientPackageRepository;
import com.pietos.bgv.repository.ComponentRepository;
import com.pietos.bgv.security.LoggedInUserService;
import com.pietos.bgv.service.ClientPackageService;
import com.pietos.bgv.service.TatDateCalculationService;

@Service
@Transactional
public class ClientPackageServiceImpl
        implements ClientPackageService {

    private final ClientPackageRepository clientPackageRepository;

    private final ClientPackageComponentRepository clientPackageComponentRepository;

    private final ClientInformationRepository clientInformationRepository;

    private final ComponentRepository componentRepository;

    private final LoggedInUserService loggedInUserService;
    
    private final TatDateCalculationService tatDateCalculationService;
    
    private final ClientComponentPricingRepository clientComponentPricingRepository;

    private final ClientComponentTatRepository clientComponentTatRepository;
    
    private final ClientComponentRepository clientComponentRepository;
    
   


    public ClientPackageServiceImpl(
            ClientPackageRepository clientPackageRepository,
            ClientInformationRepository clientInformationRepository,
            ComponentRepository componentRepository,
            ClientPackageComponentRepository clientPackageComponentRepository,
            LoggedInUserService loggedInUserService,
            TatDateCalculationService tatDateCalculationService,
            ClientComponentPricingRepository clientComponentPricingRepository,
            ClientComponentTatRepository clientComponentTatRepository,
            ClientComponentRepository clientComponentRepository) {
    
    	
    	
    	this.clientComponentRepository=
    			clientComponentRepository;
    	
        this.clientPackageRepository =
                clientPackageRepository;

        this.clientInformationRepository =
                clientInformationRepository;

        this.componentRepository =
                componentRepository;

        this.clientPackageComponentRepository =
                clientPackageComponentRepository;

        this.loggedInUserService =
                loggedInUserService;

        this.tatDateCalculationService =
                tatDateCalculationService;

        this.clientComponentPricingRepository =
                clientComponentPricingRepository;

        this.clientComponentTatRepository =
                clientComponentTatRepository;
    }


    // =========================================================
    // CREATE PACKAGE
    // =========================================================

    @Override
    @Transactional
    public ClientPackageResponse saveClientPackage(
            ClientPackageRequest request) {

        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();

        checkAdminRole(
                "You are not authorized to create client packages.");

        ClientInformation client =
                clientInformationRepository
                        .findById(request.getClientId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Client not found with id : "
                                                + request.getClientId()));

        // -----------------------------------------------------
        // VALIDATE PACKAGE TYPE
        // -----------------------------------------------------

        if (request.getPackageType() == null) {
            throw new IllegalArgumentException(
                    "Package type is required.");
        }

        String packageName = request.getPackageName();

        if (packageName == null || packageName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Package name is required.");
        }

        // -----------------------------------------------------
        // VALIDATE COMPONENTS
        // -----------------------------------------------------

        if (request.getComponents() == null
                || request.getComponents().isEmpty()) {

            throw new IllegalArgumentException(
                    "At least one component is required.");
        }

        Integer packageTat = request.getPackageTat();
        BigDecimal packageRate = request.getPackageRate();

        // -----------------------------------------------------
        // VALIDATE PACKAGE / INDIVIDUAL TYPE
        // -----------------------------------------------------

        if (PackageType.PACKAGE.equals(request.getPackageType())) {

            if (request.getPackageTat() == null
                    || request.getPackageTat() <= 0) {

                throw new IllegalArgumentException(
                        "Package TAT is required for package-wise pricing.");
            }

        } else if (PackageType.INDIVIDUAL.equals(
                request.getPackageType())) {

            packageTat = 0;
            packageRate = BigDecimal.ZERO;

            for (ClientPackageComponentRequest componentRequest
                    : request.getComponents()) {

                if (componentRequest.getTat() == null
                        || componentRequest.getTat() <= 0) {

                    throw new IllegalArgumentException(
                            "TAT is required for every component.");
                }

                if (componentRequest.getRatePerCheck() == null
                        || componentRequest.getRatePerCheck()
                            .compareTo(BigDecimal.ZERO) < 0) {

                    throw new IllegalArgumentException(
                            "Rate per check is required for every component.");
                }

                // Highest component TAT becomes package TAT
                packageTat = Math.max(
                        packageTat,
                        componentRequest.getTat());

                // Total component rates become package rate
                packageRate = packageRate.add(
                        componentRequest.getRatePerCheck());
            }
        }

        // -----------------------------------------------------
        // CREATED DATE
        // -----------------------------------------------------

        LocalDateTime createdAt = LocalDateTime.now();

        // -----------------------------------------------------
        // CREATE CLIENT PACKAGE
        // -----------------------------------------------------

        ClientPackage clientPackage = new ClientPackage();

        clientPackage.setClientInformation(client);

        clientPackage.setPackageName(packageName.trim());
        clientPackage.setPackageType(request.getPackageType());
        clientPackage.setPackageTat(packageTat);

        clientPackage.setPackageRate(packageRate);
        clientPackage.setInternalTat(request.getInternalTat());
        clientPackage.setHolidayType(request.getHolidayType());
        clientPackage.setEffectiveFrom(null);
        clientPackage.setEffectiveTo(null);
        clientPackage.setInternalEffectiveTo(null);
        clientPackage.setIsActive(true);
        clientPackage.setCreatedBy(loggedInUser);
        clientPackage.setUpdatedBy(loggedInUser);
        clientPackage.setCreatedAt(createdAt);
        clientPackage.setUpdatedAt(createdAt);
        

        // -----------------------------------------------------
        // SAVE PACKAGE
        // -----------------------------------------------------

        ClientPackage savedPackage = clientPackageRepository.save(clientPackage);

        // -----------------------------------------------------
        // CREATE PACKAGE COMPONENTS
        // -----------------------------------------------------

        List<ClientPackageComponent> packageComponents =
                new ArrayList<>();

        for (ClientPackageComponentRequest componentRequest
                : request.getComponents()) {

            if (componentRequest.getComponentId() == null) {

                throw new IllegalArgumentException(
                        "Component id is required.");
            }

            // -------------------------------------------------
            // FIND COMPONENT
            // -------------------------------------------------

            Component component =componentRepository
                            .findById(
                                    componentRequest.getComponentId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Component not found with id : "
                                                    + componentRequest
                                                            .getComponentId()));

            // -------------------------------------------------
            // VALIDATE COMPONENT ACTIVE
            // -------------------------------------------------

            if (!Boolean.TRUE.equals(
                    component.getIsActive())) {

                throw new ResourceNotFoundException(
                        "Component is inactive with id : "
                                + componentRequest.getComponentId());
            }

            // -------------------------------------------------
            // CREATE / ACTIVATE CLIENT COMPONENT
            // -------------------------------------------------

            Optional<ClientComponent> existingClientComponent =
                    clientComponentRepository
                            .findByClientInformationIdAndComponentId(
                                    client.getId(),
                                    component.getId());

            if (existingClientComponent.isPresent()) {
                ClientComponent clientComponent = existingClientComponent.get();

                clientComponent.setIsActive(true);
                clientComponent.setUpdatedBy(loggedInUser);
                clientComponent.setUpdatedAt(createdAt);

                clientComponentRepository.save(clientComponent);

            } else {

                ClientComponent clientComponent = new ClientComponent();

                clientComponent.setClientInformation(client);
                clientComponent.setComponent(component);
                clientComponent.setIsActive(true);

                clientComponent.setCreatedBy(loggedInUser);
                clientComponent.setUpdatedBy(loggedInUser);

                clientComponent.setCreatedAt(createdAt);
                clientComponent.setUpdatedAt(createdAt);

                clientComponentRepository.save(
                        clientComponent);
            }

            // =================================================
            // CREATE PACKAGE COMPONENT
            // =================================================

            ClientPackageComponent packageComponent =new ClientPackageComponent();

            packageComponent.setClientPackage(savedPackage);
            packageComponent.setComponent(component);
            if (PackageType.INDIVIDUAL.equals(request.getPackageType())) {

                packageComponent.setTatDays(
                        componentRequest.getTat()
                );

            } else if (PackageType.PACKAGE.equals(request.getPackageType())) {

                packageComponent.setTatDays(
                        request.getInternalTat()
                );
            }
            packageComponent.setEffectiveFrom(null);
            packageComponent.setEffectiveTo(null);

            packageComponent.setIsActive(true);

            packageComponent.setCreatedBy(loggedInUser);
            packageComponent.setUpdatedBy(loggedInUser);

            packageComponent.setCreatedAt(createdAt);
            packageComponent.setUpdatedAt(createdAt);

            ClientPackageComponent savedPackageComponent =
                    clientPackageComponentRepository
                            .saveAndFlush(packageComponent);


            // =================================================
            // CREATE PRICING
            // =================================================

            if (PackageType.INDIVIDUAL.equals(
                    request.getPackageType())
                    || PackageType.PACKAGE.equals(
                            request.getPackageType())) {

                ClientComponentPricing pricing =
                        new ClientComponentPricing();

                pricing.setClientPackage(savedPackage);

                pricing.setComponent(component);

                pricing.setClientPackageComponent(
                        savedPackageComponent);

                pricing.setPrice(
                        PackageType.PACKAGE.equals(
                                request.getPackageType())
                                ? request.getPackageRate()
                                : componentRequest.getRatePerCheck());

                /*
                 * Pricing effective dates are intentionally NULL.
                 */
                pricing.setEffectiveFrom(null);
                pricing.setEffectiveTo(null);

                pricing.setIsActive(true);

                pricing.setCreatedBy(loggedInUser);
                pricing.setUpdatedBy(loggedInUser);

                pricing.setCreatedAt(createdAt);
                pricing.setUpdatedAt(createdAt);

                clientComponentPricingRepository.save(
                        pricing);
            }

            packageComponents.add(savedPackageComponent);
        }

        // -----------------------------------------------------
        // SET COMPONENTS FOR RESPONSE
        // -----------------------------------------------------

        savedPackage.setPackageComponents(packageComponents);

        return mapToResponse(savedPackage);
    }
   

    // =========================================================
    // UPDATE PACKAGE
    // =========================================================

    @Override
    @Transactional
    public ClientPackageResponse updateClientPackage(Long packageId,ClientPackageRequest request) 
	    	{
	    		SystemUser loggedInUser = loggedInUserService.getLoggedInUser();
	    		checkAdminRole("You are not authorized to update client packages.");
	        // =========================================================
	        // FIND PACKAGE
	        // =========================================================
	        ClientPackage clientPackage =clientPackageRepository.findById(packageId).orElseThrow(() -> new ResourceNotFoundException(
	                                        "Client package not found with id : "
	                                                + packageId));
	
	        if (!Boolean.TRUE.equals(clientPackage.getIsActive()))
	        {
	        	throw new ResourceNotFoundException(
	                    "Cannot update inactive client package.");
	        }
	        // =========================================================
	        // VALIDATE PACKAGE TYPE
	        // =========================================================
	        if (request.getPackageType() == null) 
	        {
	            throw new IllegalArgumentException("Package type is required.");
	        }
	        // =========================================================
	        // PACKAGE NAME
	        // =========================================================
	        String packageName = request.getPackageName();
	        if (packageName == null || packageName.trim().isEmpty()) 
	        {
	            throw new IllegalArgumentException(
	                    "Package name is required.");
	        }
	        // =========================================================
	        // VALIDATE COMPONENTS
	        // =========================================================
	        if (request.getComponents() == null|| request.getComponents().isEmpty())
	        {
	            throw new IllegalArgumentException(
	                    "At least one component is required.");
	        }
	        // =========================================================
	        // CALCULATE PACKAGE TAT / RATE
	        // =========================================================
	        Integer packageTat = request.getPackageTat();
	        BigDecimal packageRate = request.getPackageRate();
	        
	        if (PackageType.PACKAGE.equals(request.getPackageType())) {
	            if (request.getPackageTat() == null|| request.getPackageTat() <= 0) 
	            {
	                throw new IllegalArgumentException("Package TAT is required for package-wise pricing.");
	            }
	            } else 
	            	{
	
	            	packageTat = 0;
	            	packageRate = BigDecimal.ZERO;

	            	for (ClientPackageComponentRequest componentRequest
	            	        : request.getComponents()) {

	            	    if (componentRequest.getTat() == null
	            	            || componentRequest.getTat() <= 0) {

	            	        throw new IllegalArgumentException(
	            	                "TAT is required for every component.");
	            	    }

	            	    if (componentRequest.getRatePerCheck() == null
	            	            || componentRequest.getRatePerCheck()
	            	                    .compareTo(BigDecimal.ZERO) < 0) {

	            	        throw new IllegalArgumentException(
	            	                "Rate per check is required for every component.");
	            	    }

	            	    // Highest component TAT becomes package TAT
	            	    packageTat = Math.max(
	            	            packageTat,
	            	            componentRequest.getTat());

	            	    // DO NOT add individual rates to packageRate.
	            	    // Individual rates are stored in ClientComponentPricing.
	            	    packageRate = packageRate.add(
	            	            componentRequest.getRatePerCheck());
	            	}
	        }
	
	        // =========================================================
	        // DATE CALCULATION
	        // =========================================================
	
	        LocalDateTime updatedAt = LocalDateTime.now();
	        LocalDate effectiveFrom = clientPackage.getEffectiveFrom();
	        LocalDate effectiveTo =tatDateCalculationService.calculateEffectiveTo(updatedAt, packageTat, request.getHolidayType());
	        LocalDate internalEffectiveTo = tatDateCalculationService.calculateEffectiveTo(updatedAt, request.getInternalTat(), request.getHolidayType());
	        // =========================================================
	        // UPDATE PACKAGE
	        // =========================================================
	        clientPackage.setPackageName(packageName.trim());
	        clientPackage.setPackageType(request.getPackageType());
	        clientPackage.setPackageTat(packageTat);
	        clientPackage.setPackageRate(packageRate);
	        clientPackage.setInternalTat(request.getInternalTat());
	        clientPackage.setHolidayType(request.getHolidayType());
	        clientPackage.setEffectiveTo(effectiveTo);
	        clientPackage.setInternalEffectiveTo(internalEffectiveTo);
	        clientPackage.setUpdatedBy(loggedInUser);
	        clientPackage.setUpdatedAt(updatedAt);
	        if (request.getIsActive() != null) 
	        {
	            clientPackage.setIsActive(
	                    request.getIsActive());
	        }
	        // =========================================================
	        // CURRENT ACTIVE COMPONENTS
	        // =========================================================
	        List<ClientPackageComponent> activeComponents =clientPackageComponentRepository.findByClientPackageIdAndIsActiveTrue(packageId);
	        // =========================================================
	        // REQUESTED COMPONENT IDS
	        // =========================================================
	        Set<Long> requestedPackageComponentIds =request.getComponents().stream().map(ClientPackageComponentRequest::getClientPackageComponentId)
	                        .filter(Objects::nonNull)
	                        .collect(Collectors.toSet());
	        // =========================================================
	        // DEACTIVATE REMOVED COMPONENTS
	        // =========================================================
	
	        for (ClientPackageComponent existingComponent: activeComponents) {
	
	        	Long existingPackageComponentId =existingComponent.getId();
	
	        	if (!requestedPackageComponentIds.contains(existingPackageComponentId)) 
	        	{
	                // -------------------------------------------------
	                // DEACTIVATE PACKAGE COMPONENT
	                // -------------------------------------------------
	                existingComponent.setIsActive(false);
	                existingComponent.setUpdatedBy(loggedInUser);
	                existingComponent.setUpdatedAt(updatedAt);
	                clientPackageComponentRepository.save(existingComponent);
	                // -------------------------------------------------
	                // DEACTIVATE ACTIVE TAT
	                // -------------------------------------------------
	                Optional<ClientComponentTat> existingTat =clientComponentTatRepository.findByClientPackageComponentIdAndIsActiveTrue(
	                                        existingComponent.getId());
	
	                if (existingTat.isPresent()) 
	                	{
	                	ClientComponentTat tat =existingTat.get();
	                    tat.setIsActive(false);
	                    tat.setUpdatedBy(loggedInUser);
	                    tat.setUpdatedAt(updatedAt);
	                    clientComponentTatRepository.save(tat);
	                }
	
	                // -------------------------------------------------
	                // DEACTIVATE ACTIVE PRICING
	                // -------------------------------------------------
	
	                Optional<ClientComponentPricing> existingPricing = clientComponentPricingRepository.findByClientPackageComponentIdAndIsActiveTrue(
	                                        existingComponent.getId());
	
	                if (existingPricing.isPresent()) {
	                    ClientComponentPricing pricing =existingPricing.get();
	                    pricing.setIsActive(false);
	                    pricing.setUpdatedBy(loggedInUser);
	                    pricing.setUpdatedAt(updatedAt);
	                    clientComponentPricingRepository.save(pricing);
	                }
	            }
	        }
	        // =========================================================
	        // ADD / UPDATE COMPONENTS
	        // =========================================================
	        for (ClientPackageComponentRequest componentRequest: request.getComponents()) 
	        {
	            Long componentId = componentRequest.getComponentId();
	
	            if (componentId == null) 
	            {
	            	throw new IllegalArgumentException("Component id is required.");
	            }
	            // -----------------------------------------------------
	            // FIND COMPONENT
	            // -----------------------------------------------------
	            Component component =componentRepository.findById(componentId).orElseThrow(() ->
	                                    new ResourceNotFoundException("Component not found with id : "+ componentId));
	
	            if (!Boolean.TRUE.equals(component.getIsActive())) 
	            {
	                throw new ResourceNotFoundException("Component is inactive with id : "+ componentId);
	            }
	            
	            Optional<ClientComponent> existingClientComponent =clientComponentRepository
	            													.findByClientInformationIdAndComponentId(clientPackage
	            													.getClientInformation().getId(),component.getId());
	
	            if (existingClientComponent.isPresent()) 
	            {
	                ClientComponent clientComponent =existingClientComponent.get();
	                clientComponent.setIsActive(true);
	                clientComponent.setUpdatedBy(loggedInUser);
	                clientComponent.setUpdatedAt(updatedAt);
	                clientComponentRepository.save(clientComponent);
	
	            } else {
	
	                ClientComponent clientComponent =new ClientComponent();
	
	                clientComponent.setClientInformation(clientPackage.getClientInformation());
	                clientComponent.setComponent(component);
	                clientComponent.setIsActive(true);
	                clientComponent.setCreatedBy(loggedInUser);
	                clientComponent.setUpdatedBy(loggedInUser);
	                clientComponent.setCreatedAt(updatedAt);
	                clientComponent.setUpdatedAt(updatedAt);
	                clientComponentRepository.save(clientComponent);
	            }
	            // -----------------------------------------------------
	            // FIND EXISTING ACTIVE COMPONENT
	            // -----------------------------------------------------
	            ClientPackageComponent existing = null;
	            
	            if (componentRequest.getClientPackageComponentId() != null) {
	
	                existing =clientPackageComponentRepository
	                                .findByIdAndClientPackageIdAndIsActiveTrue(componentRequest
	                                                .getClientPackageComponentId(),packageId)
	                                .orElseThrow(() ->
	                                        new ResourceNotFoundException("Package component not found with id : "
	                                                        + componentRequest
	                                                                .getClientPackageComponentId()));
	            }
	            // =====================================================
	            // EXISTING COMPONENT
	            // =====================================================
	            if (existing != null) {
	                LocalDate componentEffectiveFrom =existing.getEffectiveFrom();
	
	                LocalDate componentEffectiveTo;
	                // -------------------------------------------------
	                // PACKAGE-WISE
	                // -------------------------------------------------
	                if (PackageType.PACKAGE.equals(request.getPackageType())) {
	                    componentEffectiveTo =effectiveTo;
	                }
	                // -------------------------------------------------
	                // INDIVIDUAL-WISE
	                // -------------------------------------------------
	                else {
	                    componentEffectiveTo =tatDateCalculationService.calculateEffectiveTo(updatedAt,componentRequest.getTat(),request.getHolidayType());
	                }
	                // -------------------------------------------------
	                // UPDATE PACKAGE COMPONENT
	                // -------------------------------------------------
	                existing.setEffectiveFrom( componentEffectiveFrom);
	                existing.setEffectiveTo(componentEffectiveTo);
	                existing.setUpdatedBy(loggedInUser);
	                existing.setUpdatedAt(updatedAt);
	                ClientPackageComponent savedComponent =clientPackageComponentRepository.save(existing);
	                // =================================================
	                // TAT
	                // =================================================
	                Integer newTat;
	                if (PackageType.PACKAGE.equals(request.getPackageType()))
	                {
	                    newTat = request.getPackageTat();
	                } else {
	                    newTat = componentRequest.getTat();
	                }
	                Optional<ClientComponentTat> existingTat =
	                        clientComponentTatRepository
	                                .findByClientPackageComponentIdAndIsActiveTrue(
	                                        savedComponent.getId());
	
	                if (existingTat.isPresent()) {
	
	                    ClientComponentTat tat = existingTat.get();
	                    // Update the SAME TAT row
	                    tat.setTatDays(newTat);
	                    tat.setEffectiveFrom(componentEffectiveFrom);
	                    tat.setEffectiveTo(componentEffectiveTo);
	                    tat.setIsActive(true);
	                    tat.setUpdatedBy(loggedInUser);
	                    tat.setUpdatedAt(updatedAt);
	                    clientComponentTatRepository.save(tat);
	                } else {
	
	                    // ---------------------------------------------
	                    // NO TAT RECORD -> CREATE
	                    // ---------------------------------------------
	                    ClientComponentTat newTatRecord =new ClientComponentTat();
	
	                    newTatRecord.setClientPackageComponent(savedComponent);
	                    newTatRecord.setTatDays( newTat);
	                    newTatRecord.setEffectiveFrom( componentEffectiveFrom);
	                    newTatRecord.setEffectiveTo( componentEffectiveTo);
	                    newTatRecord.setIsActive(true);
	                    newTatRecord.setCreatedBy(loggedInUser);
	                    newTatRecord.setUpdatedBy( loggedInUser);
	                    newTatRecord.setCreatedAt(updatedAt);
	                    newTatRecord.setUpdatedAt(updatedAt);
	                    clientComponentTatRepository.save(newTatRecord);
	                }
	                
	             // =================================================
	             // PRICING
	             // =================================================

	             BigDecimal newPrice;

	             if (PackageType.PACKAGE.equals( request.getPackageType())) {

	                 // Package Wise:
	                 // Every check gets the same package rate
	                 newPrice = request.getPackageRate();

	             } else {

	                 // Individual Wise:
	                 // Every check gets its own rate
	            	 newPrice = componentRequest.getRatePerCheck();
	             }

	             if (newPrice == null
	                     || newPrice.compareTo(BigDecimal.ZERO) < 0) {

	                 throw new IllegalArgumentException(
	                         "Price is required.");
	             }
	             // =================================================
	             // FIND PRICING FOR THIS EXACT CHECK
	             // =================================================

	             Optional<ClientComponentPricing> existingPricing =
	                     clientComponentPricingRepository
	                             .findByClientPackageComponentIdAndIsActiveTrue(
	                                     savedComponent.getId());
	             // =================================================
	             // EXISTING PRICING -> UPDATE SAME ROW
	             // =================================================

	             if (existingPricing.isPresent()) {

	                 ClientComponentPricing pricing = existingPricing.get();
	                 pricing.setPrice(newPrice);
	                 pricing.setEffectiveFrom(componentEffectiveFrom);
	                 pricing.setEffectiveTo(componentEffectiveTo);
	                 pricing.setIsActive(true);
	                 pricing.setUpdatedBy(loggedInUser);
	                 pricing.setUpdatedAt(updatedAt);
	                 clientComponentPricingRepository.save(pricing);
	             }
	             // =================================================
	             // NO PRICING -> CREATE
	             // =================================================
	             else {
	                 ClientComponentPricing newPricing =new ClientComponentPricing();
	                 newPricing.setClientPackage(clientPackage);
	                 newPricing.setComponent(component);
	                 newPricing.setClientPackageComponent(savedComponent);
	                 newPricing.setPrice(newPrice);
	                 newPricing.setEffectiveFrom(componentEffectiveFrom);
	                 newPricing.setEffectiveTo(componentEffectiveTo);
	                 newPricing.setIsActive(true);
	                 newPricing.setCreatedBy(loggedInUser);
	                 newPricing.setUpdatedBy(loggedInUser);
	                 newPricing.setCreatedAt(updatedAt);
	                 newPricing.setUpdatedAt(updatedAt);
	                 clientComponentPricingRepository.save(newPricing);
	             }
	            }
	            // =====================================================
	            // NEW COMPONENT
	            // =====================================================
	            else {
	            	
	                ClientPackageComponent newComponent =new ClientPackageComponent();
	                
	                newComponent.setClientPackage(clientPackage);
	                newComponent.setComponent(component);
	                LocalDate componentEffectiveFrom =calculateEffectiveFrom();
	                LocalDate componentEffectiveTo;
	                if (PackageType.PACKAGE.equals(request.getPackageType()))
	                	{
	                		componentEffectiveFrom =effectiveFrom;
	                		componentEffectiveTo =effectiveTo;
	
	                } else {
	
	                    componentEffectiveTo =
	                            tatDateCalculationService
	                                    .calculateEffectiveTo(
	                                            updatedAt,
	                                            componentRequest.getTat(),
	                                            request.getHolidayType());
	                }
	
	                newComponent.setEffectiveFrom(componentEffectiveFrom);
	                newComponent.setEffectiveTo(componentEffectiveTo);
	                newComponent.setIsActive(true);
	                newComponent.setCreatedBy(loggedInUser);
	                newComponent.setUpdatedBy(loggedInUser);
	                newComponent.setCreatedAt(updatedAt);
	                newComponent.setUpdatedAt(updatedAt);
	                // -------------------------------------------------
	                // SAVE COMPONENT
	                // -------------------------------------------------
	                ClientPackageComponent savedComponent =clientPackageComponentRepository.saveAndFlush(newComponent);
	             
	           
	                // =================================================
	                // CREATE TAT
	                // =================================================
	
	                Integer newTat;
	
	                if (PackageType.PACKAGE.equals(
	                        request.getPackageType())) {
	
	                    newTat = request.getPackageTat();
	
	                } else {
	
	                    newTat = componentRequest.getTat();
	                }
	
	                ClientComponentTat clientTat =
	                        new ClientComponentTat();
	
	                clientTat.setClientPackageComponent(
	                        savedComponent);
	
	                clientTat.setEffectiveFrom(
	                        componentEffectiveFrom);
	
	                clientTat.setEffectiveTo(
	                        componentEffectiveTo);
	
	                clientTat.setTatDays(
	                        newTat);
	
	                clientTat.setIsActive(true);
	
	                clientTat.setCreatedBy(
	                        loggedInUser);
	
	                clientTat.setUpdatedBy(
	                        loggedInUser);
	
	                clientTat.setCreatedAt(
	                        updatedAt);
	
	                clientTat.setUpdatedAt(
	                        updatedAt);
	
	                clientComponentTatRepository.save(
	                        clientTat);
	
	                // =================================================
	                // CREATE PRICING - INDIVIDUAL ONLY
	                // =================================================
	
	                if (PackageType.INDIVIDUAL.equals(
	                        request.getPackageType())) {
	                	
	                	BigDecimal newPrice;

	                	if (PackageType.PACKAGE.equals(
	                	        request.getPackageType())) {

	                	    // Same package rate for every check
	                	    newPrice = request.getPackageRate();

	                	} else {

	                	    // Individual rate for this check
	                	    newPrice = componentRequest.getRatePerCheck();
	                	}

	                	if (newPrice == null
	                	        || newPrice.compareTo(BigDecimal.ZERO) < 0) {

	                	    throw new IllegalArgumentException(
	                	            "Price is required.");
	                	}
	                	

	                	ClientComponentPricing pricing =
	                	        new ClientComponentPricing();

	                	pricing.setClientPackage(clientPackage);

	                	pricing.setComponent(component);

	                	// IMPORTANT
	                	pricing.setClientPackageComponent(savedComponent);

	                	pricing.setPrice(newPrice);

	                	pricing.setEffectiveFrom(componentEffectiveFrom);

	                	pricing.setEffectiveTo(componentEffectiveTo);

	                	pricing.setIsActive(true);

	                	pricing.setCreatedBy(
	                	        loggedInUser);

	                	pricing.setUpdatedBy(
	                	        loggedInUser);

	                	pricing.setCreatedAt(
	                	        updatedAt);

	                	pricing.setUpdatedAt(
	                	        updatedAt);

	                	clientComponentPricingRepository.save(
	                	        pricing);
	                }
	            }
	        }
	
	        // =========================================================
	        // SAVE PACKAGE
	        // =========================================================
	
	        ClientPackage updatedPackage =
	                clientPackageRepository.save(
	                        clientPackage);
	
	        // =========================================================
	        // GET UPDATED ACTIVE COMPONENTS
	        // =========================================================
	
	        List<ClientPackageComponent> updatedComponents =
	                clientPackageComponentRepository
	                        .findByClientPackageIdAndIsActiveTrue(
	                                packageId);
	
	        updatedPackage.setPackageComponents(
	                updatedComponents);
	
	        // =========================================================
	        // RESPONSE
	        // =========================================================
	
	        return mapToResponse(updatedPackage);
	    }

    
    
    // =========================================================
    // GET PACKAGE BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public ClientPackageResponse getClientPackageById(
            Long packageId) {

        checkAdminRole(
                "You are not authorized to view client packages.");


        ClientPackage clientPackage =
                clientPackageRepository
                        .findById(packageId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Client package not found with id : "
                                                + packageId));


        if (!Boolean.TRUE.equals(
                clientPackage.getIsActive())) {

            throw new ResourceNotFoundException(
                    "Client package is inactive.");
        }


        List<ClientPackageComponent> activeComponents =
                clientPackageComponentRepository
                        .findByClientPackageIdAndIsActiveTrue(
                                packageId);

        clientPackage.setPackageComponents(
                activeComponents);


        return mapToResponse(clientPackage);
    }


    // =========================================================
    // GET PACKAGES BY CLIENT
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<ClientPackageResponse> getPackagesByClient(
            Long clientId) {

        checkAdminRole(
                "You are not authorized to view client packages.");


        clientInformationRepository.findById(clientId).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Client not found with id : "
                                        + clientId));


        List<ClientPackage> packages =clientPackageRepository.findByClientInformationIdAndIsActiveTrue(clientId);


        return packages.stream()
                .map(packageEntity -> {

                    List<ClientPackageComponent>activeComponents =
                            clientPackageComponentRepository
                                    .findByClientPackageIdAndIsActiveTrue(
                                            packageEntity.getId());

                    packageEntity.setPackageComponents(
                            activeComponents);

                    return mapToResponse(
                            packageEntity);

                })
                .toList();
    }


    // =========================================================
    // DELETE / DEACTIVATE PACKAGE
    // =========================================================

    @Override
    public void deleteClientPackage(
            Long packageId) {

        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();
        
        String loggedInRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).findFirst().orElse(null);

        if (!"SUPER_ADMIN".equals(loggedInRole)) {
            throw new IllegalArgumentException(
                "You are not authorized to delete client package."
            );
        }


        ClientPackage clientPackage =
                clientPackageRepository
                        .findById(packageId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Client package not found with id : "
                                                + packageId));


        if (!Boolean.TRUE.equals(
                clientPackage.getIsActive())) {

            throw new IllegalArgumentException(
                    "Client package is already inactive.");
        }


        clientPackage.setIsActive(false);

        clientPackage.setUpdatedBy(
                loggedInUser);

        clientPackage.setUpdatedAt(
                LocalDateTime.now());


        clientPackageRepository.save(
                clientPackage);
    }


    // =========================================================
    // MAP ENTITY → RESPONSE
    // =========================================================

    private ClientPackageResponse mapToResponse(
            ClientPackage clientPackage) {

        ClientPackageResponse response =
                new ClientPackageResponse();

        // =====================================================
        // PACKAGE DETAILS
        // =====================================================

        response.setId(
                clientPackage.getId());

        response.setClientId(
                clientPackage
                        .getClientInformation()
                        .getId());

        response.setClientName(
                clientPackage
                        .getClientInformation()
                        .getClientName());

        response.setPackageName(
                clientPackage.getPackageName());

        response.setPackageType(
                clientPackage.getPackageType() != null
                        ? clientPackage.getPackageType().name()
                        : null);

        response.setPackageTat(
                clientPackage.getPackageTat());

        response.setInternalTat(
                clientPackage.getInternalTat());

        response.setPackageRate(
                clientPackage.getPackageRate());

        response.setHolidayType(
                clientPackage.getHolidayType() != null
                        ? clientPackage.getHolidayType().name()
                        : null);

        // =====================================================
        // PACKAGE EFFECTIVE DATES
        // =====================================================

        response.setEffectiveFrom(
                clientPackage.getEffectiveFrom());

        response.setEffectiveTo(
                clientPackage.getEffectiveTo());

        response.setInternalEffectiveTo(
                clientPackage.getInternalEffectiveTo());

        // =====================================================
        // AUDIT
        // =====================================================

        response.setCreatedAt(
                clientPackage.getCreatedAt());

        response.setUpdatedAt(
                clientPackage.getUpdatedAt());

        response.setIsActive(
                clientPackage.getIsActive());

        // =====================================================
        // COMPONENTS
        // =====================================================

        List<ClientPackageComponentResponse> componentResponses =
                new ArrayList<>();

        if (clientPackage.getPackageComponents() != null) {

            for (ClientPackageComponent packageComponent
                    : clientPackage.getPackageComponents()) {

                ClientPackageComponentResponse componentResponse =
                        new ClientPackageComponentResponse();

                // -------------------------------------------------
                // BASIC COMPONENT DETAILS
                // -------------------------------------------------

                componentResponse.setId(
                        packageComponent.getId());

                componentResponse.setPackageId(
                        clientPackage.getId());

                componentResponse.setComponentId(
                        packageComponent
                                .getComponent()
                                .getId());

                componentResponse.setComponentName(
                        packageComponent
                                .getComponent()
                                .getComponentName());

                componentResponse.setIsActive(
                        packageComponent.getIsActive());

                // -------------------------------------------------
                // COMPONENT EFFECTIVE DATES
                // -------------------------------------------------

                componentResponse.setEffectiveFrom(
                        packageComponent.getEffectiveFrom());

                componentResponse.setEffectiveTo(
                        packageComponent.getEffectiveTo());

                // =================================================
                // INDIVIDUAL WISE
                // =================================================

                if (PackageType.INDIVIDUAL.equals(
                        clientPackage.getPackageType())) {

                    // ---------------------------------------------
                    // COMPONENT TAT
                    // ---------------------------------------------

                    Optional<ClientComponentTat> componentTat =
                            clientComponentTatRepository
                                    .findByClientPackageComponentIdAndIsActiveTrue(
                                            packageComponent.getId());

                    if (componentTat.isPresent()) {

                        componentResponse.setTat(
                                componentTat
                                        .get()
                                        .getTatDays());
                    } else {

                        componentResponse.setTat(null);
                    }

                    // ---------------------------------------------
                    // COMPONENT PRICING
                    // ---------------------------------------------

                    Optional<ClientComponentPricing> componentPricing =
                            clientComponentPricingRepository
                                    .findByClientPackageComponentIdAndIsActiveTrue(
                                            packageComponent.getId());

                    if (componentPricing.isPresent()) {

                        componentResponse.setRatePerCheck(
                                componentPricing
                                        .get()
                                        .getPrice());
                    } else {

                        componentResponse.setRatePerCheck(null);
                    }
                }


                else if (PackageType.PACKAGE.equals(
                        clientPackage.getPackageType())) {

                	componentResponse.setTat(
                            clientPackage.getPackageTat());

                    componentResponse.setRatePerCheck(clientPackage.getPackageRate());
                }

                componentResponses.add(
                        componentResponse);
            }
        }

        response.setComponents(
                componentResponses);
        
        

        return response;
    }

    // =========================================================
    // ROLE CHECK
    // =========================================================

    private void checkAdminRole(
            String message) {

        String role =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElse(null);


        if (!"SUPER_ADMIN".equals(role)
                && !"ADMIN".equals(role) && !"DATA_ENTRY_TEAM_MEMBER".equals(role)) {

            throw new ResourceNotFoundException(
                    message);
        }
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
