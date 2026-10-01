package com.pietos.bgv.dto.response.client;

public class ClientDropdownResponse {

    private Long id;

    private String clientName;


    public ClientDropdownResponse() {
    }


    public ClientDropdownResponse(
            Long id,
            String clientName) {

        this.id = id;
        this.clientName = clientName;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}