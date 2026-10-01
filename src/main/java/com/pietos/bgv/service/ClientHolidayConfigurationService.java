package com.pietos.bgv.service;

import com.pietos.bgv.dto.request.client.ClientHolidayConfigurationRequest;
import com.pietos.bgv.dto.response.client.ClientHolidayConfigurationResponse;

public interface ClientHolidayConfigurationService {

    // Save or Update Holiday Configuration
    ClientHolidayConfigurationResponse saveOrUpdate(
            ClientHolidayConfigurationRequest request);

    // Get Holiday Configuration By Client
    ClientHolidayConfigurationResponse getByClientId(
            Long clientId);

    // Delete Holiday Configuration
    void deleteByClientId(
            Long clientId);
}