package com.pietos.bgv.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.pietos.bgv.enums.HolidayType;

public interface TatDateCalculationService {

    LocalDate calculateEffectiveTo(
            LocalDateTime createdAt,
            Integer tatDays,
            HolidayType holidayType);
}