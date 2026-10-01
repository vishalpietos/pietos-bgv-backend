package com.pietos.bgv.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pietos.bgv.dto.request.client.ClientLocationCreateRequest;
import com.pietos.bgv.dto.request.client.ClientLocationPatchRequest;
import com.pietos.bgv.dto.request.client.ClientLocationSearchRequest;
import com.pietos.bgv.dto.request.client.ClientLocationUpdateRequest;
import com.pietos.bgv.dto.response.client.ClientLocationDropdownResponse;
import com.pietos.bgv.dto.response.client.ClientLocationResponse;
import com.pietos.bgv.entity.ClientInformation;
import com.pietos.bgv.entity.ClientLocation;
import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.enums.ClientLocationStatus;
import com.pietos.bgv.exception.DuplicateResourceException;
import com.pietos.bgv.exception.ResourceNotFoundException;
import com.pietos.bgv.repository.ClientInformationRepository;
import com.pietos.bgv.repository.ClientLocationRepository;
import com.pietos.bgv.security.LoggedInUserService;
import com.pietos.bgv.service.ClientLocationService;
import org.springframework.data.jpa.domain.Specification;
import com.pietos.bgv.specification.ClientLocationSpecification;

@Transactional
@Service
public class ClientLocationServiceImpl implements ClientLocationService {
	
	
	private final ClientLocationRepository locationRepository;
	private final ClientInformationRepository clientRepository;
	private final LoggedInUserService loggedInUserService;

	public ClientLocationServiceImpl(
	        ClientLocationRepository locationRepository,
	        ClientInformationRepository clientRepository,
	        LoggedInUserService loggedInUserService) {

	    this.locationRepository = locationRepository;
	    this.clientRepository = clientRepository;
	    this.loggedInUserService =loggedInUserService;
	}

	@Override
	public ClientLocationResponse createLocation(ClientLocationCreateRequest request) {
			
		
		
	    // Check Client Exists
	    ClientInformation client = clientRepository.findById(request.getClientId())
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Client not found with id : " + request.getClientId()));

	    // Check Duplicate Location
	    if (locationRepository.existsByClientInformationIdAndLocationNameIgnoreCase(
	            request.getClientId(),
	            request.getLocationName())) {

	        throw new DuplicateResourceException(
	                "Location already exists for this client.");
	    }
	    
	    if (clientRepository.existsByOfficialEmail(
	            request.getOfficialEmail())) {

	        throw new DuplicateResourceException(
	                "Official email already exists for a client.");
	    }

	    // Check against other Locations
	    if (locationRepository.existsByOfficialEmailIgnoreCase(
	            request.getOfficialEmail())) {

	        throw new DuplicateResourceException(
	                "Official email already exists for a location.");
	    }
	    
	    SystemUser loggedInUser = loggedInUserService.getLoggedInUser();
	    ClientLocation location = new ClientLocation();

	    location.setClientInformation(client);

	    location.setLocationName(request.getLocationName());
	    location.setAddress(request.getAddress());
	    location.setCountry(request.getCountry());
	    location.setState(request.getState());
	    location.setCity(request.getCity());
	    location.setPincode(request.getPincode());

	    location.setContactPerson(request.getContactPerson());
	    location.setOfficialEmail(request.getOfficialEmail());
	    location.setMobile(request.getMobile());
	    location.setLandline(request.getLandline());

	    location.setRemarks(request.getRemarks());

	    if (request.getStatus() == null) {
	        location.setStatus(ClientLocationStatus.ACTIVE);
	    } else {
	        location.setStatus(request.getStatus());
	    }

	    location.setCreatedAt(LocalDateTime.now());
	    location.setUpdatedAt(LocalDateTime.now());
        location.setCreatedBy(loggedInUser);
        location.setUpdatedBy(loggedInUser);

	    ClientLocation savedLocation = locationRepository.save(location);

	    return mapToResponse(savedLocation);
	}
 
