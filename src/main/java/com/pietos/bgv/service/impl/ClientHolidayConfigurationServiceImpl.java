package com.pietos.bgv.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pietos.bgv.dto.request.client.ClientHolidayConfigurationRequest;
import com.pietos.bgv.dto.response.client.ClientHolidayConfigurationResponse;
import com.pietos.bgv.entity.ClientHolidayConfiguration;
import com.pietos.bgv.entity.ClientInformation;
import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.exception.ResourceNotFoundException;
import com.pietos.bgv.repository.ClientHolidayConfigurationRepository;
import com.pietos.bgv.repository.ClientInformationRepository;
import com.pietos.bgv.security.LoggedInUserService;
import com.pietos.bgv.service.ClientHolidayConfigurationService;

@Service
@Transactional
public class ClientHolidayConfigurationServiceImpl
        implements ClientHolidayConfigurationService {

    private final ClientHolidayConfigurationRepository holidayConfigurationRepository;

    private final ClientInformationRepository clientRepository;

    private final LoggedInUserService loggedInUserService;

    public ClientHolidayConfigurationServiceImpl(
            ClientHolidayConfigurationRepository holidayConfigurationRepository,
            ClientInformationRepository clientRepository,
            LoggedInUserService loggedInUserService) {

        this.holidayConfigurationRepository = holidayConfigurationRepository;
        this.clientRepository = clientRepository;
        this.loggedInUserService = loggedInUserService;
    }

    @Override
    public ClientHolidayConfigurationResponse saveOrUpdate(
            ClientHolidayConfigurationRequest request) {

        // Logged-in User
        SystemUser loggedInUser = loggedInUserService.getLoggedInUser();

        String roleName = loggedInUser.getRole().getRoleName();

        if (!"SUPER_ADMIN".equals(roleName)) {
            throw new ResourceNotFoundException(
                    "You are not authorized to configure holidays.");
        }

        // Check Client Exists
        ClientInformation client = clientRepository.findById(request.getClientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Client not found with id : " + request.getClientId()));
      
        if (request.getWef().isAfter(request.getWet())) {
            throw new ResourceNotFoundException(
                    "WEF cannot be after WET.");
        }

        Optional<ClientHolidayConfiguration> existingConfiguration =
                holidayConfigurationRepository.findByClientInformationId(
                        client.getId());

        if (existingConfiguration.isPresent()) {

            // Update
            ClientHolidayConfiguration configuration =
                    existingConfiguration.get();

            configuration.setWef(request.getWef());
            configuration.setWet(request.getWet());
            configuration.setHolidayType(request.getHolidayType());

            configuration.setUpdatedBy(loggedInUser);
            configuration.setUpdatedAt(LocalDateTime.now());

            holidayConfigurationRepository.save(configuration);

        } else {

            // Insert
            ClientHolidayConfiguration configuration =
                    new ClientHolidayConfiguration();

            configuration.setClientInformation(client);

            configuration.setWef(request.getWef());
            configuration.setWet(request.getWet());
            configuration.setHolidayType(request.getHolidayType());

            configuration.setCreatedBy(loggedInUser);
            configuration.setUpdatedBy(loggedInUser);

            configuration.setCreatedAt(LocalDateTime.now());
            configuration.setUpdatedAt(LocalDateTime.now());

            holidayConfigurationRepository.save(configuration);
        }

        return getByClientId(client.getId());
    }

    @Override
    public ClientHolidayConfigurationResponse getByClientId(
            Long clientId) {

        ClientHolidayConfiguration configuration =
                holidayConfigurationRepository
                        .findByClientInformationId(clientId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Holiday configuration not found."));

        ClientHolidayConfigurationResponse response =
                new ClientHolidayConfigurationResponse();

        response.setId(configuration.getId());

        response.setClientId(
                configuration.getClientInformation().getId());

        response.setClientName(
                configuration.getClientInformation().getClientName());

        response.setWef(configuration.getWef());

        response.setWet(configuration.getWet());

        response.setHolidayType(configuration.getHolidayType());

        response.setCreatedAt(configuration.getCreatedAt());

        response.setUpdatedAt(configuration.getUpdatedAt());

        return response;
    }

    @Override
    public void deleteByClientId(Long clientId) {

        ClientHolidayConfiguration configuration =
                holidayConfigurationRepository
                        .findByClientInformationId(clientId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Holiday configuration not found."));

        holidayConfigurationRepository.delete(configuration);
    }
		
	}
