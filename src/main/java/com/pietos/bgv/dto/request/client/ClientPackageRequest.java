package com.pietos.bgv.dto.request.client;

import java.math.BigDecimal;
import java.util.List;

import com.pietos.bgv.enums.HolidayType;
import com.pietos.bgv.enums.PackageType;

public class ClientPackageRequest {

    private Long clientId;

    private String packageName;

    private PackageType packageType;

    private Integer packageTat;

    private Integer internalTat;

    private BigDecimal packageRate;

    private HolidayType holidayType;
    
    private Boolean isActive;
    
    

    private List<ClientPackageComponentRequest> components;


    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public Integer getPackageTat() {
        return packageTat;
    }

    public void setPackageTat(Integer packageTat) {
        this.packageTat = packageTat;
    }

    public Integer getInternalTat() {
        return internalTat;
    }

    public void setInternalTat(Integer internalTat) {
        this.internalTat = internalTat;
    }

    public BigDecimal getPackageRate() {
        return packageRate;
    }

    public void setPackageRate(BigDecimal packageRate) {
        this.packageRate = packageRate;
    }

    public HolidayType getHolidayType() {
        return holidayType;
    }

    public void setHolidayType(HolidayType holidayType) {
        this.holidayType = holidayType;
    }

	public List<ClientPackageComponentRequest> getComponents() {
		return components;
	}

	public void setComponents(List<ClientPackageComponentRequest> components) {
		this.components = components;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	
   
}