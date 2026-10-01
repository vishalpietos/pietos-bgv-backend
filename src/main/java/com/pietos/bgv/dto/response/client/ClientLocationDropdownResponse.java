package com.pietos.bgv.dto.response.client;

public class ClientLocationDropdownResponse {

    private Long id;

    private String locationName;


    public ClientLocationDropdownResponse() {
    }


    public ClientLocationDropdownResponse(
            Long id,
            String locationName) {

        this.id = id;
        this.locationName = locationName;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}