package com.pietos.bgv.service;

import java.util.List;

import com.pietos.bgv.dto.request.client.ClientLocationCreateRequest;
import com.pietos.bgv.dto.request.client.ClientLocationPatchRequest;
import com.pietos.bgv.dto.request.client.ClientLocationSearchRequest;
import com.pietos.bgv.dto.request.client.ClientLocationUpdateRequest;
import com.pietos.bgv.dto.response.client.ClientLocationDropdownResponse;
import com.pietos.bgv.dto.response.client.ClientLocationResponse;

public interface ClientLocationService {

    ClientLocationResponse createLocation(ClientLocationCreateRequest request);

    List<ClientLocationResponse> getAllLocations();

    ClientLocationResponse getLocationById(Long id);

    ClientLocationResponse updateLocation(
            Long id,
            ClientLocationUpdateRequest  request);

    void activateLocation(Long id);

    void deactivateLocation(Long id);

    List<ClientLocationResponse> searchLocations(
            ClientLocationSearchRequest request);

    List<ClientLocationResponse> getLocationsByClientId(Long clientId);
    
    ClientLocationResponse patchLocation(
            Long id,
            ClientLocationPatchRequest request);
    
    List<ClientLocationDropdownResponse> getLocationsByClientdropdown(Long clientId);

}