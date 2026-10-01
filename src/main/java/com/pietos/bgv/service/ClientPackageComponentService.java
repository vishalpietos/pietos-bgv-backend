package com.pietos.bgv.service;

import java.util.List;

import com.pietos.bgv.dto.request.client.ClientPackageComponentRequest;
import com.pietos.bgv.dto.response.client.ClientPackageComponentResponse;

public interface ClientPackageComponentService {

    
    ClientPackageComponentResponse addComponent(
            ClientPackageComponentRequest request);

    
    List<ClientPackageComponentResponse> getComponentsByPackageId(
            Long packageId);

    
    void deactivateComponent(Long id);
}