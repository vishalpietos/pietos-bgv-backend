package com.pietos.bgv.service.impl.cases;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pietos.bgv.dto.response.cases.CaseHistoryResponse;
import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.entity.cases.Case;
import com.pietos.bgv.entity.cases.CaseHistory;
import com.pietos.bgv.entity.cases.CasePackageComponent;
import com.pietos.bgv.exception.ResourceNotFoundException;
import com.pietos.bgv.repository.cases.CaseHistoryRepository;
import com.pietos.bgv.repository.cases.CaseRepository;
import com.pietos.bgv.security.LoggedInUserService;
import com.pietos.bgv.service.cases.CaseHistoryService;

@Service
@Transactional
public class CaseHistoryServiceImpl implements CaseHistoryService {

    private final CaseHistoryRepository caseHistoryRepository;
    private final CaseRepository caseRepository;
    private final LoggedInUserService loggedInUserService;

    public CaseHistoryServiceImpl(
            CaseHistoryRepository caseHistoryRepository,
            CaseRepository caseRepository,
            LoggedInUserService loggedInUserService) {

        this.caseHistoryRepository = caseHistoryRepository;
        this.caseRepository = caseRepository;
        this.loggedInUserService = loggedInUserService;
    }


    // =========================================================
    // CASE-LEVEL HISTORY
    // =========================================================

    @Override
    public void createHistory(
            Case caseEntity,
            String status,
            String activity) {

        SystemUser loggedInUser = loggedInUserService.getLoggedInUser();

        CaseHistory history = new CaseHistory();

        history.setCaseEntity(caseEntity);

        // Case-level history
        history.setCasePackageComponent(null);
        history.setSubCaseId(null);

        history.setStatus(status);
        history.setActivity(activity);
        history.setPerformedBy(loggedInUser);

        caseHistoryRepository.save(history);
    }


    // =========================================================
    // CHECK-LEVEL HISTORY
    // =========================================================

    @Override
    public void createHistory(
            Case caseEntity,
            CasePackageComponent casePackageComponent,
            String subCaseId,
            String status,
            String activity) {

        SystemUser loggedInUser = loggedInUserService.getLoggedInUser();

        CaseHistory history = new CaseHistory();

        history.setCaseEntity(caseEntity);

        // Particular check instance
        history.setCasePackageComponent(casePackageComponent);

        // Actual verification record ID
        // Example: PT/ED/0001
        history.setSubCaseId(subCaseId);

        history.setStatus(status);
        history.setActivity(activity);
        history.setPerformedBy(loggedInUser);

        caseHistoryRepository.save(history);
    }


    // =========================================================
    // GET COMPLETE CASE HISTORY
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<CaseHistoryResponse> getHistoryByCaseId(
            Long caseId) {

        validateCaseExists(caseId);

        return caseHistoryRepository
                .findByCaseEntityIdOrderByCreatedAtDesc(caseId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    // =========================================================
    // GET PARTICULAR CHECK HISTORY
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<CaseHistoryResponse> getHistoryByCaseIdAndSubCaseId(
            Long caseId,
            String subCaseId) {

        validateCaseExists(caseId);

        return caseHistoryRepository
                .findByCaseEntityIdAndSubCaseIdOrderByCreatedAtDesc(
                        caseId,
                        subCaseId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    // =========================================================
    // GET ONLY CASE-LEVEL HISTORY
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<CaseHistoryResponse> getCaseLevelHistory(
            Long caseId) {

        validateCaseExists(caseId);

        return caseHistoryRepository
                .findByCaseEntityIdAndSubCaseIdIsNullOrderByCreatedAtDesc(
                        caseId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    // =========================================================
    // VALIDATE CASE
    // =========================================================

    private void validateCaseExists(Long caseId) {

        if (!caseRepository.existsById(caseId)) {

            throw new ResourceNotFoundException(
                    "Case not found with id : " + caseId);
        }
    }


    // =========================================================
    // MAP ENTITY TO RESPONSE
    // =========================================================

    private CaseHistoryResponse mapToResponse(
            CaseHistory history) {

        CaseHistoryResponse response =
                new CaseHistoryResponse();

        response.setId(history.getId());

        response.setCaseId(
                history.getCaseEntity().getId());

        // Particular check instance
        if (history.getCasePackageComponent() != null) {

            response.setCasePackageComponentId(
                    history.getCasePackageComponent().getId());
        }

        // Actual verification record
        response.setSubCaseId(
                history.getSubCaseId());

        response.setStatus(
                history.getStatus());

        response.setActivity(
                history.getActivity());


        if (history.getPerformedBy() != null) {

            response.setPerformedBy(
                    history.getPerformedBy().getId());

            response.setPerformedByName(
                    getUserFullName(
                            history.getPerformedBy()));
        }

        response.setCreatedAt(
                history.getCreatedAt());

        return response;
    }


    // =========================================================
    // GET USER FULL NAME
    // =========================================================

    private String getUserFullName(
            SystemUser user) {

        String firstName =
                user.getFirstName();

        String lastName =
                user.getLastName();

        if (lastName == null
                || lastName.isBlank()) {

            return firstName;
        }

        return firstName + " " + lastName;
    }
}