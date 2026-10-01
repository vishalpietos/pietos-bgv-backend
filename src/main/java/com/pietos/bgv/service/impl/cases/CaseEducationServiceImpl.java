package com.pietos.bgv.service.impl.cases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pietos.bgv.dto.request.cases.CaseAssignmentRequest;
import com.pietos.bgv.dto.request.cases.CaseEducationRequest;
import com.pietos.bgv.dto.response.cases.CaseEducationMyCaseResponse;
import com.pietos.bgv.dto.response.cases.CaseEducationOpenCaseResponse;
import com.pietos.bgv.dto.response.cases.CaseEducationReassignResponse;
import com.pietos.bgv.dto.response.cases.CaseEducationResponse;

import com.pietos.bgv.entity.ClientPackage;
import com.pietos.bgv.entity.ClientPackageComponent;
import com.pietos.bgv.entity.Component;
import com.pietos.bgv.entity.InternalUserComponent;
import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.entity.cases.Case;
import com.pietos.bgv.entity.cases.CaseEducationTable;
import com.pietos.bgv.entity.cases.CasePackage;
import com.pietos.bgv.entity.cases.CasePackageComponent;
import com.pietos.bgv.enums.CaseStatus;
import com.pietos.bgv.enums.ComponentSubStatus;
import com.pietos.bgv.enums.DataEntryStatus;
import com.pietos.bgv.enums.DispositionStatus;
import com.pietos.bgv.enums.PackageType;
import com.pietos.bgv.exception.ResourceNotFoundException;
import com.pietos.bgv.repository.SystemUserRepository;
import com.pietos.bgv.repository.cases.CaseEducationRepository;
import com.pietos.bgv.repository.cases.CaseRepository;
import com.pietos.bgv.security.EncryptionService;
import com.pietos.bgv.security.LoggedInUserService;
import com.pietos.bgv.service.cases.CaseEducationService;
import com.pietos.bgv.service.cases.CaseHistoryService;
import com.pietos.bgv.service.cases.CaseService;
import com.pietos.bgv.service.cases.CaseStatusService;

import com.pietos.bgv.repository.cases.CasePackageComponentRepository;

import com.pietos.bgv.repository.ClientComponentTatRepository;
import com.pietos.bgv.repository.ComponentRepository;
import com.pietos.bgv.repository.InternalUserComponentRepository;
import com.pietos.bgv.service.TatDateCalculationService;

@Service
@Transactional
public class CaseEducationServiceImpl implements CaseEducationService {
	private final CaseEducationRepository caseEducationRepository;
	private final CaseRepository caseRepository;
	private final LoggedInUserService loggedInUserService;
	private final SystemUserRepository systemUserRepository;
	private final EncryptionService encryptionService;
	private final CaseHistoryService caseHistoryService;
	

	private final CasePackageComponentRepository casePackageComponentRepository;
	private final ClientComponentTatRepository clientComponentTatRepository;
	private final TatDateCalculationService tatDateCalculationService;
	private final ComponentRepository componentRepository;
	private final InternalUserComponentRepository internalUserComponentRepository;
	private final CaseService caseService;

	

	private final CaseStatusService caseStatusService;

