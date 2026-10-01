package com.pietos.bgv.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pietos.bgv.dto.request.NationalHolidayRequest;
import com.pietos.bgv.dto.response.ApiResponse;
import com.pietos.bgv.dto.response.NationalHolidayResponse;
import com.pietos.bgv.service.NationalHolidayService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/national-holidays")
@Tag(
    name = "National Holidays",
    description = "APIs for managing national holidays"
)
public class NationalHolidayController {

    private final NationalHolidayService nationalHolidayService;

    public NationalHolidayController(
            NationalHolidayService nationalHolidayService) {

        this.nationalHolidayService =
                nationalHolidayService;
    }

    // =========================================================
    // ADD HOLIDAY
    // =========================================================

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<ApiResponse<NationalHolidayResponse>>
            addHoliday(
                    @RequestBody NationalHolidayRequest request) {

        NationalHolidayResponse response =
                nationalHolidayService.addHoliday(request);

        ApiResponse<NationalHolidayResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "National holiday added successfully.",
                        response);

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.CREATED);
    }

    // =========================================================
    // GET ALL HOLIDAYS
    // =========================================================

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<ApiResponse<List<NationalHolidayResponse>>>
            getAllHolidays() {

        List<NationalHolidayResponse> response =
                nationalHolidayService.getAllHolidays();

        ApiResponse<List<NationalHolidayResponse>> apiResponse =
                new ApiResponse<>(
                        true,
                        "National holidays fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    // =========================================================
    // GET HOLIDAYS BY YEAR
    // =========================================================

    @GetMapping("/year/{year}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<ApiResponse<List<NationalHolidayResponse>>>
            getHolidaysByYear(
                    @PathVariable int year) {

        List<NationalHolidayResponse> response =
                nationalHolidayService.getHolidaysByYear(year);

        ApiResponse<List<NationalHolidayResponse>> apiResponse =
                new ApiResponse<>(
                        true,
                        "National holidays fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    // =========================================================
    // GET HOLIDAY BY DATE
    // =========================================================

    @GetMapping("/date/{holidayDate}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<ApiResponse<NationalHolidayResponse>>
            getHolidayByDate(
                    @PathVariable LocalDate holidayDate) {

        NationalHolidayResponse response =
                nationalHolidayService.getHolidayByDate(
                        holidayDate);

        ApiResponse<NationalHolidayResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "National holiday fetched successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    // =========================================================
    // UPDATE HOLIDAY
    // =========================================================

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<ApiResponse<NationalHolidayResponse>>
            updateHoliday(
                    @PathVariable Long id,
                    @RequestBody NationalHolidayRequest request) {

        NationalHolidayResponse response =
                nationalHolidayService.updateHoliday(
                        id,
                        request);

        ApiResponse<NationalHolidayResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "National holiday updated successfully.",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

    // =========================================================
    // DELETE HOLIDAY
    // =========================================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<ApiResponse<Void>>
            deleteHoliday(
                    @PathVariable Long id) {

        nationalHolidayService.deleteHoliday(id);

        ApiResponse<Void> apiResponse =
                new ApiResponse<>(
                        true,
                        "National holiday deleted successfully.",
                        null);

        return ResponseEntity.ok(apiResponse);
    }
}