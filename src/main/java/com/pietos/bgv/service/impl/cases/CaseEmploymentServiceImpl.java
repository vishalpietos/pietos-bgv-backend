package com.pietos.bgv.service.impl.cases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pietos.bgv.dto.request.cases.CaseAssignmentRequest;
import com.pietos.bgv.dto.request.cases.CaseEmploymentRequest;
import com.pietos.bgv.dto.response.cases.CaseEmploymentMyCaseResponse;
import com.pietos.bgv.dto.response.cases.CaseEmploymentOpenCaseResponse;
import com.pietos.bgv.dto.response.cases.CaseEmploymentReassignResponse;
import com.pietos.bgv.dto.response.cases.CaseEmploymentResponse;
import com.pietos.bgv.entity.ClientPackage;
import com.pietos.bgv.entity.ClientPackageComponent;
import com.pietos.bgv.entity.Component;
import com.pietos.bgv.entity.InternalUserComponent;
import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.entity.cases.Case;
import com.pietos.bgv.entity.cases.CaseEducationTable;
import com.pietos.bgv.entity.cases.CaseEmploymentTable;
import com.pietos.bgv.entity.cases.CasePackage;
import com.pietos.bgv.entity.cases.CasePackageComponent;
import com.pietos.bgv.enums.CaseStatus;
import com.pietos.bgv.enums.ComponentSubStatus;
import com.pietos.bgv.enums.DataEntryStatus;
import com.pietos.bgv.enums.DispositionStatus;
import com.pietos.bgv.enums.PackageType;
import com.pietos.bgv.exception.ResourceNotFoundException;
import com.pietos.bgv.repository.InternalUserComponentRepository;
import com.pietos.bgv.repository.SystemUserRepository;
import com.pietos.bgv.repository.cases.CaseEmploymentRepository;
import com.pietos.bgv.repository.cases.CasePackageComponentRepository;
import com.pietos.bgv.repository.cases.CasePackageRepository;
import com.pietos.bgv.repository.cases.CaseRepository;
import com.pietos.bgv.security.EncryptionService;
import com.pietos.bgv.security.LoggedInUserService;
import com.pietos.bgv.service.TatDateCalculationService;
import com.pietos.bgv.service.cases.CaseEmploymentService;
import com.pietos.bgv.service.cases.CaseHistoryService;
import com.pietos.bgv.service.cases.CaseService;
import com.pietos.bgv.service.cases.CaseStatusService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Transactional
public class CaseEmploymentServiceImpl
        implements CaseEmploymentService {

    private final CaseEmploymentRepository caseEmploymentRepository;
    private final CaseRepository caseRepository;
    private final CasePackageRepository casePackageRepository;
    private final CasePackageComponentRepository casePackageComponentRepository;
    private final LoggedInUserService loggedInUserService;
    private final EncryptionService encryptionService;
    private final CaseHistoryService caseHistoryService;
    private final CaseStatusService caseStatusService;
    private final TatDateCalculationService tatDateCalculationService;
    private final SystemUserRepository systemUserRepository;
    private final InternalUserComponentRepository internalUserComponentRepository;
    private final CaseService caseService;

    public CaseEmploymentServiceImpl(
            CaseEmploymentRepository caseEmploymentRepository,
            CaseRepository caseRepository,
            CasePackageRepository casePackageRepository,
            CasePackageComponentRepository casePackageComponentRepository,
            LoggedInUserService loggedInUserService,
            EncryptionService encryptionService,
            CaseHistoryService caseHistoryService,
            CaseStatusService caseStatusService,
            TatDateCalculationService tatDateCalculationService,
            SystemUserRepository systemUserRepository,
            InternalUserComponentRepository internalUserComponentRepository,
            CaseService caseService) {

        this.caseEmploymentRepository = caseEmploymentRepository;
        this.caseRepository =caseRepository;
        this.casePackageRepository = casePackageRepository;
        this.casePackageComponentRepository =casePackageComponentRepository;
        this.loggedInUserService =loggedInUserService;
        this.encryptionService =encryptionService;
        this.caseHistoryService =caseHistoryService;
        this.caseStatusService =caseStatusService;
        this.tatDateCalculationService =tatDateCalculationService;
        this.systemUserRepository =systemUserRepository;
        this.internalUserComponentRepository = internalUserComponentRepository;
        this.caseService = caseService;
    }

    // =====================================================
    // CREATE EMPLOYMENT
    // =====================================================

    @Override
    public CaseEmploymentResponse createEmployment(
            CaseEmploymentRequest request) {

        // -------------------------------------------------
        // VALIDATION
        // -------------------------------------------------

        if (request.getCaseId() == null) {
            throw new IllegalArgumentException(
                    "Case ID is required.");
        }

        if (request.getCasePackageComponentId() == null) {
            throw new IllegalArgumentException(
                    "Case package component ID is required.");
        }

        if (request.getStatus() == null) {
            throw new IllegalArgumentException(
                    "Status is required.");
        }

        if (request.getEmploymentType() == null) {
            throw new IllegalArgumentException(
                    "Employment type is required.");
        }


        Case caseEntity = caseRepository.findById(
                        request.getCaseId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Case not found with id : "
                                                + request.getCaseId()));

        if (caseEntity.getCaseReceivedDate() == null) {
            throw new IllegalArgumentException(
                    "Case received date is required.");
        }


        CasePackageComponent casePackageComponent =
                casePackageComponentRepository
                        .findById(
                                request.getCasePackageComponentId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Case package component not found with id : "
                                                + request.getCasePackageComponentId()));

 

        CasePackage casePackage =
                casePackageComponent.getCasePackage();

        if (casePackage == null) {
            throw new ResourceNotFoundException( "Case package not found.");
        }

        ClientPackage clientPackage = casePackage.getClientPackage();

        if (clientPackage == null) {
            throw new ResourceNotFoundException("Client package not found.");
        }

        Component component = casePackageComponent.getComponent();

        if (component == null) {
            throw new ResourceNotFoundException("Component not found for case package component.");
        }

        Integer componentTatDays;

        if (PackageType.PACKAGE.equals(
                clientPackage.getPackageType())) {

            componentTatDays = clientPackage.getInternalTat();

            if (componentTatDays == null
                    || componentTatDays < 0) {

                throw new IllegalArgumentException("Internal TAT is not configured for this package.");
            }

        } else if (PackageType.INDIVIDUAL.equals(
                clientPackage.getPackageType())) {

            ClientPackageComponent clientPackageComponent = casePackageComponent.getClientPackageComponent();

            if (clientPackageComponent == null) {
                throw new ResourceNotFoundException("Client package component not found.");
            }

            componentTatDays = clientPackageComponent.getTatDays();

            if (componentTatDays == null
                    || componentTatDays < 0) {

                throw new IllegalArgumentException("TAT is not configured for component : " + component.getComponentName());
            }

        } else {
            throw new IllegalArgumentException("Invalid package type.");
        }

        LocalDate componentDueDate =tatDateCalculationService.calculateEffectiveTo(
                        caseEntity.getCaseReceivedDate()
                                .atStartOfDay(),
                        componentTatDays,
                        clientPackage.getHolidayType());

        LocalDate caseDueDate =caseStatusService.calculateCaseDueDate(caseEntity.getId());

        LocalDateTime now = LocalDateTime.now();

        SystemUser loggedInUser = loggedInUserService.getLoggedInUser();

        CaseEmploymentTable employment = new CaseEmploymentTable();
        employment.setCaseId(caseEntity.getId());
        employment.setCaseRef(caseEntity.getCaseRef());
        employment.setCasePackageComponentId(request.getCasePackageComponentId());
        employment.setComponentDueDate(componentDueDate);
        employment.setCompanyName(encryptionService.encrypt(request.getCompanyName()));
        employment.setLocation(encryptionService.encrypt(request.getLocation()));
        employment.setEmploymentId(encryptionService.encrypt(request.getEmploymentId()));
        employment.setDesignation(encryptionService.encrypt(request.getDesignation()));
        employment.setEmploymentStartDate(request.getEmploymentStartDate());
        employment.setEmploymentEndDate(request.getEmploymentEndDate());
        employment.setSalary(request.getSalary());
        employment.setEmploymentType(request.getEmploymentType());
        employment.setReasonForLeaving(encryptionService.encrypt(request.getReasonForLeaving()));
        employment.setInternational(request.getInternational());
        employment.setInternationalAmount(request.getInternationalAmount());
        employment.setAdditionalFeesRequired(request.getAdditionalFeesRequired());
        employment.setAdditionalFeesRequiredAmount(request.getAdditionalFeesRequiredAmount());
        employment.setStatus(request.getStatus());
        employment.setRemarks(request.getRemarks());  
        employment.setCreatedAt(now);
        employment.setUpdatedAt(now);
        employment.setCreatedBy(loggedInUser);
        employment.setUpdatedBy(loggedInUser);
        employment.setSubCaseId(generateEmploymentSubCaseId());

        CaseEmploymentTable savedEmployment =caseEmploymentRepository.save(employment);


        caseEntity.setCaseDueDate(caseDueDate);
        caseEntity.setUpdatedBy(loggedInUser);
        caseEntity.setUpdatedAt(now);
        Case savedCase = caseRepository.save(caseEntity);

        CaseStatus evaluatedCaseStatus = caseService.evaluateDataEntryStatus(savedCase.getId());

        if (savedCase.getStatus() != evaluatedCaseStatus) {

            savedCase.setStatus(evaluatedCaseStatus);
            savedCase.setUpdatedBy(loggedInUser);
            savedCase.setUpdatedAt(now);

            savedCase = caseRepository.save(savedCase);
        }

        caseHistoryService.createHistory(
                savedCase,
                casePackageComponent,
                savedEmployment.getSubCaseId(),
                savedEmployment.getStatus().name(),
                "Employment data entry completed"
        );

        return mapToResponse(savedEmployment);
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public CaseEmploymentResponse getEmploymentById(
            Long id) {

        CaseEmploymentTable employment =
                caseEmploymentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employment verification not found with id : "
                                                + id));

        return mapToResponse(employment);
    }

    // =====================================================
    // GET BY CASE ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<CaseEmploymentResponse> getEmploymentByCaseId(
            Long caseId) {

        return caseEmploymentRepository
                .findByCaseId(caseId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =====================================================
    // GET BY CASE REF
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<CaseEmploymentResponse> getEmploymentByCaseRef(
            String caseRef) {

        return caseEmploymentRepository
                .findByCaseRef(caseRef)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =====================================================
    // GET BY CASE PACKAGE COMPONENT ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<CaseEmploymentResponse>
            getEmploymentByCasePackageComponentId(
                    Long casePackageComponentId) {

        return caseEmploymentRepository
                .findByCasePackageComponentId(
                        casePackageComponentId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =====================================================
    // UPDATE EMPLOYMENT
    // =====================================================

    @Override
    public CaseEmploymentResponse updateEmployment(
            Long id,
            CaseEmploymentRequest request) {

        if (request.getStatus() == null) {
            throw new IllegalArgumentException(
                    "Status is required.");
        }

        CaseEmploymentTable employment =
                caseEmploymentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employment verification not found with id : "
                                                + id));

        // -------------------------------------------------
        // UPDATE EMPLOYMENT DETAILS
        // -------------------------------------------------

        employment.setCompanyName(
                encryptionService.encrypt(
                        request.getCompanyName()));

        employment.setLocation(
                encryptionService.encrypt(
                        request.getLocation()));

        employment.setEmploymentId(
                encryptionService.encrypt(
                        request.getEmploymentId()));

        employment.setDesignation(
                encryptionService.encrypt(
                        request.getDesignation()));

        employment.setEmploymentStartDate(
                request.getEmploymentStartDate());

        employment.setEmploymentEndDate(
                request.getEmploymentEndDate());

        employment.setSalary(
                request.getSalary());

        employment.setEmploymentType(
                request.getEmploymentType());

        employment.setReasonForLeaving(
                encryptionService.encrypt(
                        request.getReasonForLeaving()));

        employment.setInternational(
                request.getInternational());

        employment.setInternationalAmount(
                request.getInternationalAmount());

        employment.setAdditionalFeesRequired(
                request.getAdditionalFeesRequired());

        employment.setAdditionalFeesRequiredAmount(
                request.getAdditionalFeesRequiredAmount());

        employment.setStatus(
                request.getStatus());

        employment.setRemarks(
                request.getRemarks());

        // -------------------------------------------------
        // AUDIT
        // -------------------------------------------------

        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();

        LocalDateTime now =
                LocalDateTime.now();

        employment.setUpdatedAt(now);
        employment.setUpdatedBy(
                loggedInUser);

        // -------------------------------------------------
        // SAVE
        // -------------------------------------------------

        CaseEmploymentTable updatedEmployment =
                caseEmploymentRepository.save(
                        employment);

        // -------------------------------------------------
        // GET CASE
        // -------------------------------------------------

        Case caseEntity =
                caseRepository.findById(
                        employment.getCaseId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Case not found with id : "
                                                + employment.getCaseId()));

        caseEntity.setUpdatedBy(
                loggedInUser);

        caseEntity.setUpdatedAt(now);

        Case savedCase =
                caseRepository.save(caseEntity);

        // -------------------------------------------------
        // HISTORY
        // -------------------------------------------------

        caseHistoryService.createHistory(
                savedCase,
                request.getStatus().name(),
                "Employment data entry updated");

        return mapToResponse(
                updatedEmployment);
    }

    
    @Override
    public CaseEmploymentResponse updateSelectedEmploymentFields(
            Long id,
            CaseEmploymentRequest request) {

        CaseEmploymentTable employment =
                caseEmploymentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employment verification not found with id : "
                                                + id));

        /*
         * Update only the fields that are provided in the request.
         * Null means: do not update this field.
         */

        if (request.getCompanyName() != null) {
            employment.setCompanyName(encryptionService.encrypt(request.getCompanyName()));
        }

        if (request.getLocation() != null) {
            employment.setLocation(encryptionService.encrypt(request.getLocation()));
        }

        if (request.getEmploymentId() != null) {
            employment.setEmploymentId(encryptionService.encrypt(request.getEmploymentId()));
        }

        if (request.getDesignation() != null) {
            employment.setDesignation(encryptionService.encrypt(request.getDesignation()));
        }

        if (request.getEmploymentStartDate() != null) {
            employment.setEmploymentStartDate(request.getEmploymentStartDate());
        }

        if (request.getEmploymentEndDate() != null) {
            employment.setEmploymentEndDate(request.getEmploymentEndDate());
        }

        if (request.getSalary() != null) {
            employment.setSalary(request.getSalary());
        }

        if (request.getEmploymentType() != null) {
            employment.setEmploymentType(request.getEmploymentType());
        }

        if (request.getReasonForLeaving() != null) {
            employment.setReasonForLeaving(encryptionService.encrypt(request.getReasonForLeaving()));
        }

        if (request.getInternational() != null) {
            employment.setInternational(request.getInternational());
        }

        if (request.getInternationalAmount() != null) {
            employment.setInternationalAmount(request.getInternationalAmount());
        }

        if (request.getAdditionalFeesRequired() != null) {
            employment.setAdditionalFeesRequired(request.getAdditionalFeesRequired());
        }

        if (request.getAdditionalFeesRequiredAmount() != null) {
            employment.setAdditionalFeesRequiredAmount(request.getAdditionalFeesRequiredAmount());
        }

        SystemUser loggedInUser = loggedInUserService.getLoggedInUser();

        LocalDateTime now = LocalDateTime.now();

        employment.setUpdatedAt(now);
        employment.setUpdatedBy(loggedInUser);

        CaseEmploymentTable updatedEmployment = caseEmploymentRepository.save(employment);

        Case caseEntity =
                caseRepository.findById(
                        employment.getCaseId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Case not found with id : "
                                                + employment.getCaseId()));

        caseEntity.setUpdatedBy(loggedInUser);
        caseEntity.setUpdatedAt(now);

        caseRepository.save(caseEntity);

        return mapToResponse(updatedEmployment);
    }
    
    
    // =====================================================
    // MAP TO RESPONSE
    // =====================================================

    private CaseEmploymentResponse mapToResponse(
            CaseEmploymentTable employment) {

        CaseEmploymentResponse response =new CaseEmploymentResponse();

        response.setId(
                employment.getId());

        response.setCaseId(
                employment.getCaseId());

        response.setSubCaseId(
                employment.getSubCaseId());

        response.setCaseRef(
                employment.getCaseRef());

        response.setCasePackageComponentId(
                employment.getCasePackageComponentId());

        response.setCompanyName(
                encryptionService.decrypt(
                        employment.getCompanyName()));

        response.setLocation(
                encryptionService.decrypt(
                        employment.getLocation()));

        response.setEmploymentId(
                encryptionService.decrypt(
                        employment.getEmploymentId()));

        response.setDesignation(
                encryptionService.decrypt(
                        employment.getDesignation()));

        response.setEmploymentStartDate(
                employment.getEmploymentStartDate());

        response.setEmploymentEndDate(
                employment.getEmploymentEndDate());

        response.setSalary(
                employment.getSalary());

        response.setEmploymentType(
                employment.getEmploymentType());

        response.setReasonForLeaving(
                encryptionService.decrypt(
                        employment.getReasonForLeaving()));

        response.setInternational(
                employment.getInternational());

        response.setInternationalAmount(
                employment.getInternationalAmount());

        response.setAdditionalFeesRequired(
                employment.getAdditionalFeesRequired());

        response.setAdditionalFeesRequiredAmount(
                employment.getAdditionalFeesRequiredAmount());

        response.setStatus(
                employment.getStatus());
        
        response.setProcessingStatus(
                employment.getProcessingStatus() != null
                        ? employment.getProcessingStatus().name()
                        : null
        );

        response.setRemarks(
                employment.getRemarks());

        response.setCreatedBy(
                employment.getCreatedBy() != null
                        ? employment.getCreatedBy().getId()
                        : null);

        response.setUpdatedBy(
                employment.getUpdatedBy() != null
                        ? employment.getUpdatedBy().getId()
                        : null);

        response.setCreatedAt(
                employment.getCreatedAt());

        response.setUpdatedAt(
                employment.getUpdatedAt());

        return response;
    }

    // =====================================================
    // GENERATE SUB CASE ID
    // =====================================================

    private String generateEmploymentSubCaseId() {

        Optional<CaseEmploymentTable> latest =
                caseEmploymentRepository
                        .findTopByOrderByIdDesc();

        long nextNumber = 1;

        if (latest.isPresent()
                && latest.get().getSubCaseId() != null) {

            String subCaseId =
                    latest.get().getSubCaseId();

            String numberPart =
                    subCaseId.substring(
                            subCaseId.lastIndexOf("/") + 1);

            nextNumber =
                    Long.parseLong(numberPart) + 1;
        }

        return String.format(
                "PT/EM/%04d",
                nextNumber);
    }
   
    
    @Override
    @Transactional(readOnly = true)
    public List<CaseEmploymentOpenCaseResponse> getOpenEmploymentCases() {

        List<CaseEmploymentTable> employmentCases =
                caseEmploymentRepository.findByAssignedToIsNull();

        return employmentCases.stream()
                .map(this::mapToOpenCaseResponse)
                .toList();
    }
    
    
 // =====================================================
 // MAP TO OPEN CASE RESPONSE
 // =====================================================

 private CaseEmploymentOpenCaseResponse mapToOpenCaseResponse(
         CaseEmploymentTable employment) {

     CaseEmploymentOpenCaseResponse response =
             new CaseEmploymentOpenCaseResponse();

     // =====================================================
     // EMPLOYMENT DATA
     // =====================================================

     response.setId(employment.getId());

     response.setCaseId(employment.getCaseId());

     response.setCaseRef(employment.getCaseRef());

     response.setSubRefNo(employment.getSubCaseId());

     response.setCasePackageComponentId(employment.getCasePackageComponentId());

     response.setLocation(encryptionService.decrypt(employment.getLocation()));

     // =====================================================
     // CASE DATA
     // =====================================================

     Case caseEntity =
             caseRepository.findById(
                     employment.getCaseId())
                     .orElseThrow(() ->
                             new ResourceNotFoundException(
                                     "Case not found with id : "
                                             + employment.getCaseId()));

     response.setCaseReceivedDate(caseEntity.getCaseReceivedDate());

     response.setCaseInDate(caseEntity.getCaseInDate());

     response.setCaseDueDate(caseEntity.getCaseDueDate());

     response.setComponentDueDate(employment.getComponentDueDate());
     // =====================================================
     // CANDIDATE DATA
     // =====================================================

     StringBuilder candidateName = new StringBuilder();

     if (caseEntity.getFirstName() != null && !caseEntity.getFirstName().isBlank()) {

         candidateName.append(caseEntity.getFirstName());
     }

     if (caseEntity.getMiddleName() != null && !caseEntity.getMiddleName().isBlank()) {

         if (candidateName.length() > 0) {
        	 candidateName.append(" ");
         }
         candidateName.append(caseEntity.getMiddleName());
     }

     if (caseEntity.getLastName() != null
             && !caseEntity.getLastName().isBlank()) {

         if (candidateName.length() > 0) { candidateName.append(" ");
         }

         candidateName.append(caseEntity.getLastName());
     }

     	response.setCandidateName(candidateName.toString());
     	response.setFatherName(caseEntity.getFatherName());
     	response.setDob(caseEntity.getDateOfBirth());
     	response.setClientEmployeeId(caseEntity.getClientEmployeeId());


     if (caseEntity.getClientInformation() != null) {
         response.setClientName(caseEntity
                         .getClientInformation()
                         .getClientName());
     }

     if (caseEntity.getLocation() != null) {
         response.setClientLocation(caseEntity.getLocation().getLocationName());
     }

     return response;
 }
    

 @Override
 @Transactional
 public void assignEmploymentCases(CaseAssignmentRequest request) {



     if (request == null) {
         throw new IllegalArgumentException(
                 "Assignment request is required.");
     }

     if (request.getComponentId() == null) {
         throw new IllegalArgumentException(
                 "Component ID is required.");
     }

     if (request.getVerificationIds() == null
             || request.getVerificationIds().isEmpty()) {

         throw new IllegalArgumentException(
                 "At least one verification ID is required.");
     }

     if (request.getAssignedTo() == null) {
         throw new IllegalArgumentException(
                 "Assigned user ID is required.");
     }

     // =====================================================
     // GET ASSIGNED USER
     // =====================================================

     SystemUser assignedUser = systemUserRepository.findById(
                     request.getAssignedTo())
                     .orElseThrow(() ->
                             new ResourceNotFoundException(
                                     "User not found with id : "
                                             + request.getAssignedTo()));

     // =====================================================
     // USER ACTIVE VALIDATION
     // =====================================================

     if (!Boolean.TRUE.equals(
             assignedUser.getIsActive())) {

         throw new IllegalArgumentException(
                 "Selected user is inactive.");
     }

     // =====================================================
     // USER COMPONENT PERMISSION
     // =====================================================

     List<InternalUserComponent> userComponents =
             internalUserComponentRepository
                     .findBySystemUserIdAndIsActiveTrue(
                             assignedUser.getId());

     boolean hasComponentPermission =
             userComponents.stream()
                     .anyMatch(userComponent ->
                             userComponent.getComponent() != null
                                     && userComponent.getComponent()
                                             .getId()
                                             .equals(
                                                     request.getComponentId())
                                     && Boolean.TRUE.equals(
                                             userComponent.getComponent()
                                                     .getIsActive()));

     if (!hasComponentPermission) {
         throw new IllegalArgumentException(
                 "Selected user is not authorized for this component.");
     }

     // =====================================================
     // LOGGED-IN USER
     // =====================================================

     SystemUser loggedInUser = loggedInUserService.getLoggedInUser();

     if (loggedInUser == null) {
         throw new IllegalArgumentException(
                 "Logged-in user not found.");
     }

     LocalDateTime now = LocalDateTime.now();

     // =====================================================
     // ASSIGN SELECTED EMPLOYMENT CHECKS
     // =====================================================

     for (Long verificationId :
             request.getVerificationIds()) {

         if (verificationId == null) {
             throw new IllegalArgumentException(
                     "Verification ID cannot be null.");
         }

         CaseEmploymentTable employment =
                 caseEmploymentRepository.findById(
                         verificationId)
                         .orElseThrow(() ->
                                 new ResourceNotFoundException(
                                         "Employment verification not found with id : "
                                                 + verificationId));

         // =================================================
         // VERIFY COMPONENT
         // =================================================

         CasePackageComponent casePackageComponent =
                 casePackageComponentRepository
                         .findById(employment.getCasePackageComponentId())
                         .orElseThrow(() ->
                                 new ResourceNotFoundException(
                                         "Case package component not found for employment verification : "
                                                 + verificationId));

         if (casePackageComponent.getComponent() == null) {
             throw new ResourceNotFoundException(
                     "Component not found for employment verification : "
                             + verificationId);
         }

         if (!casePackageComponent.getComponent()
                 .getId()
                 .equals(request.getComponentId())) {

             throw new IllegalArgumentException(
                     "Component mismatch for employment verification : "
                             + verificationId);
         }

         // =================================================
         // ALREADY ASSIGNED CHECK
         // =================================================

         if (employment.getAssignedTo() != null) {
             throw new IllegalStateException(
                     "Employment verification already assigned. ID : "
                             + verificationId);
         }


         employment.setAssignedTo(assignedUser);
         employment.setAssignedBy(loggedInUser);
         employment.setAssignedAt(now);
         employment.setProcessingStatus(ComponentSubStatus.ASSIGNED_FOR_PROCESSING);

         employment.setRejectedBy(null);
         employment.setRejectedAt(null);
         
         caseEmploymentRepository.save(employment);
         
         Case caseEntity = caseRepository.findById(employment.getCaseId()).orElseThrow(() ->
         new ResourceNotFoundException("Case not found with id : " + employment.getCaseId()));
			
		caseHistoryService.createHistory(caseEntity,casePackageComponent,employment.getSubCaseId(),"ASSIGNED_FOR_PROCESSING","ACTIVITY MESSAGE WILL GO HERE");
     }
 }
    
 @Override
 @Transactional(readOnly = true)
 public List<CaseEmploymentReassignResponse> getAssignedEmploymentCases() {

     List<CaseEmploymentTable> employmentCases =
             caseEmploymentRepository.findByAssignedToIsNotNull();

     return employmentCases.stream()
             .map(this::mapToReassignResponse)
             .toList();
 }
 
 private CaseEmploymentReassignResponse mapToReassignResponse(CaseEmploymentTable employment) {

	    CaseEmploymentReassignResponse response = new CaseEmploymentReassignResponse();

	    response.setId(employment.getId());
	    response.setCaseId(employment.getCaseId());
	    response.setCaseRef(employment.getCaseRef());
	    response.setSubRefNo(employment.getSubCaseId());
	    response.setCasePackageComponentId(employment.getCasePackageComponentId());

	    // Employment verification location
	    if (employment.getLocation() != null) {
	        response.setLocation(
	                encryptionService.decrypt(employment.getLocation())
	        );
	    }

	    // Case
	    Case caseEntity = caseRepository.findById(employment.getCaseId()).orElse(null);

	    if (caseEntity != null) {

	        response.setCaseReceivedDate(caseEntity.getCaseReceivedDate());
	        response.setCaseInDate(caseEntity.getCaseInDate());
	        response.setCaseDueDate(caseEntity.getCaseDueDate());

	        StringBuilder candidateName = new StringBuilder();

	        if (caseEntity.getFirstName() != null && !caseEntity.getFirstName().isBlank()) 
	        {
	        	candidateName.append(caseEntity.getFirstName());
	        }

	        if (caseEntity.getMiddleName() != null && !caseEntity.getMiddleName().isBlank()) 
	        {
	            if (candidateName.length() > 0) {candidateName.append(" "); }
	            candidateName.append(caseEntity.getMiddleName());
	        }

	        if (caseEntity.getLastName() != null && !caseEntity.getLastName().isBlank()) {

	            if (candidateName.length() > 0) { candidateName.append(" ");}
	            candidateName.append(caseEntity.getLastName());
	        }
	        
	        response.setCandidateName(candidateName.toString());response.setFatherName(caseEntity.getFatherName());
	        response.setDob(caseEntity.getDateOfBirth());
	        response.setClientEmployeeId(caseEntity.getClientEmployeeId());

	        if (caseEntity.getClientInformation() != null) 
	        	{
	            response.setClientName(caseEntity.getClientInformation().getClientName());
	        }

	        if (caseEntity.getLocation() != null) {
	            response.setClientLocation(caseEntity.getLocation().getLocationName());
	        }
	    }

	    response.setComponentDueDate(employment.getComponentDueDate());

	    // Assigned user
	    if (employment.getAssignedTo() != null) 
	    {
	        response.setAssignedTo(employment.getAssignedTo().getId());
	        String firstName = employment.getAssignedTo().getFirstName() != null? employment.getAssignedTo().getFirstName(): "";
	        String lastName = employment.getAssignedTo().getLastName() != null? employment.getAssignedTo().getLastName(): "";
	        response.setAssignedToName((firstName + " " + lastName).trim());
	    }

	    return response;
	}
 
 @Override
 @Transactional
 public void reassignEmploymentCases(CaseAssignmentRequest request) 
 {
	 			
	 if (request == null) {
		 	throw new IllegalArgumentException(
	            "Assignment request cannot be null."
	    );
	}

	if (request.getComponentId() == null) {

	    throw new IllegalArgumentException(
	            "Component ID is required."
	    );
	}

	if (request.getVerificationIds() == null
	        || request.getVerificationIds().isEmpty()) {

	    throw new IllegalArgumentException(
	            "At least one verification ID is required."
	    );
	}

	if (request.getAssignedTo() == null) {

	    throw new IllegalArgumentException(
	            "Assigned user is required."
	    );
	}

	SystemUser loggedInUser =
	        loggedInUserService.getLoggedInUser();

	if (loggedInUser == null) {

	    throw new IllegalArgumentException(
	            "Logged-in user not found."
	    );
	}

	SystemUser newAssignedUser =
	        systemUserRepository.findById(request.getAssignedTo())
	                .orElseThrow(() ->
	                        new IllegalArgumentException(
	                                "Selected user not found."
	                        ));

	if (!Boolean.TRUE.equals(newAssignedUser.getIsActive())) {

	    throw new IllegalArgumentException(
	            "Selected user is inactive."
	    );
	}

	boolean hasComponentPermission =
	        internalUserComponentRepository
	                .findByComponent_ComponentNameAndComponent_IsActiveTrueAndIsActiveTrueAndSystemUser_IsActiveTrue(
	                        "Employment"
	                )
	                .stream()
	                .anyMatch(assignment ->
	                        assignment.getSystemUser()
	                                .getId()
	                                .equals(newAssignedUser.getId())
	                );

	if (!hasComponentPermission) {

	    throw new IllegalArgumentException(
	            "Selected user does not have Employment component permission."
	    );
	}

	for (Long verificationId : request.getVerificationIds()) {

	    CaseEmploymentTable employment =
	            caseEmploymentRepository.findById(verificationId)
	                    .orElseThrow(() ->
	                            new IllegalArgumentException(
	                                    "Employment verification not found: "
	                                            + verificationId
	                            ));

	    if (employment.getAssignedTo() == null) {

	        throw new IllegalArgumentException(
	                "Verification " + verificationId
	                        + " is not currently assigned."
	        );
	    }

	    // Get CasePackageComponent using its ID
	    CasePackageComponent casePackageComponent =
	            casePackageComponentRepository
	                    .findById(
	                            employment.getCasePackageComponentId()
	                    )
	                    .orElseThrow(() ->
	                            new IllegalArgumentException(
	                                    "Case package component not found for verification: "
	                                            + verificationId
	                            ));

	    // Get actual master Component ID
	    Long actualComponentId = casePackageComponent.getComponent().getId();
	    
	    
	    // Compare Component ID with request Component ID
	    if (!actualComponentId.equals(request.getComponentId())) {

	        throw new IllegalArgumentException(
	                "Component mismatch for verification: "
	                        + verificationId
	        );
	    }
	    
	    SystemUser oldAssignedUser = employment.getAssignedTo();
	    employment.setAssignedTo(newAssignedUser);

	    employment.setAssignedBy(loggedInUser);

	    employment.setAssignedAt(LocalDateTime.now());

	    employment.setRejectedBy(null);

	    employment.setRejectedAt(null);

	    caseEmploymentRepository.save(employment);
	    
	    Case caseEntity = caseRepository.findById(employment.getCaseId())
	            .orElseThrow(() ->
	                    new IllegalArgumentException(
	                            "Case not found for verification: "
	                                    + verificationId
	                    ));

	    String oldUserName =
	            (oldAssignedUser.getFirstName() != null
	                    ? oldAssignedUser.getFirstName()
	                    : "")
	            + " "
	            + (oldAssignedUser.getLastName() != null
	                    ? oldAssignedUser.getLastName()
	                    : "");

	    oldUserName = oldUserName.trim();

	    String newUserName =
	            (newAssignedUser.getFirstName() != null
	                    ? newAssignedUser.getFirstName()
	                    : "")
	            + " "
	            + (newAssignedUser.getLastName() != null
	                    ? newAssignedUser.getLastName()
	                    : "");

	    newUserName = newUserName.trim();

	    caseHistoryService.createHistory(
	            caseEntity,
	            casePackageComponent,
	            employment.getSubCaseId(),
	            "REASSIGNED",
	            "Education verification reassigned from "
	                    + oldUserName
	                    + " to "
	                    + newUserName
	    );
	}
}
 
		//=====================================================
		//GET MY EMPLOYMENT CASES
		//=====================================================
			 @Override
			 @Transactional(readOnly = true)
			 public Page<CaseEmploymentMyCaseResponse> getMyEmploymentCases(
			         Pageable pageable) {
			
			     SystemUser loggedInUser =
			             loggedInUserService.getLoggedInUser();
			
			     if (loggedInUser == null) {
			         throw new IllegalArgumentException(
			                 "Logged-in user not found.");
			     }
			
			     Page<CaseEmploymentTable> employmentPage = caseEmploymentRepository.findByAssignedToIdAndProcessingStatusNotOrderBySubCaseIdDesc(loggedInUser.getId(),ComponentSubStatus.COMPLETE_SEND_TO_QC,pageable);
			
			     return employmentPage.map(
			             this::mapToMyCaseResponse
			     );
			 }
			 
			// =====================================================
			// MAP TO MY CASE RESPONSE
			// =====================================================

			private CaseEmploymentMyCaseResponse mapToMyCaseResponse(CaseEmploymentTable employment) {

			    CaseEmploymentMyCaseResponse response = new CaseEmploymentMyCaseResponse();

			    // =====================================================
			    // EMPLOYMENT DATA
			    // =====================================================

			    response.setId(employment.getId());

			    response.setCaseRef(employment.getCaseRef());

			    response.setSubRefNo(
			            employment.getSubCaseId());

			   
			    
			    response.setComponentDueDate(
			    		employment.getComponentDueDate() != null
			                    ? employment.getComponentDueDate().toString()
			                    : null);


			    response.setComponentDetail(
			            employment.getCompanyName() != null
			                    ? encryptionService.decrypt(
			                            employment.getCompanyName())
			                    : null);

			    response.setComponentStatus(
			            employment.getProcessingStatus() != null
			                    ? employment.getProcessingStatus().name()
			                    : null);



			    Case caseEntity =
			            caseRepository.findById(
			                    employment.getCaseId())
			                    .orElseThrow(() ->
			                            new ResourceNotFoundException(
			                                    "Case not found with id : "
			                                            + employment.getCaseId()));



			    response.setCaseReceivedDate(
			            caseEntity.getCaseReceivedDate() != null
			                    ? caseEntity.getCaseReceivedDate().toString()
			                    : null);

			    
			    
			    response.setCaseInDate(
			            caseEntity.getCaseInDate() != null
			                    ? caseEntity.getCaseInDate().toString()
			                    : null );


			    
			    response.setCaseDueDate(
			            caseEntity.getCaseDueDate() != null
			                    ? caseEntity.getCaseDueDate().toString()
			                    : null);

			    response.setFatherName( caseEntity.getFatherName());

			    response.setDob( caseEntity.getDateOfBirth() != null
			                    ? caseEntity.getDateOfBirth().toString()
			                    : null
			    );
			    

			    response.setClientEmployeeId( caseEntity.getClientEmployeeId());


			    StringBuilder candidateName = new StringBuilder();

			    if (caseEntity.getFirstName() != null
			            && !caseEntity.getFirstName().isBlank()) {

			        candidateName.append(
			                caseEntity.getFirstName());
			    }

			    if (caseEntity.getMiddleName() != null
			            && !caseEntity.getMiddleName().isBlank()) {

			        if (candidateName.length() > 0) {
			            candidateName.append(" ");
			        }

			        candidateName.append(
			                caseEntity.getMiddleName());
			    }

			    if (caseEntity.getLastName() != null
			            && !caseEntity.getLastName().isBlank()) {

			        if (candidateName.length() > 0) {
			            candidateName.append(" ");
			        }

			        candidateName.append(
			                caseEntity.getLastName());
			    }

			    response.setCandidateName(
			            candidateName.toString());


			    response.setMobileNo(
			            caseEntity.getMobileNumber() != null
			                    ? encryptionService.decrypt(
			                            caseEntity.getMobileNumber())
			                    : null);


			    String clientName =
			            caseEntity.getClientInformation() != null
			                    ? caseEntity.getClientInformation()
			                            .getClientName()
			                    : null;

			    String locationName =
			            caseEntity.getLocation() != null
			                    ? caseEntity.getLocation()
			                            .getLocationName()
			                    : null;

			    StringBuilder clientLocation =
			            new StringBuilder();

			    if (clientName != null
			            && !clientName.isBlank()) {

			        clientLocation.append(clientName);
			    }

			    if (locationName != null
			            && !locationName.isBlank()) {

			        if (clientLocation.length() > 0) {
			            clientLocation.append(" / ");
			        }

			        clientLocation.append(locationName);
			    }

			    response.setClientNameLocation(
			            clientLocation.toString());

			    return response;
			}

			@Override
			@Transactional
			public String refuseEmploymentCase(Long id, String rejectionComment) {

			    if (id == null) {
			        throw new IllegalArgumentException(
			                "Employment verification ID is required.");
			    }

			    if (rejectionComment == null
			            || rejectionComment.isBlank()) {

			        throw new IllegalArgumentException(
			                "Rejection comment is required.");
			    }

			    SystemUser loggedInUser =
			            loggedInUserService.getLoggedInUser();

			    if (loggedInUser == null) {
			        throw new IllegalArgumentException(
			                "Logged-in user not found.");
			    }

			    CaseEmploymentTable employment =
			            caseEmploymentRepository.findById(id)
			                    .orElseThrow(() ->
			                            new ResourceNotFoundException(
			                                    "Employment verification not found with id : "
			                                            + id
			                            ));

			    if (employment.getAssignedTo() == null) {
			        throw new IllegalStateException(
			                "This employment verification is not assigned.");
			    }

			    if (!employment.getAssignedTo()
			            .getId()
			            .equals(loggedInUser.getId())) {

			        throw new IllegalStateException(
			                "You can only refuse an employment verification assigned to you.");
			    }

			    LocalDateTime now = LocalDateTime.now();

			    employment.setAssignedTo(null);
			    employment.setRejectedBy(loggedInUser);
			    employment.setRejectedAt(now);

			    employment.setUpdatedBy(loggedInUser);
			    employment.setUpdatedAt(now);

			    // Save rejection comment
			    employment.setRejectionComment(
			            rejectionComment.trim());

			    caseEmploymentRepository.save(employment);

			    Case caseEntity =
			            caseRepository.findById(employment.getCaseId())
			                    .orElseThrow(() ->
			                            new ResourceNotFoundException(
			                                    "Case not found with id : "
			                                            + employment.getCaseId()));

			    CasePackageComponent casePackageComponent =
			            casePackageComponentRepository
			                    .findById(employment.getCasePackageComponentId())
			                    .orElseThrow(() ->
			                            new ResourceNotFoundException(
			                                    "Case package component not found."));

			    caseHistoryService.createHistory(
			            caseEntity,
			            casePackageComponent,
			            employment.getSubCaseId(),
			            "REJECTED",
			            rejectionComment.trim()
			    );

			    return employment.getRejectionComment();
			}
	@Override
	@Transactional
	public void startProcessing(Long id) {

	    CaseEmploymentTable employment =
	            caseEmploymentRepository.findById(id)
	                    .orElseThrow(() ->new ResourceNotFoundException( "Employment verification not found with id : " + id));

	    if (employment.getProcessingStatus() == ComponentSubStatus.ASSIGNED_FOR_PROCESSING) {
	    	employment.setProcessingStatus(ComponentSubStatus.WIP);
	    	Case caseEntity = caseRepository.findById(employment.getCaseId())
	                .orElseThrow(() ->
	                        new ResourceNotFoundException(
	                                "Case not found with id : "
	                                        + employment.getCaseId()));

	        CasePackageComponent casePackageComponent =
	                casePackageComponentRepository
	                        .findById(employment.getCasePackageComponentId())
	                        .orElseThrow(() ->
	                                new ResourceNotFoundException(
	                                        "Case package component not found."
	                                ));

	        caseHistoryService.createHistory(
	                caseEntity,
	                casePackageComponent,
	                employment.getSubCaseId(),
	                "WIP",
	                "Case check Status to WIP");
	    }

	    SystemUser loggedInUser = loggedInUserService.getLoggedInUser();

	    LocalDateTime now = LocalDateTime.now();

	    employment.setUpdatedAt(now);
	    employment.setUpdatedBy(loggedInUser);
	    caseEmploymentRepository.save(employment);
	}
	
	@Override
	@Transactional
	public void updateProcessingStatus(Long id, ComponentSubStatus status, DispositionStatus dispositionStatus, String activity) {

	    if (id == null) {
	        throw new IllegalArgumentException(
	                "Employment verification ID is required."
	        );
	    }

	    if (status == null) {
	        throw new IllegalArgumentException(
	                "Processing status is required."
	        );
	    }

	    if (activity == null || activity.isBlank()) {
	        throw new IllegalArgumentException(
	                "Activity is required."
	        );
	    }


	    if (status == ComponentSubStatus.COMPLETE_SEND_TO_QC
	            && dispositionStatus == null) {

	        throw new IllegalArgumentException(
	                "Disposition status is required when sending case to QC."
	        );
	    }

	 
	    CaseEmploymentTable employment =
	            caseEmploymentRepository.findById(id)
	                    .orElseThrow(() ->
	                            new ResourceNotFoundException(
	                                    "Employment verification not found with id : "
	                                            + id));



	      employment.setProcessingStatus(status);


	      if (status == ComponentSubStatus.COMPLETE_SEND_TO_QC) {

	          // Process Team sends case to QC
	    	  employment.setDispositionStatus(dispositionStatus);

	          // QC disposition should be empty initially
	    	  employment.setQcDispositionStatus(null);

	      } else if (status == ComponentSubStatus.QC_COMPLETE) {

	          // QC has completed the verification
	          // Keep the original Process Team disposition
	          // and save the QC disposition separately.
	    	  employment.setQcDispositionStatus(dispositionStatus);

	      } else if (status == ComponentSubStatus.RE_WORK) {

	          // QC sends case back for re-work
	    	  employment.setProcessingStatus(status.ASSIGNED_FOR_PROCESSING);
	    	  employment.setDispositionStatus(null);
	    	  employment.setQcDispositionStatus(null);

	      } else {

	          // Existing processing statuses
	    	  employment.setDispositionStatus(null);
	    	  employment.setQcDispositionStatus(null);
	      }

	    SystemUser loggedInUser = loggedInUserService.getLoggedInUser();

	    if (loggedInUser == null) {
	        throw new IllegalArgumentException(
	                "Logged-in user not found.");
	    }

	    LocalDateTime now = LocalDateTime.now();

	    employment.setUpdatedAt(now);
	    employment.setUpdatedBy(loggedInUser);

	    CaseEmploymentTable savedEmployment = caseEmploymentRepository.save(employment);


	    Case caseEntity =
	            caseRepository.findById(
	                    savedEmployment.getCaseId()).orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Case not found with id : "
	                                    + savedEmployment.getCaseId()));


	    CasePackageComponent casePackageComponent =
	            casePackageComponentRepository.findById(
	                    savedEmployment.getCasePackageComponentId())
	            .orElseThrow(() ->new ResourceNotFoundException( "Case package component not found with id : "+ savedEmployment.getCasePackageComponentId()));


	    caseHistoryService.createHistory(
	            caseEntity,
	            casePackageComponent,
	            savedEmployment.getSubCaseId(),
	            status.name(),
	            activity);
	}
}