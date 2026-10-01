package com.pietos.bgv.service.impl.cases;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pietos.bgv.dto.request.cases.CaseVendorResponseRequest;
import com.pietos.bgv.dto.response.cases.CaseEducationVendorResponseDTO;
import com.pietos.bgv.dto.response.cases.CaseEmploymentVendorResponseDTO;

import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.entity.cases.CaseEducationTable;
import com.pietos.bgv.entity.cases.CaseEducationVendorResponse;
import com.pietos.bgv.entity.cases.CaseEmploymentTable;
import com.pietos.bgv.entity.cases.CaseEmploymentVendorResponse;

import com.pietos.bgv.enums.EmploymentType;

import com.pietos.bgv.exception.ResourceNotFoundException;

import com.pietos.bgv.repository.cases.CaseEducationRepository;
import com.pietos.bgv.repository.cases.CaseEducationVendorResponseRepository;
import com.pietos.bgv.repository.cases.CaseEmploymentRepository;
import com.pietos.bgv.repository.cases.CaseEmploymentVendorResponseRepository;

import com.pietos.bgv.security.EncryptionService;
import com.pietos.bgv.security.LoggedInUserService;

import com.pietos.bgv.service.cases.CaseVendorResponseService;

@Service
public class CaseVendorResponseServiceImpl
        implements CaseVendorResponseService {

    private final CaseEducationRepository caseEducationRepository;

    private final CaseEmploymentRepository caseEmploymentRepository;

    private final CaseEducationVendorResponseRepository
            caseEducationVendorResponseRepository;

    private final CaseEmploymentVendorResponseRepository
            caseEmploymentVendorResponseRepository;

    private final LoggedInUserService loggedInUserService;

    private final EncryptionService encryptionService;

    public CaseVendorResponseServiceImpl(
            CaseEducationRepository caseEducationRepository,
            CaseEmploymentRepository caseEmploymentRepository,
            CaseEducationVendorResponseRepository caseEducationVendorResponseRepository,
            CaseEmploymentVendorResponseRepository caseEmploymentVendorResponseRepository,
            LoggedInUserService loggedInUserService,
            EncryptionService encryptionService) {

        this.caseEducationRepository = caseEducationRepository;

        this.caseEmploymentRepository = caseEmploymentRepository;

        this.caseEducationVendorResponseRepository = caseEducationVendorResponseRepository;

        this.caseEmploymentVendorResponseRepository = caseEmploymentVendorResponseRepository;

        this.loggedInUserService = loggedInUserService;

        this.encryptionService = encryptionService;
    }

    // =========================================================
    // EDUCATION
    // =========================================================

    @Override
    @Transactional
    public CaseEducationVendorResponseDTO saveEducationVendorResponse(
            Long caseEducationId,
            CaseVendorResponseRequest request) {

        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();

        if (loggedInUser == null) {
            throw new IllegalArgumentException(
                    "Logged-in user not found."
            );
        }

        validateRequest(request);

        // =====================================================
        // FIND EDUCATION CHECK
        // =====================================================

        CaseEducationTable caseEducation =
                caseEducationRepository.findById(caseEducationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Case education not found with id : "
                                                + caseEducationId
                                )
                        );

        // =====================================================
        // FIND EXISTING VENDOR RESPONSE
        // =====================================================

        CaseEducationVendorResponse vendorResponse =
                caseEducationVendorResponseRepository
                        .findByCaseEducationId(caseEducationId)
                        .orElse(null);

        // =====================================================
        // CREATE IF NOT EXISTS
        // =====================================================

        if (vendorResponse == null) {

            vendorResponse = new CaseEducationVendorResponse();
            vendorResponse.setCaseEducation(caseEducation);
            vendorResponse.setCreatedBy(loggedInUser);
            vendorResponse.setCreatedAt(LocalDateTime.now());
        }

        // =====================================================
        // UPDATE ONLY SELECTED FIELD
        // =====================================================

        String encryptedValue =
                encryptionService.encrypt(request.getValue());

        switch (request.getSelectedField()) {

            case "UNIVERSITY":

                vendorResponse.setUniversity(
                        encryptedValue);

                break;

            case "INSTITUTION":

                vendorResponse.setInstitution(
                        encryptedValue);

                break;

            case "REGISTRATION_NUMBER":

                vendorResponse.setRegistrationNumber(
                        encryptedValue);

                break;

            case "QUALIFICATION":

                vendorResponse.setQualification(
                        encryptedValue);

                break;

            case "YEAR_OF_PASSING":

                vendorResponse.setYearOfPassing(
                        encryptedValue);

                break;

            default:

                throw new IllegalArgumentException(
                        "Invalid education vendor field: "
                                + request.getSelectedField());
        }


        vendorResponse.setUpdatedBy(loggedInUser);

        vendorResponse.setUpdatedAt(LocalDateTime.now());

        CaseEducationVendorResponse savedResponse =caseEducationVendorResponseRepository.save(vendorResponse);

        return mapEducationResponse(savedResponse);
    }

    
    @Override
    @Transactional
    public CaseEmploymentVendorResponseDTO saveEmploymentVendorResponse(
            Long caseEmploymentId,
            CaseVendorResponseRequest request) {

        SystemUser loggedInUser =loggedInUserService.getLoggedInUser();

        if (loggedInUser == null) {
            throw new IllegalArgumentException(
                    "Logged-in user not found.");
        }

        validateRequest(request);

        CaseEmploymentTable caseEmployment =
                caseEmploymentRepository.findById(caseEmploymentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Case employment not found with id : "
                                                + caseEmploymentId ));


        CaseEmploymentVendorResponse vendorResponse =
                caseEmploymentVendorResponseRepository
                        .findByCaseEmploymentId(caseEmploymentId)
                        .orElse(null);


        if (vendorResponse == null) {

            vendorResponse = new CaseEmploymentVendorResponse();

            vendorResponse.setCaseEmployment(caseEmployment);

            vendorResponse.setCreatedBy(loggedInUser);

            vendorResponse.setCreatedAt(LocalDateTime.now());
        }


        String selectedField = request.getSelectedField();

        String value = request.getValue();

        switch (selectedField) {

            case "COMPANY_NAME":

                vendorResponse.setCompanyName(
                        encryptionService.encrypt(value));

                break;

            case "LOCATION":

                vendorResponse.setLocation(
                        encryptionService.encrypt(value));

                break;

            case "EMPLOYMENT_ID":

                vendorResponse.setEmploymentId(
                        encryptionService.encrypt(value));

                break;

            case "DESIGNATION":

                vendorResponse.setDesignation(
                        encryptionService.encrypt(value));

                break;

            case "EMPLOYMENT_START_DATE":

                vendorResponse.setEmploymentStartDate(
                        parseDate(value));

                break;

            case "EMPLOYMENT_END_DATE":

                vendorResponse.setEmploymentEndDate(
                        parseDate(value));

                break;

            case "SALARY":

                vendorResponse.setSalary(
                        parseSalary(value));

                break;

            case "EMPLOYMENT_TYPE":

                vendorResponse.setEmploymentType(
                        parseEmploymentType(value));

                break;

            case "REASON_FOR_LEAVING":

                vendorResponse.setReasonForLeaving(
                        encryptionService.encrypt(value));

                break;

            default:

                throw new IllegalArgumentException(
                        "Invalid employment vendor field: "
                                + selectedField);
        }

        vendorResponse.setUpdatedBy(loggedInUser);

        vendorResponse.setUpdatedAt(LocalDateTime.now());

        CaseEmploymentVendorResponse savedResponse = caseEmploymentVendorResponseRepository.save(vendorResponse);

        return mapEmploymentResponse(savedResponse);
    }


    private void validateRequest(CaseVendorResponseRequest request) {

        if (request == null) {

            throw new IllegalArgumentException(
                    "Vendor response request cannot be null.");
        }

        if (request.getSelectedField() == null || request.getSelectedField().isBlank()) {

            throw new IllegalArgumentException("Selected field is required.");
        }

        if (request.getValue() == null) {

            throw new IllegalArgumentException("Vendor value is required.");
        }
    }


    private CaseEducationVendorResponseDTO mapEducationResponse(CaseEducationVendorResponse vendorResponse) {

        CaseEducationVendorResponseDTO response = new CaseEducationVendorResponseDTO();
        response.setId(vendorResponse.getId());
        response.setCaseEducationId(vendorResponse.getCaseEducation().getId());
        response.setUniversity(decryptNullable(vendorResponse.getUniversity()));
        response.setInstitution( decryptNullable(vendorResponse.getInstitution()));
        response.setRegistrationNumber(decryptNullable(vendorResponse.getRegistrationNumber()));
        response.setQualification(decryptNullable(vendorResponse.getQualification()));
        response.setYearOfPassing(decryptNullable(vendorResponse.getYearOfPassing()));
        return response;
    }

    // =========================================================
    // EMPLOYMENT RESPONSE MAPPER
    // =========================================================

    private CaseEmploymentVendorResponseDTO mapEmploymentResponse(
            CaseEmploymentVendorResponse vendorResponse) {

        CaseEmploymentVendorResponseDTO response =new CaseEmploymentVendorResponseDTO();

        response.setId(vendorResponse.getId());
        response.setCaseEmploymentId(vendorResponse.getCaseEmployment().getId());
        response.setCompanyName( decryptNullable(vendorResponse.getCompanyName()));
        response.setLocation(decryptNullable(vendorResponse.getLocation()));
        response.setEmploymentId(decryptNullable(vendorResponse.getEmploymentId()));
        response.setDesignation(decryptNullable(vendorResponse.getDesignation()));
        response.setEmploymentStartDate(vendorResponse.getEmploymentStartDate());
        response.setEmploymentEndDate(vendorResponse.getEmploymentEndDate());
        response.setSalary(vendorResponse.getSalary());
        response.setEmploymentType(vendorResponse.getEmploymentType());
        response.setReasonForLeaving(decryptNullable(vendorResponse.getReasonForLeaving()));
        return response;
    }

    // =========================================================
    // NULL-SAFE DECRYPT
    // =========================================================

    private String decryptNullable(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        return encryptionService.decrypt(value);
    }

    // =========================================================
    // DATE PARSER
    // Expected format: yyyy-MM-dd
    // =========================================================

    private LocalDate parseDate(String value) {

        try {

            return LocalDate.parse(value);

        } catch (Exception e) {

            throw new IllegalArgumentException(
                    "Invalid date format. Expected yyyy-MM-dd.");
        }
    }

    // =========================================================
    // SALARY PARSER
    // =========================================================

    private BigDecimal parseSalary(String value) {

        try {

            return new BigDecimal(value);

        } catch (Exception e) {

            throw new IllegalArgumentException(
                    "Invalid salary value.");
        }
    }


    private EmploymentType parseEmploymentType(
            String value) {

        try {

            return EmploymentType.valueOf(
                    value.trim().toUpperCase());

        } catch (Exception e) {

            throw new IllegalArgumentException(
                    "Invalid employment type: "+ value);
        }
    }
    
    
    @Override
    @Transactional(readOnly = true)
    public CaseEducationVendorResponseDTO getEducationVendorResponse(Long caseEducationId) {

        CaseEducationVendorResponse vendorResponse = caseEducationVendorResponseRepository
                        .findByCaseEducationId(caseEducationId)
                        .orElseThrow(() ->new ResourceNotFoundException("Education vendor response not found for case education id : " + caseEducationId));

        return mapEducationResponse(vendorResponse);
    }

    
    
    @Override
    @Transactional(readOnly = true)
    public CaseEmploymentVendorResponseDTO getEmploymentVendorResponse(
            Long caseEmploymentId) {

        CaseEmploymentVendorResponse vendorResponse =
                caseEmploymentVendorResponseRepository
                        .findByCaseEmploymentId(caseEmploymentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employment vendor response not found for case employment id : "
                                                + caseEmploymentId));

        return mapEmploymentResponse(vendorResponse);
    }
    
}