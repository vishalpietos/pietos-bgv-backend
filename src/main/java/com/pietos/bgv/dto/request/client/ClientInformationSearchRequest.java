package com.pietos.bgv.dto.request.client;

import java.time.LocalDate;

import com.pietos.bgv.enums.ClientStatus;

public class ClientInformationSearchRequest {

    private String clientCode;

    private String clientName;

    private String contactPerson;

    private String officialEmail;

    private String mobile;

    private String country;

    private String state;

    private String city;

    private ClientStatus status;
    
    private String abbreviation;

    private LocalDate clientSince;

    public ClientInformationSearchRequest() {
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getOfficialEmail() {
        return officialEmail;
    }

    public void setOfficialEmail(String officialEmail) {
        this.officialEmail = officialEmail;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ClientStatus getStatus() {
        return status;
    }

    public void setStatus(ClientStatus status) {
        this.status = status;
    }

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public LocalDate getClientSince() {
		return clientSince;
	}

	public void setClientSince(LocalDate clientSince) {
		this.clientSince = clientSince;
	}
    
    
}