package com.pietos.bgv.dto.request.client;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class ClientContractRequest {

    private Long clientId;

    private String contractName;

    private LocalDate wef;

    private LocalDate wet;

    private List<MultipartFile> contractDocuments;

    public ClientContractRequest() {
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

    public List<MultipartFile> getContractDocuments() {
        return contractDocuments;
    }

    public void setContractDocuments(List<MultipartFile> contractDocuments) {
        this.contractDocuments = contractDocuments;
    }
}