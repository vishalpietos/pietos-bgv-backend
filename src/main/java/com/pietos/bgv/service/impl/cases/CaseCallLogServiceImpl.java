package com.pietos.bgv.service.impl.cases;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pietos.bgv.dto.request.cases.CaseCallLogRequest;
import com.pietos.bgv.dto.response.cases.CaseCallLogResponse;
import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.entity.cases.Case;
import com.pietos.bgv.entity.cases.CaseCallLog;
import com.pietos.bgv.entity.cases.CasePackageComponent;
import com.pietos.bgv.exception.ResourceNotFoundException;
import com.pietos.bgv.repository.cases.CaseCallLogRepository;
import com.pietos.bgv.repository.cases.CasePackageComponentRepository;
import com.pietos.bgv.repository.cases.CaseRepository;
import com.pietos.bgv.security.LoggedInUserService;
import com.pietos.bgv.service.cases.CaseCallLogService;

@Service
@Transactional
public class CaseCallLogServiceImpl implements CaseCallLogService {

    private final CaseCallLogRepository caseCallLogRepository;
    private final CaseRepository caseRepository;
    private final CasePackageComponentRepository casePackageComponentRepository;
    private final LoggedInUserService loggedInUserService;

    public CaseCallLogServiceImpl(
            CaseCallLogRepository caseCallLogRepository,
            CaseRepository caseRepository,
            CasePackageComponentRepository casePackageComponentRepository,
            LoggedInUserService loggedInUserService) {

        this.caseCallLogRepository = caseCallLogRepository;
        this.caseRepository = caseRepository;
        this.casePackageComponentRepository = casePackageComponentRepository;
        this.loggedInUserService = loggedInUserService;
    }


    // =====================================================
    // CREATE CALL LOG
    // =====================================================

    @Override
    public CaseCallLogResponse createCallLog(Long caseId,CaseCallLogRequest request) {

        // -------------------------------------------------
        // VALIDATION
        // -------------------------------------------------

        if (caseId == null) {
            throw new IllegalArgumentException(
                    "Case ID is required."
            );
        }

        if (request == null) {
            throw new IllegalArgumentException(
                    "Call log request is required."
            );
        }

        if (request.getCasePackageComponentId() == null) {
            throw new IllegalArgumentException(
                    "Case package component ID is required."
            );
        }

        if (request.getSubCaseId() == null
                || request.getSubCaseId().isBlank()) {

            throw new IllegalArgumentException(
                    "Sub case ID is required."
            );
        }

        if (request.getActivity() == null
                || request.getActivity().isBlank()) {

            throw new IllegalArgumentException(
                    "Activity is required."
            );
        }


        Case caseEntity =
                caseRepository.findById(caseId).orElseThrow(() ->new ResourceNotFoundException("Case not found with id : "+ caseId));

        CasePackageComponent casePackageComponent =
                casePackageComponentRepository.findById(
                        request.getCasePackageComponentId())
                .orElseThrow(() ->new ResourceNotFoundException("Case package component not found with id : "
                                        + request.getCasePackageComponentId()));


        SystemUser loggedInUser = loggedInUserService.getLoggedInUser();

        if (loggedInUser == null) {
            throw new IllegalArgumentException(
                    "Logged-in user not found.");
        }


        LocalDateTime now = LocalDateTime.now();

        CaseCallLog callLog = new CaseCallLog();
        callLog.setCaseId(caseEntity.getId());
        callLog.setCasePackageComponentId(casePackageComponent.getId());
        callLog.setSubCaseId(request.getSubCaseId().trim());
        callLog.setConsiderAsAttempt(request.getConsiderAsAttempt() != null? request.getConsiderAsAttempt(): false);
        callLog.setActivity(request.getActivity().trim());
        callLog.setPerformedBy(loggedInUser);
        callLog.setCreatedAt(now);

        CaseCallLog savedCallLog = caseCallLogRepository.save(callLog);


        return mapToResponse(savedCallLog,casePackageComponent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CaseCallLogResponse> getCallLogsByCaseId(
            Long caseId) {

        if (caseId == null) {
            throw new IllegalArgumentException("Case ID is required.");
        }

        // Validate case exists
        if (!caseRepository.existsById(caseId)) {
            throw new ResourceNotFoundException("Case not found with id : " + caseId);
        }

        return caseCallLogRepository.findByCaseIdOrderByCreatedAtDesc(caseId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }


    // =====================================================
    // MAP ENTITY TO RESPONSE
    // =====================================================

    private CaseCallLogResponse mapToResponse(CaseCallLog callLog) 
    {

        CasePackageComponent casePackageComponent = casePackageComponentRepository.findById(callLog.getCasePackageComponentId()).orElse(null);

        return mapToResponse(callLog,casePackageComponent);
    }


    private CaseCallLogResponse mapToResponse(CaseCallLog callLog,CasePackageComponent casePackageComponent) {

        CaseCallLogResponse response =
                new CaseCallLogResponse();

        response.setId(callLog.getId());

        response.setCaseId(callLog.getCaseId());

        response.setCasePackageComponentId(callLog.getCasePackageComponentId());

        response.setSubCaseId(callLog.getSubCaseId());

        if (casePackageComponent != null
                && casePackageComponent.getComponent() != null) {

            response.setComponentName(casePackageComponent.getComponent().getComponentName());
        }

        response.setConsiderAsAttempt(callLog.getConsiderAsAttempt());

        response.setActivity(callLog.getActivity());


        SystemUser performedBy = callLog.getPerformedBy();

        if (performedBy != null) {

            String firstName =
                    performedBy.getFirstName() != null
                            ? performedBy.getFirstName().trim()
                            : "";

            String lastName =
                    performedBy.getLastName() != null
                            ? performedBy.getLastName().trim()
                            : "";

            String fullName =
                    (firstName + " " + lastName).trim();

            response.setPerformedByName(
                    fullName.isBlank()
                            ? String.valueOf(performedBy.getId())
                            : fullName);
        }

        response.setCreatedAt(callLog.getCreatedAt());

        return response;
    }
}