package com.pietos.bgv.dto.request.client;

import java.time.LocalDate;

import com.pietos.bgv.enums.HolidayType;

public class ClientHolidayConfigurationRequest {

    private Long clientId;

    private LocalDate wef;

    private LocalDate wet;

    private HolidayType holidayType;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
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

    public HolidayType getHolidayType() {
        return holidayType;
    }

    public void setHolidayType(HolidayType holidayType) {
        this.holidayType = holidayType;
    }
}