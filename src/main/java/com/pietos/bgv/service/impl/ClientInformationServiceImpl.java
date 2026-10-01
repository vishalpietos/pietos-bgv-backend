package com.pietos.bgv.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pietos.bgv.dto.request.client.ClientInformationRequest;
import com.pietos.bgv.dto.request.client.ClientInformationSearchRequest;
import com.pietos.bgv.dto.response.client.ClientDropdownResponse;
import com.pietos.bgv.dto.response.client.ClientInformationResponse;
import com.pietos.bgv.email.EmailSubject;
import com.pietos.bgv.entity.ClientInformation;
import com.pietos.bgv.entity.ClientLocation;
import com.pietos.bgv.entity.Role;
import com.pietos.bgv.enums.ClientLocationStatus;
import com.pietos.bgv.enums.ClientStatus;
import com.pietos.bgv.exception.DuplicateResourceException;
import com.pietos.bgv.exception.ResourceNotFoundException;
import com.pietos.bgv.repository.ClientInformationRepository;
import com.pietos.bgv.repository.ClientLocationRepository;
import com.pietos.bgv.repository.RoleRepository;
import com.pietos.bgv.service.ClientInformationService;
import com.pietos.bgv.specification.ClientInformationSpecification;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.repository.SystemUserRepository;
import com.pietos.bgv.security.LoggedInUserService;
import com.pietos.bgv.service.EmailService;
import com.pietos.bgv.util.PasswordGenerator;
import com.pietos.bgv.email.EmailTemplateService;

@Service
public class ClientInformationServiceImpl implements ClientInformationService {

    private final ClientInformationRepository clientRepository;
    
    private final RoleRepository roleRepository;
    
    private final SystemUserRepository systemUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;
    
    private final EmailTemplateService emailTemplateService;
    
    private final LoggedInUserService loggedInUserService;
    
    private final ClientLocationRepository locationRepository;
    
    

    public ClientInformationServiceImpl(
            ClientInformationRepository clientRepository,
            RoleRepository roleRepository,
            SystemUserRepository systemUserRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            EmailTemplateService emailTemplateService,
            LoggedInUserService loggedInUserService,
            ClientLocationRepository locationRepository) {

    	this.locationRepository = locationRepository;
        this.clientRepository = clientRepository;
        this.roleRepository = roleRepository;
        this.systemUserRepository = systemUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.emailTemplateService = emailTemplateService;
        this.loggedInUserService = loggedInUserService;
    }
    
    @Transactional
    @Override
    public ClientInformationResponse createClient(
            ClientInformationRequest request) {

        // Check duplicate official email
        if (clientRepository.existsByOfficialEmail(request.getOfficialEmail())) {
            throw new DuplicateResourceException(
                    "Official Email already exists.");
        }


        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();

        ClientInformation client = new ClientInformation();


        client.setClientName(request.getClientName());
        client.setAbbreviation(request.getAbbreviation());

        client.setContactPerson(request.getContactPerson());
        client.setOfficialEmail(request.getOfficialEmail());

        client.setMobile(request.getMobile());
        client.setLandline(request.getLandline());

        client.setAddress(request.getAddress());

        client.setCountry(request.getCountry());
        client.setState(request.getState());
        client.setCity(request.getCity());
        client.setPincode(request.getPincode());

        client.setRemarks(request.getRemarks());

        // Initial client status
        // Client status from request
        client.setStatus(request.getStatus());

        // Generate client code
        client.setClientCode(generateClientCode());

        // Audit
        client.setCreatedBy(loggedInUser);
        client.setUpdatedBy(loggedInUser);

        client.setCreatedAt(LocalDateTime.now());
        client.setUpdatedAt(LocalDateTime.now());

        // Save Client
        ClientInformation savedClient =
                clientRepository.save(client);
        
        ClientLocation clientLocation =
                new ClientLocation();

        clientLocation.setClientInformation(
                savedClient);

        clientLocation.setLocationName(
                savedClient.getCity());

        clientLocation.setAddress(
                savedClient.getAddress());

        clientLocation.setCountry(
                savedClient.getCountry());

        clientLocation.setState(
                savedClient.getState());

        clientLocation.setCity(
                savedClient.getCity());

        clientLocation.setPincode(
                savedClient.getPincode());

        clientLocation.setContactPerson(
                savedClient.getContactPerson());

        clientLocation.setOfficialEmail(
                savedClient.getOfficialEmail());

        clientLocation.setMobile(
                savedClient.getMobile());

        clientLocation.setLandline(
                savedClient.getLandline());

        clientLocation.setStatus(
                ClientLocationStatus.ACTIVE);

        clientLocation.setRemarks(
                savedClient.getRemarks());

        clientLocation.setCreatedBy(
                loggedInUser);

        clientLocation.setUpdatedBy(
                loggedInUser);

        clientLocation.setCreatedAt(
                LocalDateTime.now());

        clientLocation.setUpdatedAt(
                LocalDateTime.now());

        locationRepository.save(clientLocation);

        return mapToResponse(savedClient);
    }

