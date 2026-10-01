package com.pietos.bgv.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pietos.bgv.entity.NationalHoliday;

public interface NationalHolidayRepository
        extends JpaRepository<NationalHoliday, Long> {

    // Check Duplicate Holiday
    boolean existsByHolidayDate(
            LocalDate holidayDate);

    // Find Holiday By Date
    Optional<NationalHoliday> findByHolidayDate(
            LocalDate holidayDate);

    // Get Holidays Between Two Dates
    List<NationalHoliday> findByHolidayDateBetween(
            LocalDate startDate,
            LocalDate endDate);

    // Get All Holidays In Ascending Order
    List<NationalHoliday> findAllByOrderByHolidayDateAsc();
    
    
}