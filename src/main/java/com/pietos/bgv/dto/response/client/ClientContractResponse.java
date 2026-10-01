package com.pietos.bgv.dto.response.client;

import java.time.LocalDate;
import java.util.List;

public class ClientContractResponse {

    private Long id;

    private Long clientId;

    private String contractName;

    private LocalDate wef;

    private LocalDate wet;

    private List<ClientContractDocumentResponse> documents;

    public ClientContractResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public LocalDate getWef() {
        return wef;
    }

    public void setWef(LocalDate wef) {
        this.wef = wef;
    }

    public LocalDate getWet() {
        return wet;
    }

    public void setWet(LocalDate wet) {
        this.wet = wet;
    }

    public List<ClientContractDocumentResponse> getDocuments() {
        return documents;
    }

    public void setDocuments(List<ClientContractDocumentResponse> documents) {
        this.documents = documents;
    }
}