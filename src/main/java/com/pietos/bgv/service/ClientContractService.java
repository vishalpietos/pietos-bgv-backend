package com.pietos.bgv.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.pietos.bgv.dto.request.client.ClientContractRequest;
import com.pietos.bgv.dto.response.client.ClientContractDocumentResponse;
import com.pietos.bgv.dto.response.client.ClientContractResponse;

public interface ClientContractService {

    ClientContractResponse saveClientContract(
            ClientContractRequest request);

    ClientContractResponse uploadContractDocuments(
            Long contractId,
            List<MultipartFile> contractFiles);

    ClientContractResponse updateClientContract(
            Long contractId,
            ClientContractRequest request);

    List<ClientContractResponse> getContractsByClient(
            Long clientId);

    List<ClientContractResponse> getActiveContracts();

    List<ClientContractResponse> getExpiredContracts();

    List<ClientContractResponse> getContractsExpiringWithin(
            int days);

    void deleteClientContract(
            Long contractId);

    ClientContractDocumentResponse getDocumentData(Long documentId);
}