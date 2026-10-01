package com.pietos.bgv.dto.response.client;

import java.util.List;

public class ClientSelectedComponentResponse {

    private Long clientId;

    private String clientName;

    private List<SelectedComponentResponse> components;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public List<SelectedComponentResponse> getComponents() {
        return components;
    }

    public void setComponents(List<SelectedComponentResponse> components) {
        this.components = components;
    }
}