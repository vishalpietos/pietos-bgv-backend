package com.pietos.bgv.service;

import java.util.List;

import com.pietos.bgv.dto.request.client.ClientComponentTatRequest;
import com.pietos.bgv.dto.response.client.ClientComponentTatResponse;

public interface ClientComponentTatService {

    // =========================================
    // CREATE / UPDATE TAT
    // =========================================

    List<ClientComponentTatResponse> saveOrUpdateClientComponentTat(
            ClientComponentTatRequest request);


    // =========================================
    // UPDATE SINGLE TAT RECORD
    // =========================================

    List<ClientComponentTatResponse> updateClientComponentTat(
            Long id,
            ClientComponentTatRequest request);


    // =========================================
    // GET ALL TAT OF CLIENT
    // =========================================

    List<ClientComponentTatResponse> getTatByClientId(
            Long clientId);


    // =========================================
    // DELETE / DEACTIVATE TAT
    // =========================================

    void deleteTatById(Long id);
}