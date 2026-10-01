package com.pietos.bgv.service;

import java.util.List;

import com.pietos.bgv.dto.request.client.ClientPackageRequest;
import com.pietos.bgv.dto.response.client.ClientPackageResponse;

public interface ClientPackageService {

    
    ClientPackageResponse saveClientPackage(
            ClientPackageRequest request);

    // Update package and components
    ClientPackageResponse updateClientPackage(
            Long packageId,
            ClientPackageRequest request);

    
    ClientPackageResponse getClientPackageById(
            Long packageId);

    
    List<ClientPackageResponse> getPackagesByClient(
            Long clientId);

    
    void deleteClientPackage(
            Long packageId);
}