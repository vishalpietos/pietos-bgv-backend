package com.pietos.bgv.service;

import java.util.List;

import com.pietos.bgv.dto.request.client.ClientInformationRequest;
import com.pietos.bgv.dto.request.client.ClientInformationSearchRequest;
import com.pietos.bgv.dto.response.client.ClientDropdownResponse;
import com.pietos.bgv.dto.response.client.ClientInformationResponse;

public interface ClientInformationService {

    // Create Client
    ClientInformationResponse createClient(ClientInformationRequest request);

    // Get All Clients
    List<ClientInformationResponse> getAllClients();

    // Get Client By Id
    ClientInformationResponse getClientById(Long id);

    // Update Client
    ClientInformationResponse updateClient(Long id, ClientInformationRequest request);

    // Activate Client
    void activateClient(Long id);

    // Deactivate Client
    void deactivateClient(Long id);
    
    List<ClientInformationResponse> searchClients(
            ClientInformationSearchRequest request);
    
    List<ClientDropdownResponse> getClientDropdown();

}