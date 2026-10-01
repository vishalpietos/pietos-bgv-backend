package com.pietos.bgv.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pietos.bgv.dto.request.NationalHolidayRequest;
import com.pietos.bgv.dto.response.NationalHolidayResponse;
import com.pietos.bgv.entity.NationalHoliday;
import com.pietos.bgv.exception.DuplicateResourceException;
import com.pietos.bgv.exception.ResourceNotFoundException;
import com.pietos.bgv.repository.NationalHolidayRepository;
import com.pietos.bgv.service.NationalHolidayService;

@Service
@Transactional
public class NationalHolidayServiceImpl
        implements NationalHolidayService {

    private final NationalHolidayRepository nationalHolidayRepository;

    public NationalHolidayServiceImpl(
            NationalHolidayRepository nationalHolidayRepository) {

        this.nationalHolidayRepository =
                nationalHolidayRepository;
    }

    // =========================================================
    // ADD HOLIDAY
    // =========================================================

    @Override
    public NationalHolidayResponse addHoliday(
            NationalHolidayRequest request) {

        // Validate date
        if (request.getHolidayDate() == null) {

            throw new IllegalArgumentException(
                    "Holiday date is required.");
        }

        // Validate holiday name
        if (request.getHolidayName() == null
                || request.getHolidayName().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Holiday name is required.");
        }

        // Check duplicate
        boolean exists =
                nationalHolidayRepository
                        .existsByHolidayDate(
                                request.getHolidayDate());

        if (exists) {

            throw new DuplicateResourceException(
                    "Holiday already exists for date : "
                            + request.getHolidayDate());
        }

        // Create entity
        NationalHoliday holiday =
                new NationalHoliday();

        holiday.setHolidayDate(
                request.getHolidayDate());

        holiday.setHolidayName(
                request.getHolidayName().trim());

        // Save
        NationalHoliday savedHoliday =
                nationalHolidayRepository.save(holiday);

        return mapToResponse(savedHoliday);
    }

    // =========================================================
    // GET ALL HOLIDAYS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<NationalHolidayResponse> getAllHolidays() {

        return nationalHolidayRepository
                .findAllByOrderByHolidayDateAsc()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================================================
    // GET HOLIDAYS BY YEAR
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<NationalHolidayResponse> getHolidaysByYear(
            int year) {

        LocalDate startDate =
                LocalDate.of(year, 1, 1);

        LocalDate endDate =
                LocalDate.of(year, 12, 31);

        return nationalHolidayRepository
                .findByHolidayDateBetween(
                        startDate,
                        endDate)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================================================
    // GET HOLIDAY BY DATE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public NationalHolidayResponse getHolidayByDate(
            LocalDate holidayDate) {

        if (holidayDate == null) {

            throw new IllegalArgumentException(
                    "Holiday date is required.");
        }

        NationalHoliday holiday =
                nationalHolidayRepository
                        .findByHolidayDate(holidayDate)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Holiday not found for date : "
                                                + holidayDate));

        return mapToResponse(holiday);
    }

    // =========================================================
    // UPDATE HOLIDAY
    // =========================================================

    @Override
    public NationalHolidayResponse updateHoliday(
            Long id,
            NationalHolidayRequest request) {

        // Find existing holiday
        NationalHoliday holiday =
                nationalHolidayRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Holiday not found with id : "
                                                + id));

        // Validate date
        if (request.getHolidayDate() == null) {

            throw new IllegalArgumentException(
                    "Holiday date is required.");
        }

        // Validate name
        if (request.getHolidayName() == null
                || request.getHolidayName().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Holiday name is required.");
        }

        // Check duplicate date
        boolean dateChanged =
                !request.getHolidayDate()
                        .equals(holiday.getHolidayDate());

        if (dateChanged) {

            boolean exists =
                    nationalHolidayRepository
                            .existsByHolidayDate(
                                    request.getHolidayDate());

            if (exists) {

                throw new DuplicateResourceException(
                        "Holiday already exists for date : "
                                + request.getHolidayDate());
            }
        }

        // Update
        holiday.setHolidayDate(
                request.getHolidayDate());

        holiday.setHolidayName(
                request.getHolidayName().trim());

        NationalHoliday updatedHoliday =
                nationalHolidayRepository.save(holiday);

        return mapToResponse(updatedHoliday);
    }

    // =========================================================
    // DELETE HOLIDAY
    // =========================================================

    @Override
    public void deleteHoliday(Long id) {

        NationalHoliday holiday =
                nationalHolidayRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Holiday not found with id : "
                                                + id));

        nationalHolidayRepository.delete(holiday);
    }

    // =========================================================
    // MAP ENTITY → RESPONSE
    // =========================================================

    private NationalHolidayResponse mapToResponse(
            NationalHoliday holiday) {

        NationalHolidayResponse response =
                new NationalHolidayResponse();

        response.setId(
                holiday.getId());

        response.setHolidayDate(
                holiday.getHolidayDate());

        response.setHolidayName(
                holiday.getHolidayName());

        return response;
    }
}