	 @Transactional(readOnly = true)
	@Override
	public List<ClientLocationResponse> getAllLocations() {

	    return locationRepository.findAll()
	            .stream()
	            .map(this::mapToResponse)
	            .toList();
	}

	@Override
	 @Transactional(readOnly = true)
	public ClientLocationResponse getLocationById(Long id) {

	    ClientLocation location = locationRepository.findById(id)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Location not found with id : " + id));

	    return mapToResponse(location);
	}

	
	@Override
	public ClientLocationResponse updateLocation(
	        Long id,
	        ClientLocationUpdateRequest  request) {

	    // Check Location Exists
	    ClientLocation location = locationRepository.findById(id)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Location not found with id : " + id));

	    // Check Duplicate Location Name for Same Client
	    if (!location.getLocationName().equalsIgnoreCase(request.getLocationName())
	            && locationRepository.existsByClientInformationIdAndLocationNameIgnoreCase(
	                    location.getClientInformation().getId(),
	                    request.getLocationName())) {

	        throw new DuplicateResourceException(
	                "Location already exists for this client.");
	    }
	    
	 // ==========================================
	 // Check Duplicate Official Email
	 // ==========================================

	 if (!location.getOfficialEmail().equalsIgnoreCase(
	         request.getOfficialEmail())) {

	     // Check against Client Information
	     if (clientRepository.existsByOfficialEmail(
	             request.getOfficialEmail())) {

	         throw new DuplicateResourceException(
	                 "Official email already exists for a client.");
	     }

	     // Check against other Locations
	     if (locationRepository.existsByOfficialEmailIgnoreCase(
		            request.getOfficialEmail())) {

		        throw new DuplicateResourceException(
		                "Official email already exists for a location.");
		    }
	 }
	    
	    SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();
	    
	    // Update Location Details
	    location.setLocationName(request.getLocationName());
	    location.setAddress(request.getAddress());
	    location.setCountry(request.getCountry());
	    location.setState(request.getState());
	    location.setCity(request.getCity());
	    location.setPincode(request.getPincode());

	    // Update Contact Details
	    location.setContactPerson(request.getContactPerson());
	    location.setOfficialEmail(request.getOfficialEmail());
	    location.setMobile(request.getMobile());
	    location.setLandline(request.getLandline());

	    // Update Other Details
	    location.setRemarks(request.getRemarks());

	    if (request.getStatus() != null) {
	        location.setStatus(request.getStatus());
	    }

	    location.setUpdatedAt(LocalDateTime.now());
	    location.setUpdatedBy(loggedInUser);

	    ClientLocation updatedLocation = locationRepository.save(location);

	    return mapToResponse(updatedLocation);
	}

	@Override
	public void activateLocation(Long id) {

	    ClientLocation location = locationRepository.findById(id)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Location not found with id : " + id));

	    location.setStatus(ClientLocationStatus.ACTIVE);

	    location.setUpdatedAt(LocalDateTime.now());

	    locationRepository.save(location);
	}

	@Override
	public void deactivateLocation(Long id) {

	    ClientLocation location = locationRepository.findById(id)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Location not found with id : " + id));

	    location.setStatus(ClientLocationStatus.INACTIVE);

	    location.setUpdatedAt(LocalDateTime.now());

	    locationRepository.save(location);
	}

