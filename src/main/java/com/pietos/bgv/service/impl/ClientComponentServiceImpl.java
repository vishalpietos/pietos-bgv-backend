package com.pietos.bgv.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.pietos.bgv.dto.request.client.ClientComponentItemRequest;
import com.pietos.bgv.dto.request.client.ClientComponentRequest;
import com.pietos.bgv.dto.response.client.ClientComponentResponse;
import com.pietos.bgv.dto.response.client.ClientSelectedComponentResponse;
import com.pietos.bgv.dto.response.client.SelectedComponentResponse;
import com.pietos.bgv.entity.ClientComponent;
import com.pietos.bgv.entity.ClientInformation;
import com.pietos.bgv.entity.Component;
import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.exception.DuplicateResourceException;
import com.pietos.bgv.exception.ResourceNotFoundException;
import com.pietos.bgv.repository.ClientComponentRepository;
import com.pietos.bgv.repository.ClientInformationRepository;
import com.pietos.bgv.repository.ComponentRepository;
import com.pietos.bgv.security.LoggedInUserService;
import com.pietos.bgv.service.ClientComponentService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ClientComponentServiceImpl implements ClientComponentService {
	
	private final ClientComponentRepository clientComponentRepository;

	private final ClientInformationRepository clientRepository;

	private final ComponentRepository componentRepository;

	private final LoggedInUserService loggedInUserService;
	
	public ClientComponentServiceImpl(
	        ClientComponentRepository clientComponentRepository,
	        ClientInformationRepository clientRepository,
	        ComponentRepository componentRepository,
	        LoggedInUserService loggedInUserService) {

	    this.clientComponentRepository = clientComponentRepository;
	    this.clientRepository = clientRepository;
	    this.componentRepository = componentRepository;
	    this.loggedInUserService = loggedInUserService;
	}

	@Override
	public ClientSelectedComponentResponse saveClientComponents(
	        ClientComponentRequest request) {// =====================================================
		// LOGGED-IN USER
		// =====================================================

		SystemUser loggedInUser =
		        loggedInUserService.getLoggedInUser();

		String roleName =
		        loggedInUser.getRole().getRoleName();

		// =====================================================
		// GET CLIENT
		// =====================================================

		ClientInformation client;

		if ("SUPER_ADMIN".equals(roleName)) {

		    client =
		            clientRepository.findById(
		                    request.getClientId())
		                    .orElseThrow(() ->
		                            new ResourceNotFoundException(
		                                    "Client not found with id : "
		                                            + request.getClientId()));

		} else if ("CLIENT_ADMIN".equals(roleName)) {

		    client =
		            clientRepository
		                    .findBySystemUser(loggedInUser)
		                    .orElseThrow(() ->
		                            new ResourceNotFoundException(
		                                    "Client not found for logged-in user."));

		} else {

		    throw new ResourceNotFoundException(
		            "You are not authorized to assign components.");
		}

		// =====================================================
		// VALIDATE REQUEST
		// =====================================================

		if (request.getComponents() == null) {

		    throw new IllegalArgumentException(
		            "Component list is required.");
		}

		// =====================================================
		// REQUESTED COMPONENT IDS
		// =====================================================

		Set<Long> requestedComponentIds = request.getComponents()
		                .stream()
		                .map(ClientComponentItemRequest::getComponentId)
		                .filter(Objects::nonNull)
		                .collect(Collectors.toSet());

		// =====================================================
		// GET ALL EXISTING CLIENT COMPONENTS
		// IMPORTANT: GET ACTIVE + INACTIVE
		// =====================================================

		List<ClientComponent> existingComponents =
		        clientComponentRepository
		                .findByClientInformationId(
		                        client.getId());

		// =====================================================
		// UPDATE EXISTING COMPONENTS
		// =====================================================

		for (ClientComponent clientComponent
		        : existingComponents) {

		    Long componentId =
		            clientComponent
		                    .getComponent()
		                    .getId();

		    // -------------------------------------------------
		    // SELECTED IN CURRENT SAVE
		    // -------------------------------------------------

		    if (requestedComponentIds.contains(componentId)) {

		        clientComponent.setIsActive(true);

		    }
		    // -------------------------------------------------
		    // NOT SELECTED IN CURRENT SAVE
		    // -------------------------------------------------

		    else {

		        clientComponent.setIsActive(false);
		    }

		    clientComponent.setUpdatedBy(loggedInUser);

		    clientComponent.setUpdatedAt(
		            LocalDateTime.now());

		    clientComponentRepository.save(
		            clientComponent);
		}

		// =====================================================
		// CREATE NEW COMPONENTS
		// =====================================================

		for (ClientComponentItemRequest item
		        : request.getComponents()) {

		    Long componentId =
		            item.getComponentId();

		    if (componentId == null) {

		        throw new IllegalArgumentException(
		                "Component id is required.");
		    }

		    // -------------------------------------------------
		    // CHECK IF COMPONENT ALREADY EXISTS
		    // -------------------------------------------------

		    Optional<ClientComponent> existing =
		            clientComponentRepository
		                    .findByClientInformationIdAndComponentId(
		                            client.getId(),
		                            componentId);

		    // -------------------------------------------------
		    // ALREADY EXISTS
		    // -------------------------------------------------

		    if (existing.isPresent()) {

		        // Already handled above.
		        // If selected, it is active.
		        // If it existed but wasn't selected,
		        // it was deactivated above.

		        continue;
		    }

		    // -------------------------------------------------
		    // FIND MASTER COMPONENT
		    // -------------------------------------------------

		    Component component =
		            componentRepository
		                    .findById(componentId)
		                    .orElseThrow(() ->
		                            new ResourceNotFoundException(
		                                    "Component not found with id : "
		                                            + componentId));

		    // -------------------------------------------------
		    // CHECK MASTER COMPONENT ACTIVE
		    // -------------------------------------------------

		    if (!Boolean.TRUE.equals(
		            component.getIsActive())) {

		        throw new ResourceNotFoundException(
		                "Component is inactive with id : "
		                        + componentId);
		    }

		    // -------------------------------------------------
		    // CREATE CLIENT COMPONENT
		    // -------------------------------------------------

		    ClientComponent clientComponent =
		            new ClientComponent();

		    clientComponent.setClientInformation(client);

		    clientComponent.setComponent(component);

		    clientComponent.setIsActive(true);

		    clientComponent.setCreatedBy(loggedInUser);

		    clientComponent.setUpdatedBy(loggedInUser);

		    LocalDateTime now =
		            LocalDateTime.now();

		    clientComponent.setCreatedAt(now);
		    clientComponent.setUpdatedAt(now);

		    clientComponentRepository.save(
		            clientComponent);
		}

		// =====================================================
		// RETURN CURRENT ACTIVE COMPONENTS
		// =====================================================

		return getComponentsByClientId(client.getId());
		}

	
	@Override
	public ClientSelectedComponentResponse getComponentsByClientId(
	        Long clientId) {

		// =====================================================
		// CHECK CLIENT EXISTS
		// =====================================================

		ClientInformation client =
		        clientRepository.findById(clientId)
		                .orElseThrow(() ->
		                        new ResourceNotFoundException(
		                                "Client not found with id : "
		                                        + clientId));

		// =====================================================
		// GET ACTIVE SELECTED COMPONENTS
		// =====================================================

		List<ClientComponent> clientComponents =
		        clientComponentRepository
		                .findByClientInformationIdAndIsActiveTrue(
		                        clientId);

		// =====================================================
		// CREATE COMPONENT LIST
		// =====================================================

		List<SelectedComponentResponse> selectedComponents =
		        clientComponents.stream()
		                .map(clientComponent -> {

		                    SelectedComponentResponse response =
		                            new SelectedComponentResponse();

		                    response.setComponentId(
		                            clientComponent
		                                    .getComponent()
		                                    .getId());

		                    response.setComponentName(
		                            clientComponent
		                                    .getComponent()
		                                    .getComponentName());

		                    return response;
		                })
		                .toList();

		// =====================================================
		// PREPARE RESPONSE
		// =====================================================

		ClientSelectedComponentResponse response =
		        new ClientSelectedComponentResponse();

		response.setClientId(
		        client.getId());

		response.setClientName(
		        client.getClientName());

		response.setComponents(
		        selectedComponents);

		return response;
	}

	@Override
	public List<ClientComponentResponse> getClientsByComponentId(
	        Long componentId) {

	    // Check Component Exists
	    componentRepository.findById(componentId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Component not found with id : " + componentId));

	    List<ClientComponent> clientComponents =
	            clientComponentRepository.findByComponentId(componentId);

	    if (clientComponents.isEmpty()) {
	        throw new ResourceNotFoundException(
	                "No clients found for component id : " + componentId);
	    }

	    return clientComponents.stream()
	            .map(this::mapToResponse)
	            .toList();
	}

	
	
	
	@Override
	public void deleteComponentByClientIdAndComponentId(
	        Long clientId,
	        Long componentId) {

	    ClientComponent clientComponent =
	            clientComponentRepository
	                    .findByClientInformationIdAndComponentId(
	                            clientId,
	                            componentId)
	                    .orElseThrow(() -> new ResourceNotFoundException(
	                            "Component not assigned to client."));

	    clientComponentRepository.delete(clientComponent);
	}
	
	private ClientComponentResponse mapToResponse(
	        ClientComponent clientComponent) {

	    ClientComponentResponse response =
	            new ClientComponentResponse();

	    response.setId(clientComponent.getId());

	    response.setClientId(clientComponent.getClientInformation().getId());

	    response.setClientName(clientComponent.getClientInformation().getClientName());

	    response.setComponentId(clientComponent.getComponent().getId());
	    
	    response.setIsActive(clientComponent.getComponent().getIsActive());

	    response.setComponentName(clientComponent.getComponent().getComponentName());

	    response.setCreatedAt(clientComponent.getCreatedAt());

	    response.setUpdatedAt(clientComponent.getUpdatedAt());

	    return response;
	}

}