    @Override
    public List<ClientInformationResponse> getAllClients() {

        return clientRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    
    
    @Override
    public ClientInformationResponse getClientById(Long id) {

        ClientInformation client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client not found with id : " + id));

        return mapToResponse(client);
    }

    @Transactional
    @Override
    public ClientInformationResponse updateClient(
            Long id,
            ClientInformationRequest request) {

        // Find client
        ClientInformation client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Client not found with id : " + id));

        // Get logged-in user
        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();

        // =====================================================
        // Check duplicate official email
        // =====================================================

        if (!client.getOfficialEmail().equalsIgnoreCase(
                request.getOfficialEmail())) {

            if (clientRepository.existsByOfficialEmail(
                    request.getOfficialEmail())) {

                throw new DuplicateResourceException(
                        "Official Email already exists.");
            }

            // Also make sure SystemUser email is not used
//            SystemUser existingUser =
//                    systemUserRepository
//                            .findByEmail(request.getOfficialEmail())
//                            .orElse(null);
//
//            if (existingUser != null
//                    && !existingUser.getId().equals(
//                            client.getSystemUser().getId())) {
//
//                throw new DuplicateResourceException(
//                        "Email already exists.");
//            }
        }

        // =====================================================
        // Update Client Information
        // =====================================================

        client.setClientName(request.getClientName());
        client.setAbbreviation(request.getAbbreviation());
        client.setContactPerson(request.getContactPerson());
        client.setOfficialEmail(request.getOfficialEmail());

        client.setMobile(request.getMobile());
        client.setLandline(request.getLandline());

        client.setAddress(request.getAddress());
        client.setStatus(request.getStatus());
        client.setCountry(request.getCountry());
        client.setState(request.getState());
        client.setCity(request.getCity());
        client.setPincode(request.getPincode());

        client.setRemarks(request.getRemarks());

        // =====================================================
        // Update linked SystemUser
        // =====================================================

//        SystemUser systemUser = client.getSystemUser();
//
//        if (systemUser != null) {
//
//            systemUser.setFirstName(
//                    request.getContactPerson());
//
//            systemUser.setEmail(
//                    request.getOfficialEmail());
//
//            systemUser.setMobileNumber(
//                    request.getMobile());
//
//            // Audit
//            systemUser.setUpdatedBy(loggedInUser);
//            systemUser.setUpdatedAt(LocalDateTime.now());
//
//            systemUserRepository.save(systemUser);
//        }

        // =====================================================
        // Client Audit
        // =====================================================

        client.setUpdatedBy(loggedInUser);
        client.setUpdatedAt(LocalDateTime.now());

        // Save Client
        ClientInformation updatedClient =
                clientRepository.save(client);

        return mapToResponse(updatedClient);
    }

    @Override
    public void activateClient(Long id) {

        ClientInformation client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client not found with id : " + id));

        client.setStatus(ClientStatus.ACTIVE);

        clientRepository.save(client);
    }

    @Override
    public void deactivateClient(Long id) {

        ClientInformation client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client not found with id : " + id));

        client.setStatus(ClientStatus.INACTIVE);

        clientRepository.save(client);
    }

    

    private ClientInformationResponse mapToResponse(ClientInformation client) {

        ClientInformationResponse response = new ClientInformationResponse();

        response.setId(client.getId());
        response.setClientCode(client.getClientCode());
        response.setClientName(client.getClientName());
        response.setAbbreviation(client.getAbbreviation());
        response.setContactPerson(client.getContactPerson());
        response.setOfficialEmail(client.getOfficialEmail());
        response.setMobile(client.getMobile());
        response.setLandline(client.getLandline());
        response.setAddress(client.getAddress());
        response.setCountry(client.getCountry());
        response.setState(client.getState());
        response.setCity(client.getCity());
        response.setPincode(client.getPincode());
        response.setStatus(client.getStatus());
        response.setRemarks(client.getRemarks());
        response.setCreatedAt(client.getCreatedAt());
        response.setUpdatedAt(client.getUpdatedAt());
        //response.setRoleName(client.getSystemUser().getRole().getRoleName());

        return response;
    }

    private String generateClientCode() {

        Long total = clientRepository.getTotalClients();

        return String.format("#PIT/CL/%06d", total + 1);

    }
    
    @Override
    public List<ClientInformationResponse> searchClients(
            ClientInformationSearchRequest request) {

        Specification<ClientInformation> specification =
                Specification
                        .where(ClientInformationSpecification.hasClientCode(request.getClientCode()))
                        .and(ClientInformationSpecification.hasClientName(request.getClientName()))
                        .and(ClientInformationSpecification.hasContactPerson(request.getContactPerson()))
                        .and(ClientInformationSpecification.hasOfficialEmail(request.getOfficialEmail()))
                        .and(ClientInformationSpecification.hasAbbreviation(request.getAbbreviation()))
                        .and(ClientInformationSpecification.hasCreatedAt(request.getClientSince()))
                        .and(ClientInformationSpecification.hasMobile(request.getMobile()))
                        .and(ClientInformationSpecification.hasCountry(request.getCountry()))
                        .and(ClientInformationSpecification.hasState(request.getState()))
                        .and(ClientInformationSpecification.hasCity(request.getCity()))
                        .and(ClientInformationSpecification.hasStatus(request.getStatus()));

        return clientRepository.findAll(specification)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    
    @Override
    public List<ClientDropdownResponse> getClientDropdown() {

        return clientRepository
                .findByStatus(ClientStatus.ACTIVE)
                .stream()
                .map(client -> new ClientDropdownResponse(
                        client.getId(),
                        client.getClientName()
                ))
                .collect(Collectors.toList());
    }
}
