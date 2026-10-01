package com.pietos.bgv.dto.request;



import java.time.LocalDate;

public class NationalHolidayRequest {

    private LocalDate holidayDate;

    private String holidayName;

    public NationalHolidayRequest() {
    }

    public LocalDate getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(LocalDate holidayDate) {
        this.holidayDate = holidayDate;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }
}
