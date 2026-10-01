package com.pietos.bgv.service.impl.cases;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pietos.bgv.dto.request.cases.CasePackageRequest;
import com.pietos.bgv.dto.response.cases.CasePackageComponentResponse;
import com.pietos.bgv.dto.response.cases.CasePackageResponse;
import com.pietos.bgv.entity.ClientPackage;
import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.entity.cases.Case;
import com.pietos.bgv.entity.cases.CasePackage;
import com.pietos.bgv.exception.ResourceNotFoundException;
import com.pietos.bgv.repository.ClientPackageRepository;
import com.pietos.bgv.repository.cases.CasePackageRepository;
import com.pietos.bgv.repository.cases.CaseRepository;
import com.pietos.bgv.security.LoggedInUserService;
import com.pietos.bgv.service.cases.CasePackageComponentService;
import com.pietos.bgv.service.cases.CasePackageService;

@Service
@Transactional
public class CasePackageServiceImpl implements CasePackageService {

    private final CasePackageRepository casePackageRepository;

    private final CaseRepository caseRepository;

    private final ClientPackageRepository clientPackageRepository;

    private final LoggedInUserService loggedInUserService;

    private final CasePackageComponentService casePackageComponentService;


    public CasePackageServiceImpl(
            CasePackageRepository casePackageRepository,
            CaseRepository caseRepository,
            ClientPackageRepository clientPackageRepository,
            LoggedInUserService loggedInUserService,
            CasePackageComponentService casePackageComponentService) {

        this.casePackageRepository = casePackageRepository;
        this.caseRepository = caseRepository;
        this.clientPackageRepository = clientPackageRepository;
        this.loggedInUserService = loggedInUserService;
        this.casePackageComponentService = casePackageComponentService;
    }


    // =====================================================
    // CREATE CASE PACKAGE
    // =====================================================

    @Override
    public CasePackageResponse createCasePackage(
            CasePackageRequest request) {

        if (request.getCaseId() == null) {
            throw new IllegalArgumentException(
                    "Case ID is required.");
        }

        if (request.getPackageId() == null) {
            throw new IllegalArgumentException(
                    "Package ID is required.");
        }

        if (casePackageRepository
                .existsByCaseEntityId(request.getCaseId())) {

            throw new IllegalArgumentException(
                    "Package is already assigned to this case.");
        }

        // Find Case
        Case caseEntity =
                caseRepository
                        .findById(request.getCaseId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Case not found with id : "
                                                + request.getCaseId()));

        // Find Package
        ClientPackage clientPackage =
                clientPackageRepository
                        .findById(request.getPackageId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Package not found with id : "
                                                + request.getPackageId()));

        // Logged-in User
        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();

        LocalDateTime now =
                LocalDateTime.now();

        // Create Case Package
        CasePackage casePackage =
                new CasePackage();

        casePackage.setCaseEntity(caseEntity);
        casePackage.setClientPackage(clientPackage);
        casePackage.setCreatedBy(loggedInUser);
        casePackage.setCreatedAt(now);
        casePackage.setUpdatedBy(loggedInUser);
        casePackage.setUpdatedAt(now);

        CasePackage savedCasePackage =
                casePackageRepository.save(casePackage);

        // =====================================================
        // COPY ALL PACKAGE COMPONENTS
        // =====================================================

        casePackageComponentService
                .copyPackageComponentsToCasePackage(
                        savedCasePackage.getId(),
                        request.getPackageId());

        // =====================================================
        // ADDITIONAL COMPONENTS SELECTED BY USER
        // =====================================================

        if (request.getComponentIds() != null
                && !request.getComponentIds().isEmpty()) {

            casePackageComponentService.saveComponents(
                    savedCasePackage.getId(),
                    request.getComponentIds());
        }

        return mapToResponse(savedCasePackage);
    }

    // =====================================================
    // GET CASE PACKAGE BY CASE ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public CasePackageResponse getCasePackageByCaseId(
            Long caseId) {

        CasePackage casePackage =
                casePackageRepository
                        .findByCaseEntityId(caseId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Package not found for case id : "
                                                + caseId));


        return mapToResponse(casePackage);
    }


    // =====================================================
    // UPDATE CASE PACKAGE
    // =====================================================

    @Override
    public CasePackageResponse updateCasePackage(
            Long caseId,
            CasePackageRequest request) {

        CasePackage casePackage =
                casePackageRepository
                        .findByCaseEntityId(caseId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Package not found for case id : "
                                                + caseId));


        ClientPackage clientPackage = null;

        if (request.getPackageId() != null) {

            clientPackage =
                    clientPackageRepository
                            .findById(request.getPackageId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Package not found with id : "
                                                    + request.getPackageId()));
        }


        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();


        casePackage.setClientPackage(clientPackage);

        casePackage.setUpdatedBy(loggedInUser);

        casePackage.setUpdatedAt(LocalDateTime.now());


        CasePackage updatedCasePackage =
                casePackageRepository.save(casePackage);


        // -------------------------------------------------
        // REPLACE COMPONENTS
        // -------------------------------------------------

        casePackageComponentService
                .deleteComponentsByCasePackageId(
                        updatedCasePackage.getId());


        if (request.getComponentIds() != null
                && !request.getComponentIds().isEmpty()) {

            casePackageComponentService.saveComponents(
                    updatedCasePackage.getId(),
                    request.getComponentIds());
        }


        return mapToResponse(updatedCasePackage);
    }


    // =====================================================
    // MAP RESPONSE
    // =====================================================

    private CasePackageResponse mapToResponse(
            CasePackage casePackage) {

        CasePackageResponse response =
                new CasePackageResponse();

        response.setId(casePackage.getId());

        response.setCaseId(
                casePackage.getCaseEntity().getId());

        response.setCaseRef(
                casePackage.getCaseEntity().getCaseRef());


        if (casePackage.getClientPackage() != null) {

            response.setPackageId(
                    casePackage
                            .getClientPackage()
                            .getId());

            response.setPackageName(
                    casePackage
                            .getClientPackage()
                            .getPackageName());
        }


        List<CasePackageComponentResponse> components =
                casePackageComponentService
                        .getComponentsByCasePackageId(
                                casePackage.getId());


        response.setComponents(components);


        return response;
    }
}