	public CaseEducationServiceImpl(
			CaseEducationRepository caseEducationRepository,
	        CaseRepository caseRepository,
	        LoggedInUserService loggedInUserService,
	        EncryptionService encryptionService,
	        CaseHistoryService caseHistoryService,
	        CaseStatusService caseStatusService,
	        SystemUserRepository systemUserRepository,
	        CasePackageComponentRepository casePackageComponentRepository,
	        ClientComponentTatRepository clientComponentTatRepository,
	        TatDateCalculationService tatDateCalculationService,	      
	        ComponentRepository componentRepository,	         
	        InternalUserComponentRepository internalUserComponentRepository,
	        CaseService caseService) {

	    this.caseEducationRepository = caseEducationRepository;
	    this.caseRepository = caseRepository;
	    this.loggedInUserService = loggedInUserService;
	    this.encryptionService = encryptionService;
	    this.caseHistoryService = caseHistoryService;
	    this.caseStatusService = caseStatusService;
	    this.systemUserRepository = systemUserRepository;
	    this.casePackageComponentRepository = casePackageComponentRepository;
	    this.clientComponentTatRepository = clientComponentTatRepository;
	    this.tatDateCalculationService = tatDateCalculationService;	    
	    this.componentRepository =componentRepository;    
	    this.internalUserComponentRepository =internalUserComponentRepository;
	    this.caseService=caseService;
	}
    
    
    @Override
    @Transactional
    public CaseEducationResponse createEducation(CaseEducationRequest request) {

        if (request.getCaseId() == null) {
            throw new IllegalArgumentException("Case ID is required.");
        }

        if (request.getCasePackageComponentId() == null) {
            throw new IllegalArgumentException("Case package component ID is required.");
        }

        if (request.getStatus() == null || request.getStatus().isBlank()) {

            throw new IllegalArgumentException( "Status is required.");
        }

        Case caseEntity = caseRepository.findById(request.getCaseId())
                        .orElseThrow(() -> new ResourceNotFoundException("Case not found with id : " + request.getCaseId()));

        if (caseEntity.getCaseReceivedDate() == null) {
            throw new IllegalArgumentException("Case received date is required.");
        }

        CasePackageComponent casePackageComponent = casePackageComponentRepository
                        .findById( request.getCasePackageComponentId())
                        .orElseThrow(() ->new ResourceNotFoundException("Case package component not found with id : "+ request.getCasePackageComponentId()));

        CasePackage casePackage =casePackageComponent.getCasePackage();

        if (casePackage == null) {
            throw new ResourceNotFoundException("Case package not found.");
        }

        ClientPackage clientPackage = casePackage.getClientPackage();

        if (clientPackage == null) {
            throw new ResourceNotFoundException("Client package not found.");
        }

        Integer componentTatDays;
        
        if (PackageType.PACKAGE.equals(clientPackage.getPackageType())) {

           
            componentTatDays = clientPackage.getInternalTat();

            if (componentTatDays == null || componentTatDays < 0) {

                throw new IllegalArgumentException("Internal TAT is not configured for this package.");
            }

        }

        // INDIVIDUAL-WISE
        else if (PackageType.INDIVIDUAL.equals(clientPackage.getPackageType())) {

        	ClientPackageComponent clientPackageComponent = casePackageComponent.getClientPackageComponent();

        	if (clientPackageComponent == null) {
        	    throw new ResourceNotFoundException("Client package component not found.");
        	}

        	componentTatDays =clientPackageComponent.getTatDays();

        	if (componentTatDays == null
        	        || componentTatDays < 0) {

        	    throw new IllegalArgumentException("TAT is not configured for component : "+ casePackageComponent.getComponent().getComponentName());
        	}
        } else {

            throw new IllegalArgumentException("Invalid package type.");
        }


        LocalDate componentDueDate = tatDateCalculationService.calculateEffectiveTo(caseEntity.getCaseReceivedDate().atStartOfDay(),componentTatDays,clientPackage.getHolidayType());


        LocalDate caseDueDate =caseStatusService.calculateCaseDueDate(caseEntity.getId());


        LocalDateTime now = LocalDateTime.now();

        SystemUser loggedInUser = loggedInUserService.getLoggedInUser();
        CaseEducationTable education =new CaseEducationTable();
        education.setCaseId(caseEntity.getId());
        education.setCaseRef(caseEntity.getCaseRef());
        education.setCasePackageComponentId(request.getCasePackageComponentId());
        education.setComponentDueDate(componentDueDate);
        education.setStatus(DataEntryStatus.valueOf(request.getStatus()));
        education.setRemarks(request.getRemarks());
        education.setUniversity(encryptionService.encrypt(request.getUniversity()));
        education.setInstitution(encryptionService.encrypt(request.getInstitution()));
        education.setRegistrationNumber(encryptionService.encrypt(request.getRegistrationNumber()));
        education.setInternational(request.getInternational());
        education.setInternationalAmount(request.getInternationalAmount());
        education.setAdditionalFeesRequired(request.getAdditionalFeesRequired());
        education.setAdditionalFeesRequiredAmount(request.getAdditionalFeesRequiredAmount());
        education.setQualification(encryptionService.encrypt(request.getQualification()));
        education.setYearOfPassing(encryptionService.encrypt(request.getYearOfPassing()));
        education.setCreatedAt(now);
        education.setUpdatedAt(now);
        education.setCreatedBy(loggedInUser);
        education.setUpdatedBy(loggedInUser);
        education.setSubCaseId(generateEducationSubCaseId());
        CaseEducationTable savedEducation = caseEducationRepository.save(education);
        caseEntity.setCaseDueDate(caseDueDate);
        caseEntity.setUpdatedBy(loggedInUser);
        caseEntity.setUpdatedAt(now);
        Case savedCase = caseRepository.save(caseEntity);
        CaseStatus evaluatedStatus = caseService.evaluateDataEntryStatus(savedCase.getId());


        // Update Case Status if required
        if (savedCase.getStatus() != evaluatedStatus) {

            savedCase.setStatus(evaluatedStatus);
            savedCase.setUpdatedBy(loggedInUser);
            savedCase.setUpdatedAt(now);

            savedCase = caseRepository.save(savedCase);
        }
        
        caseHistoryService.createHistory(
                savedCase,
                casePackageComponent,
                savedEducation.getSubCaseId(),
                savedEducation.getStatus().name(),
                "Education data entry completed"
        );
        
        return mapToResponse(savedEducation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CaseEducationResponse> getEducationByCaseId(Long caseId) {
    	
        return caseEducationRepository.findByCaseId(caseId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CaseEducationResponse> getEducationByCaseRef(String caseRef) {

        return caseEducationRepository.findByCaseRef(caseRef).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CaseEducationResponse>getEducationByCasePackageComponentId(Long casePackageComponentId) {

        return caseEducationRepository.findByCasePackageComponentId(casePackageComponentId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public CaseEducationResponse updateEducation(Long id,CaseEducationRequest request) {

        if (request.getStatus() == null) {
            throw new IllegalArgumentException("Status is required.");
        }

        CaseEducationTable education =
                caseEducationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Education verification not found with id : "
                                                + id));

        // Encrypt fields before saving
        education.setUniversity(encryptionService.encrypt(request.getUniversity()));
        education.setInstitution(encryptionService.encrypt(request.getInstitution()));
        education.setRegistrationNumber(encryptionService.encrypt(request.getRegistrationNumber()));
        education.setInternational(request.getInternational());
        education.setInternationalAmount(request.getInternationalAmount());
        education.setAdditionalFeesRequired(request.getAdditionalFeesRequired());
        education.setAdditionalFeesRequiredAmount(request.getAdditionalFeesRequiredAmount());
        education.setQualification(encryptionService.encrypt(request.getQualification()));
        education.setYearOfPassing(encryptionService.encrypt(request.getYearOfPassing()));
        SystemUser loggedInUser =loggedInUserService.getLoggedInUser();

        LocalDateTime now = LocalDateTime.now();

        education.setUpdatedAt(now);
        education.setUpdatedBy(loggedInUser);
       
        CaseEducationTable updatedEducation = caseEducationRepository.save(education);
        
        Case caseEntity = caseRepository.findById(education.getCaseId())
                        .orElseThrow(() ->new ResourceNotFoundException( "Case not found with id : "+ education.getCaseId()));

        caseEntity.setUpdatedBy(loggedInUser);
        caseEntity.setUpdatedAt(now);

        Case savedCase = caseRepository.save(caseEntity);

        if (request.getStatus() != null) {
            caseHistoryService.createHistory(
                    savedCase,
                    request.getStatus(),
                    "Education data entry updated"
            );
        }

        return mapToResponse(updatedEducation);
    }
    
    
    @Override
    public CaseEducationResponse updateSelectedEducationFields( Long id,  CaseEducationRequest request) {

        CaseEducationTable education =
                caseEducationRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException( "Education verification not found with id : "+ id));

        /*
         * Update only the fields that are provided in the request.
         * Null means: do not update this field.
         */

        if (request.getUniversity() != null) {
            education.setUniversity(encryptionService.encrypt(request.getUniversity()));
        }

        if (request.getInstitution() != null) {
            education.setInstitution(encryptionService.encrypt(request.getInstitution()));
        }

        if (request.getRegistrationNumber() != null) {
            education.setRegistrationNumber(encryptionService.encrypt(request.getRegistrationNumber()));
        }

        if (request.getInternational() != null) {
            education.setInternational(request.getInternational());
        }

        if (request.getInternationalAmount() != null) {
            education.setInternationalAmount(request.getInternationalAmount());
        }

        if (request.getAdditionalFeesRequired() != null) {
            education.setAdditionalFeesRequired(request.getAdditionalFeesRequired());
        }

        if (request.getAdditionalFeesRequiredAmount() != null) {
            education.setAdditionalFeesRequiredAmount(request.getAdditionalFeesRequiredAmount());
        }

        if (request.getQualification() != null) {
            education.setQualification(encryptionService.encrypt(request.getQualification()));
        }

        if (request.getYearOfPassing() != null) {
            education.setYearOfPassing(encryptionService.encrypt(request.getYearOfPassing()));
        }

        SystemUser loggedInUser = loggedInUserService.getLoggedInUser();

        LocalDateTime now = LocalDateTime.now();

        education.setUpdatedAt(now);
        education.setUpdatedBy(loggedInUser);

        CaseEducationTable updatedEducation =caseEducationRepository.save(education);

        /*
         * Update Case audit information.
         */
        Case caseEntity = caseRepository.findById(education.getCaseId())
                        .orElseThrow(() ->new ResourceNotFoundException("Case not found with id : "+ education.getCaseId()));

        caseEntity.setUpdatedBy(loggedInUser);
        caseEntity.setUpdatedAt(now);

        caseRepository.save(caseEntity);

        return mapToResponse(updatedEducation);
    }
    
    
    
    
    

    private CaseEducationResponse mapToResponse(CaseEducationTable education) {

        CaseEducationResponse response = new CaseEducationResponse();
        response.setId(education.getId());
        response.setCaseId(education.getCaseId());
        response.setCaseRef(education.getCaseRef());
        response.setCasePackageComponentId( education.getCasePackageComponentId());
        response.setUniversity(encryptionService.decrypt(education.getUniversity()));
        response.setInstitution(encryptionService.decrypt(education.getInstitution()));
        response.setRegistrationNumber(encryptionService.decrypt(education.getRegistrationNumber()));
        response.setInternational(education.getInternational());
        response.setInternationalAmount(education.getInternationalAmount());
        response.setAdditionalFeesRequired(education.getAdditionalFeesRequired());
        response.setAdditionalFeesRequiredAmount(education.getAdditionalFeesRequiredAmount());
        response.setQualification(encryptionService.decrypt(education.getQualification()));
        response.setYearOfPassing(encryptionService.decrypt(education.getYearOfPassing()));
        response.setRemarks(education.getRemarks());
        response.setSubRefNo(education.getSubCaseId());
        response.setStatus(education.getStatus() != null? education.getStatus().name(): null);
        response.setCreatedBy(education.getCreatedBy() != null? education.getCreatedBy().getId(): null );
        response.setUpdatedBy(education.getUpdatedBy() != null? education.getUpdatedBy().getId(): null);
        response.setCreatedAt(education.getCreatedAt());
        response.setUpdatedAt( education.getUpdatedAt());

        return response;
    }
    
    
    private String generateEducationSubCaseId() {

        Optional<CaseEducationTable> latest =
                caseEducationRepository.findTopByOrderByIdDesc();

        long nextNumber = 1;

        if (latest.isPresent() && latest.get().getSubCaseId() != null) {

            String subCaseId = latest.get().getSubCaseId();

            String numberPart = subCaseId.substring(subCaseId.lastIndexOf("/") + 1);

            nextNumber = Long.parseLong(numberPart) + 1;
        }

        return String.format("PT/ED/%04d", nextNumber);
    }
    
    
    @Override
    @Transactional(readOnly = true)
    public List<CaseEducationOpenCaseResponse> getOpenEducationCases() {

        List<CaseEducationTable> educationCases =
                caseEducationRepository.findByAssignedToIsNull();

        return educationCases.stream().map(this::mapToOpenCaseResponse)
                .toList();
    }
    
    private CaseEducationOpenCaseResponse mapToOpenCaseResponse(
            CaseEducationTable education) {

        CaseEducationOpenCaseResponse response = new CaseEducationOpenCaseResponse();

        // =====================================================
        // EDUCATION DATA
        // =====================================================

        response.setId(education.getId());
        response.setCaseId(education.getCaseId());
        response.setCaseRef(education.getCaseRef());
        response.setSubRefNo(education.getSubCaseId());
        response.setCasePackageComponentId(education.getCasePackageComponentId());
        response.setUniversityName(encryptionService.decrypt(education.getUniversity()));
        response.setDegree(encryptionService.decrypt(education.getQualification()));
        response.setYearOfPassing(encryptionService.decrypt(education.getYearOfPassing()));
        response.setCollegeName(encryptionService.decrypt(education.getInstitution()));
        response.setComponentDueDate(education.getComponentDueDate());

        Case caseEntity = caseRepository.findById(education.getCaseId()).orElseThrow(() -> new ResourceNotFoundException("Case not found with id : "+ education.getCaseId()));
        response.setCaseReceivedDate(caseEntity.getCaseReceivedDate());
        response.setCaseInDate(caseEntity.getCaseInDate());
        response.setCaseDueDate(caseEntity.getCaseDueDate());

        StringBuilder candidateName = new StringBuilder();

        if (caseEntity.getFirstName() != null
                && !caseEntity.getFirstName().isBlank()) {

            candidateName.append(caseEntity.getFirstName());
        }

        if (caseEntity.getMiddleName() != null
                && !caseEntity.getMiddleName().isBlank()) {

            if (candidateName.length() > 0) {
                candidateName.append(" ");
            }

            candidateName.append(caseEntity.getMiddleName());
        }

        if (caseEntity.getLastName() != null
                && !caseEntity.getLastName().isBlank()) {

            if (candidateName.length() > 0) {
                candidateName.append(" ");
            }

            candidateName.append(caseEntity.getLastName());
        }

        response.setCandidateName(candidateName.toString());
        response.setFatherName(caseEntity.getFatherName());
        response.setDob(caseEntity.getDateOfBirth());
        response.setClientEmployeeId(caseEntity.getClientEmployeeId());
        
        if (caseEntity.getClientInformation() != null) {
            response.setClientName(caseEntity.getClientInformation().getClientName());
        }


        if (caseEntity.getLocation() != null) {
            response.setClientLocation(caseEntity.getLocation().getLocationName());
        }

        return response;
    }
    
    @Override
    @Transactional
    public void assignEducationCases(CaseAssignmentRequest request) {


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


        Component component = componentRepository
                .findById(request.getComponentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Component not found with id : "
                                        + request.getComponentId()));


        if (!Boolean.TRUE.equals(component.getIsActive())) {
            throw new IllegalArgumentException(
                    "Selected component is inactive.");
        }

        if (!"Education".equalsIgnoreCase(
                component.getComponentName())) {

            throw new IllegalArgumentException(
                    "Selected component is not Education.");
        }

        SystemUser assignedUser = systemUserRepository
                .findById(request.getAssignedTo())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id : "+ request.getAssignedTo()));

        if (!Boolean.TRUE.equals(assignedUser.getIsActive())) {

            throw new IllegalArgumentException("Selected user is inactive.");
        }


        List<InternalUserComponent> userComponents =
                internalUserComponentRepository.findBySystemUserIdAndIsActiveTrue(assignedUser.getId());

        boolean hasEducationPermission =
                userComponents.stream()
                        .anyMatch(userComponent ->
                                userComponent.getComponent() != null
                                        && userComponent.getComponent().getId().equals(component.getId())
                                        && Boolean.TRUE.equals(userComponent.getComponent().getIsActive()) );

        if (!hasEducationPermission) {
            throw new IllegalArgumentException(
                    "Selected user is not authorized for Education component.");
        }

        SystemUser loggedInUser = loggedInUserService.getLoggedInUser();

        if (loggedInUser == null) {
            throw new IllegalArgumentException(
                    "Logged-in user not found.");
        }

        LocalDateTime now = LocalDateTime.now();


        for (Long verificationId :
                request.getVerificationIds()) {

            if (verificationId == null) {
                throw new IllegalArgumentException(
                        "Verification ID cannot be null.");
            }

            CaseEducationTable education = caseEducationRepository.findById(verificationId).orElseThrow(() ->new ResourceNotFoundException("Education verification not found with id : "+ verificationId));


            if (education.getCasePackageComponentId() == null) {
                throw new IllegalArgumentException(
                        "Case package component is missing for education verification : "
                                + verificationId);
            }



            if (education.getAssignedTo() != null) {
                throw new IllegalStateException(
                        "Education verification already assigned. ID : "
                                + verificationId);
            }


            education.setAssignedTo(assignedUser);
            education.setAssignedBy(loggedInUser);
            education.setAssignedAt(now);
            education.setRejectedBy(null);
            education.setRejectedAt(null);
            education.setProcessingStatus(ComponentSubStatus.ASSIGNED_FOR_PROCESSING);
            
            caseEducationRepository.save(education);
            
            Case caseEntity = caseRepository.findById(education.getCaseId()).orElseThrow(() ->
                            new ResourceNotFoundException("Case not found with id : " + education.getCaseId()));

            CasePackageComponent casePackageComponent =
                    casePackageComponentRepository
                            .findById(education.getCasePackageComponentId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException("Case package component not found with id : " + education.getCasePackageComponentId()));

            caseHistoryService.createHistory(caseEntity,casePackageComponent,education.getSubCaseId(),"ASSIGNED_FOR_PROCESSING","ACTIVITY MESSAGE WILL GO HERE");
  
        }
    }
    
    
    
    @Override
    @Transactional(readOnly = true)
    public List<CaseEducationReassignResponse> getAssignedEducationCases() {

        List<CaseEducationTable> educationCases =
                caseEducationRepository.findByAssignedToIsNotNull();

        return educationCases.stream()
                .map(this::mapToReassignResponse)
                .toList();
    }
    
    
    private CaseEducationReassignResponse mapToReassignResponse(CaseEducationTable education) 
    
    	{

        CaseEducationReassignResponse response = new CaseEducationReassignResponse();

        // Basic education data
        response.setId(education.getId());
        response.setCasePackageComponentId(education.getCasePackageComponentId());

        response.setUniversityName(education.getUniversity() != null ? encryptionService.decrypt(education.getUniversity()) : null );
        response.setCollegeName(education.getInstitution() != null ? encryptionService.decrypt(education.getInstitution()): null);
        response.setYearOfPassing(education.getYearOfPassing() != null? encryptionService.decrypt(education.getYearOfPassing()): null );
        response.setDegree(education.getQualification() != null? encryptionService.decrypt(education.getQualification()): null);

        // Case data
        Case caseEntity = caseRepository.findById(education.getCaseId()).orElse(null);

        if (caseEntity != null)
        	{
            response.setCaseId(caseEntity.getId());
            response.setCaseRef(caseEntity.getCaseRef());
            response.setCaseReceivedDate(caseEntity.getCaseReceivedDate());
            response.setLocation(caseEntity.getLocation() != null ? caseEntity.getLocation().getLocationName() : null);
            response.setCaseInDate( caseEntity.getCaseInDate());
            response.setCaseDueDate(caseEntity.getCaseDueDate());

            StringBuilder candidateName = new StringBuilder();

            if (caseEntity.getFirstName() != null && !caseEntity.getFirstName().isBlank()) 
            {
            		candidateName.append(caseEntity.getFirstName());
            }

            if (caseEntity.getMiddleName() != null  && !caseEntity.getMiddleName().isBlank()) {

                if (candidateName.length() > 0) { candidateName.append(" ");}
                candidateName.append(caseEntity.getMiddleName());
            }

            if (caseEntity.getLastName() != null && !caseEntity.getLastName().isBlank()) {

                if (candidateName.length() > 0) {
                    candidateName.append(" ");
                }
                	candidateName.append(caseEntity.getLastName());
            }
            
            response.setCandidateName(candidateName.toString());
            response.setFatherName(caseEntity.getFatherName());

            response.setDob(caseEntity.getDateOfBirth());

            response.setClientEmployeeId(
                    caseEntity.getClientEmployeeId()
            );

            // Client
            if (caseEntity.getClientInformation() != null) {

                response.setClientName(caseEntity.getClientInformation().getClientName());
            }

            
            if (caseEntity.getLocation() != null)
            {
            	response.setClientLocation(caseEntity.getLocation().getLocationName());
            }
        }

        // Sub case
        response.setSubRefNo( education.getSubCaseId());

        // Component due date
        response.setComponentDueDate(
                education.getComponentDueDate()
        );

       

        // Assigned user
        if (education.getAssignedTo() != null) {

            response.setAssignedTo(
                    education.getAssignedTo().getId()
            );

            String firstName = education.getAssignedTo().getFirstName() != null
                            ? education.getAssignedTo().getFirstName()
                            : "";

            String lastName = education.getAssignedTo().getLastName() != null
                            ? education.getAssignedTo().getLastName()
                            : "";

            response.setAssignedToName((firstName + " " + lastName).trim());
        }

        return response;
    }
    
    
    
    
    
    @Override
    @Transactional
    public void reassignEducationCases(CaseAssignmentRequest request) {
    	
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
    	
    	
    	  SystemUser loggedInUser = loggedInUserService.getLoggedInUser();

    	    if (loggedInUser == null) {
    	        throw new IllegalArgumentException("Logged-in user not found.");
    	        }
    	    
    	    
    	    
    	SystemUser newAssignedUser = systemUserRepository.findById(request.getAssignedTo()).orElseThrow(() ->
            new IllegalArgumentException("Selected user not found."));

    		if (!Boolean.TRUE.equals(newAssignedUser.getIsActive())) {
    			throw new IllegalArgumentException(
    					"Selected user is inactive."
    					);
    		}

    		boolean hasComponentPermission =
    				internalUserComponentRepository
    				.findByComponent_ComponentNameAndComponent_IsActiveTrueAndIsActiveTrueAndSystemUser_IsActiveTrue(
    						"Education"
    						)
    				.stream()
    				.anyMatch(assignment ->
    				assignment.getSystemUser()
    				.getId()
    				.equals(newAssignedUser.getId())
    						);

    		if (!hasComponentPermission) {
    			throw new IllegalArgumentException(
    					"Selected user does not have Education component permission."
    					);
    		}

    		for (Long verificationId : request.getVerificationIds()) {

    			CaseEducationTable education =
    					caseEducationRepository.findById(verificationId)
    					.orElseThrow(() ->
    					new IllegalArgumentException(
    							"Education verification not found: "
    									+ verificationId
    							));

    			if (education.getAssignedTo() == null) {
    				throw new IllegalArgumentException(
    						"Verification " + verificationId
    						+ " is not currently assigned."
    						);
    			}

    			CasePackageComponent casePackageComponent =
    					casePackageComponentRepository.findById(education.getCasePackageComponentId()).orElseThrow(() ->
    						new IllegalArgumentException(
    								"Case package component not found for verification: "
                                + verificationId
    								));

    			Long actualComponentId =casePackageComponent.getComponent().getId();

    			if (!actualComponentId.equals(request.getComponentId())) {
    				throw new IllegalArgumentException("Component mismatch for verification: "+ verificationId);
    			}
    			
    			SystemUser oldAssignedUser = education.getAssignedTo();
    			education.setAssignedTo(newAssignedUser);
    			education.setAssignedBy(loggedInUser);
    			education.setAssignedAt(LocalDateTime.now());
    			education.setRejectedBy(null);
    			education.setRejectedAt(null);

    			caseEducationRepository.save(education);
    			
    			 Case caseEntity = caseRepository.findById(education.getCaseId())
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
    			            education.getSubCaseId(),
    			            "REASSIGNED",
    			            "Education verification reassigned from "
    			                    + oldUserName
    			                    + " to "
    			                    + newUserName
    			    );
    			
    		}}


    @Override
    @Transactional(readOnly = true)
    public Page<CaseEducationMyCaseResponse> getMyEducationCases(Pageable pageable) {

        SystemUser loggedInUser = loggedInUserService.getLoggedInUser();

        if (loggedInUser == null) {
            throw new IllegalArgumentException("Logged-in user not found.");
        }

        Page<CaseEducationTable> educationPage = caseEducationRepository.findByAssignedToIdAndProcessingStatusNotOrderBySubCaseIdDesc(loggedInUser.getId(),ComponentSubStatus.COMPLETE_SEND_TO_QC,pageable);

        return educationPage.map(education -> {

            Case caseEntity =
                    caseRepository.findById(education.getCaseId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Case not found with id : "
                                                    + education.getCaseId()
                                    )
                            );

            CasePackageComponent casePackageComponent =
                    casePackageComponentRepository.findById(
                                    education.getCasePackageComponentId()
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Case package component not found with id : "
                                                    + education.getCasePackageComponentId()
                                    )
                            );

            return mapToMyCaseResponse(
                    education,
                    caseEntity,
                    casePackageComponent
            );
        });
    }
    
  
  private CaseEducationMyCaseResponse mapToMyCaseResponse(
	        CaseEducationTable education,
	        Case caseEntity,
	        CasePackageComponent casePackageComponent) {

	    CaseEducationMyCaseResponse response = new CaseEducationMyCaseResponse();

	    
	    response.setId(education.getId());

	    response.setCaseRef(caseEntity.getCaseRef());

	    response.setSubRefNo(education.getSubCaseId());

	    
	    String clientName =
	            caseEntity.getClientInformation() != null
	                    ? caseEntity.getClientInformation().getClientName()
	                    : null;

	    String clientLocation =
	            caseEntity.getLocation() != null
	                    ? caseEntity.getLocation().getLocationName()
	                    : null;

	    String clientNameLocation = null;

	    if (clientName != null && clientLocation != null) {
	        clientNameLocation = clientName + " / " + clientLocation;
	    } else if (clientName != null) {
	        clientNameLocation = clientName;
	    } else {
	        clientNameLocation = clientLocation;
	    }

	    response.setClientNameLocation(clientNameLocation);


	    String candidateName =
	            ((caseEntity.getFirstName() != null
	                        ? caseEntity.getFirstName()
	                        : "")
	                + " "
	                + (caseEntity.getMiddleName() != null
	                        ? caseEntity.getMiddleName() + " "
	                        : "")
	                + (caseEntity.getLastName() != null
	                        ? caseEntity.getLastName()
	                        : "")
	            ).trim();

	    response.setCandidateName(candidateName);
	    response.setFatherName(caseEntity.getFatherName());
	    response.setClientEmployeeId(caseEntity.getClientEmployeeId());	   
	    response.setMobileNo(caseEntity.getMobileNumber() != null? encryptionService.decrypt(caseEntity.getMobileNumber()): null);
	    response.setDob(caseEntity.getDateOfBirth() != null? caseEntity.getDateOfBirth().toString(): null);
	    response.setCaseReceivedDate(caseEntity.getCaseReceivedDate() != null? caseEntity.getCaseReceivedDate().toString(): null);
	    response.setCaseInDate(caseEntity.getCaseInDate() != null? caseEntity.getCaseInDate().toString(): null);
	    response.setCaseDueDate(caseEntity.getCaseDueDate() != null? caseEntity.getCaseDueDate().toString(): null);
	    response.setComponentDueDate(education.getComponentDueDate() != null? education.getComponentDueDate().toString(): null);
	    response.setComponentDetail(education.getUniversity() != null? encryptionService.decrypt(education.getUniversity()): null);
	    response.setComponentStatus(education.getProcessingStatus() != null? education.getProcessingStatus().name(): null);

	    return response;
	}	
  
