package com.pietos.bgv.service;

import java.time.LocalDate;
import java.util.List;

import com.pietos.bgv.dto.request.NationalHolidayRequest;
import com.pietos.bgv.dto.response.NationalHolidayResponse;

public interface NationalHolidayService {

    // Add a holiday
    NationalHolidayResponse addHoliday(
            NationalHolidayRequest request);

    // Get all holidays
    List<NationalHolidayResponse> getAllHolidays();

    // Get holidays for a particular year
    List<NationalHolidayResponse> getHolidaysByYear(
            int year);

    // Get holiday by date
    NationalHolidayResponse getHolidayByDate(
            LocalDate holidayDate);

    // Update holiday
    NationalHolidayResponse updateHoliday(
            Long id,
            NationalHolidayRequest request);

    // Delete holiday
    void deleteHoliday(Long id);
}