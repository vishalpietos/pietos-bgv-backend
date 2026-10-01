package com.pietos.bgv.service.impl.cases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pietos.bgv.dto.response.cases.QCComponentLevelResponse;
import com.pietos.bgv.entity.InternalUserComponent;
import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.entity.cases.Case;
import com.pietos.bgv.entity.cases.CaseEducationTable;
import com.pietos.bgv.entity.cases.CaseEmploymentTable;
import com.pietos.bgv.entity.cases.CasePackageComponent;
import com.pietos.bgv.enums.ComponentSubStatus;
import com.pietos.bgv.repository.InternalUserComponentRepository;
import com.pietos.bgv.repository.cases.CaseEducationRepository;
import com.pietos.bgv.repository.cases.CaseEmploymentRepository;
import com.pietos.bgv.repository.cases.CasePackageComponentRepository;
import com.pietos.bgv.repository.cases.CaseRepository;
import com.pietos.bgv.security.LoggedInUserService;
import com.pietos.bgv.service.cases.QCComponentLevelService;

@Service
public class QCComponentLevelServiceImpl
        implements QCComponentLevelService {

    private final LoggedInUserService loggedInUserService;
    private final InternalUserComponentRepository internalUserComponentRepository;
    private final CasePackageComponentRepository casePackageComponentRepository;
    private final CaseEducationRepository caseEducationRepository;
    private final CaseEmploymentRepository caseEmploymentRepository;
    private final CaseRepository caseRepository;

    public QCComponentLevelServiceImpl(
            LoggedInUserService loggedInUserService,
            InternalUserComponentRepository internalUserComponentRepository,
            CasePackageComponentRepository casePackageComponentRepository,
            CaseEducationRepository caseEducationRepository,
            CaseEmploymentRepository caseEmploymentRepository,
            CaseRepository caseRepository) {

        this.loggedInUserService = loggedInUserService;
        this.internalUserComponentRepository =
                internalUserComponentRepository;
        this.casePackageComponentRepository =
                casePackageComponentRepository;
        this.caseEducationRepository =
                caseEducationRepository;
        this.caseEmploymentRepository =
                caseEmploymentRepository;
        this.caseRepository = caseRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<QCComponentLevelResponse> getComponentLevelCases() {

        SystemUser loggedInUser = loggedInUserService.getLoggedInUser();

        if (loggedInUser == null) {
            throw new IllegalArgumentException(
                    "Logged-in user not found.");
            }

        List<InternalUserComponent> userComponents = internalUserComponentRepository.findBySystemUserIdAndIsActiveTrue(loggedInUser.getId());

        Set<Long> allowedComponentIds = userComponents.stream()
                       					.filter(item -> item.getComponent() != null)
                       					.filter(item ->Boolean.TRUE.equals(item.getComponent().getIsActive())).map(item ->item.getComponent().getId()).collect(Collectors.toSet());

        if (allowedComponentIds.isEmpty()) {
            return List.of();
        }


        List<CasePackageComponent> packageComponents = casePackageComponentRepository
        												.findAll().stream()
                        								.filter(item -> item.getComponent() != null)
                        								.filter(item ->allowedComponentIds.contains(item.getComponent().getId())).collect(Collectors.toList());

        if (packageComponents.isEmpty()) {
            return List.of();
        }

        Set<Long> packageComponentIds = packageComponents.stream().map(CasePackageComponent::getId).collect(Collectors.toSet());

        // =====================================================
        // 4. EDUCATION - COMPLETE SEND TO QC
        // =====================================================

        List<CaseEducationTable> educationCases = caseEducationRepository
                        .findByCasePackageComponentIdInAndProcessingStatusOrderBySubCaseIdDesc(
                                packageComponentIds,
                                ComponentSubStatus.COMPLETE_SEND_TO_QC);

        // =====================================================
        // 5. EMPLOYMENT - COMPLETE SEND TO QC
        // =====================================================

        List<CaseEmploymentTable> employmentCases =
                caseEmploymentRepository
                        .findByCasePackageComponentIdInAndProcessingStatusOrderBySubCaseIdDesc(
                                packageComponentIds,
                                ComponentSubStatus.COMPLETE_SEND_TO_QC);

        // =====================================================
        // 6. CASE PACKAGE COMPONENT MAP
        // =====================================================

        Map<Long, CasePackageComponent> packageComponentMap =
                packageComponents.stream()
                        .collect(Collectors.toMap(
                                CasePackageComponent::getId,
                                item -> item));

        // =====================================================
        // 7. COLLECT CASE IDS
        // =====================================================

        Set<Long> caseIds = educationCases.stream()
                .map(CaseEducationTable::getCaseId)
                .collect(Collectors.toSet());

        caseIds.addAll(
                employmentCases.stream()
                        .map(CaseEmploymentTable::getCaseId)
                        .collect(Collectors.toSet()));

        // =====================================================
        // 8. CASE MAP
        // =====================================================

        Map<Long, Case> caseMap = new HashMap<>();

        for (Case caseEntity :
                caseRepository.findAllById(caseIds)) {

            caseMap.put(caseEntity.getId(),caseEntity);
        }

        // =====================================================
        // 9. ONE COMMON RESPONSE LIST
        // =====================================================

        List<QCComponentLevelResponse> result =
                new ArrayList<>();

        // Education
        for (CaseEducationTable education : educationCases) {

            CasePackageComponent packageComponent = packageComponentMap.get(education.getCasePackageComponentId());

            Case caseEntity = caseMap.get( education.getCaseId());

            if (packageComponent == null || caseEntity == null) {
                continue;
            }

            result.add(mapEducation(
                            education,
                            caseEntity,
                            packageComponent));
        }

        // Employment
        for (CaseEmploymentTable employment : employmentCases) {

            CasePackageComponent packageComponent =packageComponentMap.get(employment.getCasePackageComponentId());

            Case caseEntity =caseMap.get(employment.getCaseId());

            if (packageComponent == null || caseEntity == null) {
                continue;
            }

            result.add(mapEmployment(employment,caseEntity,packageComponent));
        }

        return result;
    }

    private QCComponentLevelResponse mapEducation(
            CaseEducationTable education,
            Case caseEntity,
            CasePackageComponent packageComponent) {

        QCComponentLevelResponse response =
                new QCComponentLevelResponse();

        response.setCheckId(education.getId());
        response.setCaseId(education.getCaseId());
        response.setCaseRef(caseEntity.getCaseRef());
        response.setSubRefNo(education.getSubCaseId());
        response.setComponentName(packageComponent.getComponent().getComponentName());
        response.setStatus(education.getProcessingStatus().name());
        response.setCandidateName(buildCandidateName(caseEntity));
        response.setFatherName(caseEntity.getFatherName());
        response.setClientEmployeeId(caseEntity.getClientEmployeeId());
        response.setCaseReceivedDate(caseEntity.getCaseReceivedDate());
        response.setClientNameLocation(buildClientLocation(caseEntity));
        response.setCaseDueDate(caseEntity.getCaseDueDate());
        response.setCaseInDate(caseEntity.getCaseInDate());
        response.setComponentDueDate(education.getComponentDueDate());
        response.setDispositionStatus(education.getDispositionStatus() != null? education.getDispositionStatus().name(): null);
        return response;
    }

    private QCComponentLevelResponse mapEmployment(CaseEmploymentTable employment,
            Case caseEntity,
            CasePackageComponent packageComponent) {

        QCComponentLevelResponse response = new QCComponentLevelResponse();

        response.setCheckId(employment.getId());
        response.setCaseId(employment.getCaseId());
        response.setCaseRef(caseEntity.getCaseRef());
        response.setSubRefNo(employment.getSubCaseId());
        response.setComponentName(packageComponent.getComponent().getComponentName());
        response.setStatus(employment.getProcessingStatus().name());
        response.setCandidateName(buildCandidateName(caseEntity));
        response.setFatherName(caseEntity.getFatherName());
        response.setClientEmployeeId(caseEntity.getClientEmployeeId());
        response.setCaseReceivedDate(caseEntity.getCaseReceivedDate());
        response.setClientNameLocation(buildClientLocation(caseEntity));
        response.setCaseDueDate(caseEntity.getCaseDueDate());
        response.setCaseInDate(caseEntity.getCaseInDate());
        response.setComponentDueDate(employment.getComponentDueDate());
        response.setDispositionStatus(employment.getDispositionStatus() != null? employment.getDispositionStatus().name(): null);
        return response;
    }

    private String buildCandidateName(Case caseEntity) {

        return String.join(
                " ",
                caseEntity.getFirstName() != null
                        ? caseEntity.getFirstName()
                        : "",
                caseEntity.getMiddleName() != null
                        ? caseEntity.getMiddleName()
                        : "",
                caseEntity.getLastName() != null
                        ? caseEntity.getLastName()
                        : ""
        ).trim().replaceAll("\\s+", " ");
    }

    private String buildClientLocation(Case caseEntity) {

    	String clientName =
    	        caseEntity.getClientInformation() != null
    	                ? caseEntity.getClientInformation().getClientName()
    	                : "";

    	String locationName =
    	        caseEntity.getLocation() != null
    	                ? caseEntity.getLocation().getLocationName()
    	                : "";

    	if (!clientName.isBlank() && !locationName.isBlank()) {
    	    return clientName + " / " + locationName;
    	}

    	if (!clientName.isBlank()) {
    	    return clientName;
    	}

    	return locationName;
    }
}