  @Override
  @Transactional
  public String refuseEducationCase(
          Long id,
          String rejectionComment) {

      if (id == null) {
          throw new IllegalArgumentException(
                  "Education verification ID is required."
          );
      }

      if (rejectionComment == null
              || rejectionComment.isBlank()) {

          throw new IllegalArgumentException(
                  "Rejection comment is required."
          );
      }

      SystemUser loggedInUser =
              loggedInUserService.getLoggedInUser();

      if (loggedInUser == null) {
          throw new IllegalArgumentException(
                  "Logged-in user not found."
          );
      }

      CaseEducationTable education =
              caseEducationRepository.findById(id)
                      .orElseThrow(() ->
                              new ResourceNotFoundException(
                                      "Education verification not found with id : "
                                              + id
                              ));

      if (education.getAssignedTo() == null) {
          throw new IllegalStateException(
                  "Education verification is not currently assigned."
          );
      }

      if (!education.getAssignedTo().getId()
              .equals(loggedInUser.getId())) {

          throw new IllegalStateException(
                  "You are not assigned to this Education verification."
          );
      }

      LocalDateTime now = LocalDateTime.now();

      education.setAssignedTo(null);
      education.setAssignedBy(null);
      education.setRejectedBy(loggedInUser);
      education.setRejectedAt(now);
      education.setRejectionComment(rejectionComment.trim());

      caseEducationRepository.save(education);

      Case caseEntity =
              caseRepository.findById(education.getCaseId())
                      .orElseThrow(() ->
                              new ResourceNotFoundException(
                                      "Case not found with id : "
                                              + education.getCaseId()
                              ));

      CasePackageComponent casePackageComponent =
              casePackageComponentRepository
                      .findById(
                              education.getCasePackageComponentId()
                      )
                      .orElseThrow(() ->
                              new ResourceNotFoundException(
                                      "Case package component not found."
                              ));

      caseHistoryService.createHistory(
              caseEntity,
              casePackageComponent,
              education.getSubCaseId(),
              "REJECTED",
              rejectionComment.trim()
      );

      return education.getRejectionComment();
  }
  
  
  @Override
  @Transactional(readOnly = true)
  public CaseEducationResponse getEducationById(Long id) {

      if (id == null) {
          throw new IllegalArgumentException("Education verification ID is required.");
      }

      CaseEducationTable education = caseEducationRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException("Education verification not found with id : "+ id));
      return mapToResponse(education);
  }
  
  @Override
  @Transactional
  public void startProcessing(Long id) {

      CaseEducationTable education = caseEducationRepository.findById(id) .orElseThrow(() ->
                              new ResourceNotFoundException("Education verification not found with id : " + id));

      if (education.getProcessingStatus() == ComponentSubStatus.ASSIGNED_FOR_PROCESSING) {
    	    education.setProcessingStatus(ComponentSubStatus.WIP);
    	    Case caseEntity = caseRepository.findById(education.getCaseId())
    	            .orElseThrow(() ->
    	                    new ResourceNotFoundException(
    	                            "Case not found with id : "
    	                                    + education.getCaseId()));

    	    CasePackageComponent casePackageComponent =
    	            casePackageComponentRepository
    	                    .findById(education.getCasePackageComponentId())
    	                    .orElseThrow(() ->
    	                            new ResourceNotFoundException(
    	                                    "Case package component not found."));

    	    caseHistoryService.createHistory(
    	            caseEntity,
    	            casePackageComponent,
    	            education.getSubCaseId(),
    	            "WIP",
    	            "Case check Status to WIP");
    	}

      education.setUpdatedAt(LocalDateTime.now());
      education.setUpdatedBy(loggedInUserService.getLoggedInUser());
      caseEducationRepository.save(education);
  }
  
  
  @Override
  @Transactional
  public void updateProcessingStatus(
          Long id,
          ComponentSubStatus status,
          DispositionStatus dispositionStatus,
          String activity) {

      if (id == null) {
          throw new IllegalArgumentException(
                  "Education verification ID is required."
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


      if ((status == ComponentSubStatus.COMPLETE_SEND_TO_QC
              || status == ComponentSubStatus.QC_COMPLETE)
              && dispositionStatus == null) {

          throw new IllegalArgumentException("Disposition status is required for this processing status.");
      }


      CaseEducationTable education =caseEducationRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException("Education verification not found with id : "+ id));


      education.setProcessingStatus(status);


      if (status == ComponentSubStatus.COMPLETE_SEND_TO_QC) {

          // Process Team sends case to QC
          education.setDispositionStatus(dispositionStatus);

          // QC disposition should be empty initially
          education.setQcDispositionStatus(null);

      } else if (status == ComponentSubStatus.QC_COMPLETE) {

          // QC has completed the verification
          // Keep the original Process Team disposition
          // and save the QC disposition separately.
          education.setQcDispositionStatus(dispositionStatus);

      } else if (status == ComponentSubStatus.RE_WORK) {

          // QC sends case back for re-work
    	  education.setProcessingStatus(status.ASSIGNED_FOR_PROCESSING);
          education.setDispositionStatus(null);
          education.setQcDispositionStatus(null);

      } else {

          // Existing processing statuses
          education.setDispositionStatus(null);
          education.setQcDispositionStatus(null);
      }



      SystemUser loggedInUser = loggedInUserService.getLoggedInUser();

      if (loggedInUser == null) {
          throw new IllegalArgumentException(
                  "Logged-in user not found.");
      }

      LocalDateTime now = LocalDateTime.now();

      education.setUpdatedAt(now);
      education.setUpdatedBy(loggedInUser);

      CaseEducationTable savedEducation = caseEducationRepository.save(education);

  

      Case caseEntity =
              caseRepository.findById(savedEducation.getCaseId())
                      .orElseThrow(() ->
                              new ResourceNotFoundException(
                                      "Case not found with id : "
                                              + savedEducation.getCaseId()));


      CasePackageComponent casePackageComponent =
              casePackageComponentRepository.findById(
                      savedEducation.getCasePackageComponentId())
              .orElseThrow(() ->new ResourceNotFoundException("Case package component not found with id : " + savedEducation.getCasePackageComponentId()));


      caseHistoryService.createHistory(
              caseEntity,
              casePackageComponent,
              savedEducation.getSubCaseId(),
              status.name(),
              activity
      );
  }
	  
}
