package com.pietos.bgv.dto.request.client;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public class ClientComponentRequest {

    private Long clientId;

    @NotEmpty(message = "Please select at least one component.")
    @Valid
    private List<ClientComponentItemRequest> components;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public List<ClientComponentItemRequest> getComponents() {
        return components;
    }

    public void setComponents(List<ClientComponentItemRequest> components) {
        this.components = components;
    }
}