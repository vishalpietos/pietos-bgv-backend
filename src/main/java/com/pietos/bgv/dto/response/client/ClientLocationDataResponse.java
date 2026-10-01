package com.pietos.bgv.dto.response.client;

import java.util.List;

public class ClientLocationDataResponse {

    private Long clientId;

    private String clientName;

    private List<LocationData> locations;

    private List<RoleData> roles;
    
    private String state;

    private String city;
    
    
    


    // ================= CLIENT =================

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


    // ================= LOCATIONS =================

    public List<LocationData> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationData> locations) {
        this.locations = locations;
    }


    // ================= ROLES =================

    public List<RoleData> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleData> roles) {
        this.roles = roles;
    }


    // =====================================================
    // LOCATION DATA
    // =====================================================

    public static class LocationData {

        private Long id;

        private String locationName;

        private String state;

        private String city;


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
    }


    // =====================================================
    // ROLE DATA
    // =====================================================

    public static class RoleData {

        private Long id;

        private String roleName;


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }


        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }
    }
}