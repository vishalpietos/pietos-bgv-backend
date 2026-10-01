package com.pietos.bgv.service;

import java.util.List;

import com.pietos.bgv.dto.request.client.ClientComponentRequest;
import com.pietos.bgv.dto.response.client.ClientComponentResponse;
import com.pietos.bgv.dto.response.client.ClientSelectedComponentResponse;

public interface ClientComponentService {

    // Save all selected components of the logged-in client
    ClientSelectedComponentResponse saveClientComponents(
            ClientComponentRequest request);

    // Get all selected components of a client
    ClientSelectedComponentResponse getComponentsByClientId(
            Long clientId);

    // Get all clients assigned to a component
    List<ClientComponentResponse> getClientsByComponentId(
            Long componentId);

  
    void deleteComponentByClientIdAndComponentId(
            Long clientId,
            Long componentId);
}