	@Override
 	public List<ClientLocationResponse> searchLocations(
	        ClientLocationSearchRequest request) {

	    Specification<ClientLocation> specification =
	            Specification
	                    .where(ClientLocationSpecification.hasClientId(request.getClientId()))
	                    .and(ClientLocationSpecification.hasLocationName(request.getLocationName()))
	                    .and(ClientLocationSpecification.hasCountry(request.getCountry()))
	                    .and(ClientLocationSpecification.hasState(request.getState()))
	                    .and(ClientLocationSpecification.hasCity(request.getCity()))
	                    .and(ClientLocationSpecification.hasOfficialEmail(request.getOfficialEmail()))
	                    .and(ClientLocationSpecification.hasContactPerson(request.getContactPerson()))
	                    .and(ClientLocationSpecification.hasMobile(request.getMobile()))
	                    .and(ClientLocationSpecification.hasStatus(request.getStatus()));

	    return locationRepository.findAll(specification)
	            .stream()
	            .map(this::mapToResponse)
	            .toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ClientLocationResponse> getLocationsByClientId(Long clientId) {

	    // Check Client Exists
	    clientRepository.findById(clientId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Client not found with id : " + clientId));

	    return locationRepository.findByClientInformationId(clientId)
	            .stream()
	            .map(this::mapToResponse)
	            .toList();
	}
	
	
	@Override
	public ClientLocationResponse patchLocation(
	        Long id,
	        ClientLocationPatchRequest request) {

	    ClientLocation location = locationRepository.findById(id)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Location not found with id : " + id));

	    // Check duplicate only if location name is changing
	    if (request.getLocationName() != null
	            && !request.getLocationName().equalsIgnoreCase(location.getLocationName())
	            && locationRepository.existsByClientInformationIdAndLocationNameIgnoreCase(
	                    location.getClientInformation().getId(),
	                    request.getLocationName())) {

	        throw new DuplicateResourceException(
	                "Location already exists for this client.");
	    }

	    if (request.getLocationName() != null)
	        location.setLocationName(request.getLocationName());

	    if (request.getAddress() != null)
	        location.setAddress(request.getAddress());

	    if (request.getCountry() != null)
	        location.setCountry(request.getCountry());

	    if (request.getState() != null)
	        location.setState(request.getState());

	    if (request.getCity() != null)
	        location.setCity(request.getCity());

	    if (request.getPincode() != null)
	        location.setPincode(request.getPincode());

	    if (request.getContactPerson() != null)
	        location.setContactPerson(request.getContactPerson());

	    if (request.getOfficialEmail() != null)
	        location.setOfficialEmail(request.getOfficialEmail());

	    if (request.getMobile() != null)
	        location.setMobile(request.getMobile());

	    if (request.getLandline() != null)
	        location.setLandline(request.getLandline());

	    if (request.getRemarks() != null)
	        location.setRemarks(request.getRemarks());

	    if (request.getStatus() != null)
	        location.setStatus(request.getStatus());

	    location.setUpdatedAt(LocalDateTime.now());

	    ClientLocation updatedLocation = locationRepository.save(location);

	    return mapToResponse(updatedLocation);
	}
	
	private ClientLocationResponse mapToResponse(ClientLocation location) {

	    ClientLocationResponse response = new ClientLocationResponse();

	    response.setId(location.getId());

	    // Client Details
	    response.setClientId(location.getClientInformation().getId());
	    response.setClientCode(location.getClientInformation().getClientCode());
	    response.setClientName(location.getClientInformation().getClientName());

	    // Location Details
	    response.setLocationName(location.getLocationName());
	    response.setAddress(location.getAddress());
	    response.setCountry(location.getCountry());
	    response.setState(location.getState());
	    response.setCity(location.getCity());
	    response.setPincode(location.getPincode());

	    response.setContactPerson(location.getContactPerson());
	    response.setOfficialEmail(location.getOfficialEmail());
	    response.setMobile(location.getMobile());
	    response.setLandline(location.getLandline());

	    response.setStatus(location.getStatus());
	    response.setRemarks(location.getRemarks());

	    response.setCreatedAt(location.getCreatedAt());
	    response.setUpdatedAt(location.getUpdatedAt());

	    return response;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ClientLocationDropdownResponse> getLocationsByClientdropdown(
	        Long clientId) {

	    clientRepository.findById(clientId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Client not found with id : " + clientId));

	    return locationRepository
	            .findByClientInformationId(clientId)
	            .stream()
	            .map(location ->
	                    new ClientLocationDropdownResponse(
	                            location.getId(),
	                            location.getLocationName()
	                    ))
	            .toList();
	}
}