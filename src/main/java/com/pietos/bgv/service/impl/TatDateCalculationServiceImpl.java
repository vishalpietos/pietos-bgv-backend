package com.pietos.bgv.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pietos.bgv.enums.HolidayType;
import com.pietos.bgv.repository.NationalHolidayRepository;
import com.pietos.bgv.service.TatDateCalculationService;

@Service
@Transactional(readOnly = true)
public class TatDateCalculationServiceImpl
        implements TatDateCalculationService {

    private static final LocalTime TAT_CUTOFF_TIME =
            LocalTime.of(16, 30);

    private final NationalHolidayRepository nationalHolidayRepository;

    public TatDateCalculationServiceImpl(
            NationalHolidayRepository nationalHolidayRepository) {

        this.nationalHolidayRepository =
                nationalHolidayRepository;
    }

    @Override
    public LocalDate calculateEffectiveTo(
            LocalDateTime createdAt,
            Integer tatDays,
            HolidayType holidayType) {

        // =====================================================
        // VALIDATION
        // =====================================================

        if (createdAt == null) {
            throw new IllegalArgumentException(
                    "Created date and time is required.");
        }

        if (tatDays == null) {
            throw new IllegalArgumentException(
                    "TAT days are required.");
        }

        if (tatDays < 0) {
            throw new IllegalArgumentException(
                    "TAT days cannot be negative.");
        }

        if (holidayType == null) {
            throw new IllegalArgumentException(
                    "Holiday type is required.");
        }

        // =====================================================
        // INITIAL DATE
        // =====================================================

        LocalDate currentDate =
                createdAt.toLocalDate();

        boolean afterCutoff =
                createdAt.toLocalTime()
                        .isAfter(TAT_CUTOFF_TIME);

        // =====================================================
        // TAT = 0
        // =====================================================

        if (tatDays == 0) {

            /*
             * For zero TAT, return the first eligible date.
             *
             * Example:
             * Saturday + EXCLUDE_WEEKENDS
             * -> Monday
             */

            while (!isEligibleDate(
                    currentDate,
                    holidayType)) {

                currentDate = currentDate.plusDays(1);
            }

            return currentDate;
        }

        // =====================================================
        // AFTER 4:30 PM
        // =====================================================

        /*
         * If created after 4:30 PM,
         * the creation date cannot be counted.
         *
         * Start from next day.
         */
        if (afterCutoff) {
            currentDate = currentDate.plusDays(1);
        }

        // =====================================================
        // CALCULATE TAT
        // =====================================================

        int countedDays = 0;

        while (countedDays < tatDays) {

            if (isEligibleDate(
                    currentDate,
                    holidayType)) {

                countedDays++;

                /*
                 * Required TAT reached.
                 * Current date is the effective/due date.
                 */
                if (countedDays == tatDays) {
                    return currentDate;
                }
            }

            currentDate = currentDate.plusDays(1);
        }

        /*
         * This point should technically never be reached
         * because the while loop returns when TAT is completed.
         */
        throw new IllegalStateException(
                "Unable to calculate TAT effective date.");
    }

    // =========================================================
    // CHECK ELIGIBLE DATE
    // =========================================================

    private boolean isEligibleDate(
            LocalDate date,
            HolidayType holidayType) {

        // -----------------------------------------------------
        // CALENDAR DAYS
        // -----------------------------------------------------

        if (HolidayType.CALENDAR_DAYS.equals(
                holidayType)) {

            return true;
        }

        // -----------------------------------------------------
        // EXCLUDE WEEKENDS
        // -----------------------------------------------------

        if (HolidayType.EXCLUDE_WEEKENDS.equals(
                holidayType)) {

            return !isWeekend(date);
        }

        // -----------------------------------------------------
        // EXCLUDE WEEKENDS + BGV HOLIDAYS
        // -----------------------------------------------------

        if (HolidayType.EXCLUDE_WEEKENDS_AND_BGV_HOLIDAYS.equals(
                holidayType)) {

            // Saturday / Sunday
            if (isWeekend(date)) {
                return false;
            }

            // BGV / National Holiday
            return !nationalHolidayRepository
                    .findByHolidayDate(date)
                    .isPresent();
        }

        /*
         * Unknown holiday type should not silently
         * count the date.
         */
        throw new IllegalArgumentException(
                "Unsupported holiday type: " + holidayType);
    }

    // =========================================================
    // CHECK WEEKEND
    // =========================================================

    private boolean isWeekend(LocalDate date) {

        DayOfWeek day =
                date.getDayOfWeek();

        return day == DayOfWeek.SATURDAY
                || day == DayOfWeek.SUNDAY;
    }
}