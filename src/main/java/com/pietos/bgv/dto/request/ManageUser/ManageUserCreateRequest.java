package com.pietos.bgv.dto.request.ManageUser;

public class ManageUserCreateRequest {

    private Long clientId;

    private Long locationId;

    private Long roleId;

    private String firstName;

    private String lastName;

    private String email;

    private String mobile;
    
    private String city;
    
    private String state;
    	
    
    // Getters and Setters
    
    
    
    public Long getClientId() {
        return clientId;
    }

    public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	
    
    
}