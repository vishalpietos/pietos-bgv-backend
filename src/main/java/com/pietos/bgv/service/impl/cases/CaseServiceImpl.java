package com.pietos.bgv.service.impl.cases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pietos.bgv.constant.ComponentCodes;
import com.pietos.bgv.dto.request.cases.CaseRequest;
import com.pietos.bgv.dto.response.cases.CaseResponse;
import com.pietos.bgv.entity.ClientInformation;
import com.pietos.bgv.entity.ClientLocation;
import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.entity.cases.Case;
import com.pietos.bgv.entity.cases.CaseEducationTable;
import com.pietos.bgv.entity.cases.CaseEmploymentTable;
import com.pietos.bgv.entity.cases.CasePackage;
import com.pietos.bgv.entity.cases.CasePackageComponent;
import com.pietos.bgv.enums.CaseStatus;
import com.pietos.bgv.enums.DataEntryStatus;
import com.pietos.bgv.exception.ResourceNotFoundException;
import com.pietos.bgv.repository.ClientInformationRepository;
import com.pietos.bgv.repository.ClientLocationRepository;
import com.pietos.bgv.repository.cases.CaseEducationRepository;
import com.pietos.bgv.repository.cases.CaseEmploymentRepository;
import com.pietos.bgv.repository.cases.CasePackageComponentRepository;
import com.pietos.bgv.repository.cases.CasePackageRepository;
import com.pietos.bgv.repository.cases.CaseRepository;
import com.pietos.bgv.security.EncryptionService;
import com.pietos.bgv.security.LoggedInUserService;
import com.pietos.bgv.service.cases.CaseHistoryService;
import com.pietos.bgv.service.cases.CaseService;
import com.pietos.bgv.util.CaseReferenceUtil;

@Service
@Transactional
public class CaseServiceImpl implements CaseService {

    private final ClientInformationRepository clientInformationRepository;
    private final ClientLocationRepository clientLocationRepository;
    private final LoggedInUserService loggedInUserService;    
    private final EncryptionService encryptionService;   
    private static final Logger logger = LoggerFactory.getLogger(CaseServiceImpl.class);
    private final CaseRepository caseRepository;
    private final CasePackageRepository casePackageRepository;
    private final CasePackageComponentRepository casePackageComponentRepository;

    private final CaseEducationRepository caseEducationRepository;
    private final CaseEmploymentRepository caseEmploymentRepository;
    
    private final CaseHistoryService caseHistoryService;


    public CaseServiceImpl(
            CaseRepository caseRepository,
            ClientInformationRepository clientInformationRepository,
            ClientLocationRepository clientLocationRepository,
            LoggedInUserService loggedInUserService,
            EncryptionService encryptionService,
            CaseHistoryService caseHistoryService,
            CasePackageRepository casePackageRepository,
            CasePackageComponentRepository casePackageComponentRepository,
            CaseEducationRepository caseEducationRepository,
            CaseEmploymentRepository caseEmploymentRepository) {

        this.caseRepository = caseRepository;
      
        this.clientInformationRepository = clientInformationRepository;
        this.clientLocationRepository = clientLocationRepository;
        this.loggedInUserService = loggedInUserService;
        this.encryptionService = encryptionService;
        this.caseHistoryService = caseHistoryService;
        this.casePackageRepository = casePackageRepository;
        this.casePackageComponentRepository = casePackageComponentRepository;
        this.caseEducationRepository = caseEducationRepository;
        this.caseEmploymentRepository = caseEmploymentRepository;
        
    }


    @Override
    public CaseResponse createCase(CaseRequest request) {

     
        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();


      
        ClientInformation client =
                clientInformationRepository
                        .findById(request.getClientId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Client not found with id : "
                                                + request.getClientId()));


     
        ClientLocation clientLocation =
                clientLocationRepository
                        .findByIdAndClientInformationId(
                                request.getLocationId(),
                                request.getClientId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Location is not assigned to this client."));


        //  Validate request data

        if (request.getCaseReceivedDate() == null) {

            throw new IllegalArgumentException(
                    "Case received date is required.");
        }

        if (request.getEmployment() == null) {

            throw new IllegalArgumentException(
                    "Employment type is required.");
        }

        if (request.getFirstName() == null
                || request.getFirstName().isBlank()) {

            throw new IllegalArgumentException(
                    "First name is required.");
        }

        if (request.getLastName() == null
                || request.getLastName().isBlank()) {

            throw new IllegalArgumentException(
                    "Last name is required.");
        }
        
        if (request.getMobileNumber() == null
                || request.getMobileNumber().isBlank()) {

            throw new IllegalArgumentException(
                    "Mobile number is required.");
        }

        if (request.getEmail() == null
                || request.getEmail().isBlank()) {

            throw new IllegalArgumentException(
                    "Email is required.");
        }
        
        Case caseEntity = new Case();

        caseEntity.setClientInformation(client);

        caseEntity.setLocation(clientLocation);

        caseEntity.setCaseReceivedDate(request.getCaseReceivedDate());

        // Case In Date = current date
        caseEntity.setCaseInDate(LocalDate.now());

        // Will be calculated later using TAT + holidays
        caseEntity.setCaseDueDate(null);

        caseEntity.setEmployment(request.getEmployment());

        caseEntity.setFirstName(request.getFirstName());

        caseEntity.setMiddleName(request.getMiddleName());

        caseEntity.setLastName(request.getLastName());

        caseEntity.setDateOfBirth(request.getDateOfBirth());

        caseEntity.setFatherName(request.getFatherName());
        
        caseEntity.setEmployment(request.getEmployment());

        caseEntity.setClientEmployeeId(request.getClientEmployeeId());

        caseEntity.setDateOfJoining(request.getDateOfJoining());

        caseEntity.setMobileNumber(encryptionService.encrypt(request.getMobileNumber()));

        caseEntity.setEmail(encryptionService.encrypt(request.getEmail()));

        caseEntity.setGender(request.getGender());

        // Initial Status
        caseEntity.setStatus(CaseStatus.NEW);

        //  Audit
        caseEntity.setCreatedBy(loggedInUser);
        caseEntity.setUpdatedBy(loggedInUser);

        //  Save Case
        // Database generates the Case ID here
        Case savedCase = caseRepository.save(caseEntity);


        //  Generate Case Reference
        String caseRef = CaseReferenceUtil.generateCaseReference(
                        client.getAbbreviation(),
                        client.getId(),
                        savedCase.getId()
                );

        savedCase.setCaseRef(caseRef);
        
        caseHistoryService.createHistory(
                savedCase,
                CaseStatus.NEW.name(),
                "Case Created"
        );

        logger.info(
                "Case created successfully. caseRef={}, clientId={}, createdBy={}",
                savedCase.getCaseRef(),
                client.getId(),
                loggedInUser.getId()
        );


        // 12. Return Response
        return mapToResponse(savedCase);
    }


    @Override
    @Transactional(readOnly = true)
    public CaseResponse getCaseById(Long id) {

        logger.info(
                "Fetching case. caseId={}",
                id
        );

        Case caseEntity =caseRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Case not found with id : " + id));

        return mapToResponse(caseEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CaseResponse> getAllCases() {

        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();

        String roleName =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElse(null);

        List<Case> cases;

        if ("ADMIN".equals(roleName)
                || "SUPER_ADMIN".equals(roleName)) {

            // Admin and Super Admin can see all cases
            logger.info(
                    "Fetching all cases. role={}, userId={}",
                    roleName,
                    loggedInUser.getId()
            );

            cases = caseRepository.findAll();

        } else if ("DATA_ENTRY".equals(roleName)) {

            // Data Entry can see only cases created by them
            logger.info(
                    "Fetching cases created by Data Entry user. userId={}",
                    loggedInUser.getId()
            );

            cases = caseRepository.findByCreatedById(
                    loggedInUser.getId());

        } else {

            throw new IllegalArgumentException(
                    "You are not authorized to view cases.");
        }

        return cases.stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public CaseResponse updateCase(
            Long id,
            CaseRequest request) {

      
        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();


        
        Case caseEntity =
                caseRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Case not found with id : "
                                                + id));


      
        ClientInformation client =
                clientInformationRepository
                        .findById(request.getClientId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Client not found with id : "
                                                + request.getClientId()));


        
        ClientLocation clientLocation =
                clientLocationRepository
                        .findByIdAndClientInformationId(
                                request.getLocationId(),
                                request.getClientId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Location is not assigned to this client."));


        
        if (request.getCaseReceivedDate() == null) {

            throw new IllegalArgumentException(
                    "Case received date is required.");
        }

        if (request.getCaseReceivedDate()
                .isAfter(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Case received date cannot be a future date.");
        }


        
        if (request.getEmployment() == null) {

            throw new IllegalArgumentException(
                    "Employment type is required.");
        }


       
        if (request.getFirstName() == null
                || request.getFirstName().isBlank()) {

            throw new IllegalArgumentException(
                    "First name is required.");
        }


       
        if (request.getLastName() == null
                || request.getLastName().isBlank()) {

            throw new IllegalArgumentException(
                    "Last name is required.");
        }


        // 9. Update Client
        caseEntity.setClientInformation(client);
        caseEntity.setLocation(clientLocation);
        caseEntity.setCaseReceivedDate(request.getCaseReceivedDate());
        caseEntity.setEmployment(request.getEmployment());
        caseEntity.setFirstName(request.getFirstName());
        caseEntity.setMiddleName(request.getMiddleName());
        caseEntity.setLastName(request.getLastName());
        caseEntity.setDateOfBirth(request.getDateOfBirth());
        caseEntity.setFatherName(request.getFatherName());
        caseEntity.setClientEmployeeId(request.getClientEmployeeId());
        caseEntity.setDateOfJoining(request.getDateOfJoining());

        if (request.getMobileNumber() != null
                && !request.getMobileNumber().isBlank()) {

            caseEntity.setMobileNumber(
                    encryptionService.encrypt(
                            request.getMobileNumber()));
        }

        if (request.getEmail() != null
                && !request.getEmail().isBlank()) {

            caseEntity.setEmail(
                    encryptionService.encrypt(
                            request.getEmail()));
        }
        caseEntity.setGender(request.getGender());
        caseEntity.setUpdatedBy(loggedInUser);
        Case updatedCase = caseRepository.save(caseEntity);
        logger.info(
                "Case updated successfully. caseId={}, caseRef={}, updatedBy={}",
                updatedCase.getId(),
                updatedCase.getCaseRef(),
                loggedInUser.getId()
        );
        return mapToResponse(updatedCase);
    }

private CaseResponse mapToResponse(Case caseEntity) {

	    CaseResponse response = new CaseResponse();

	    // Basic Case Information
	    response.setId(caseEntity.getId());
	    response.setCaseRef(caseEntity.getCaseRef());
	    // Client
	    response.setClientId(caseEntity.getClientInformation().getId());
	    response.setClientName(caseEntity.getClientInformation().getClientName());
	    response.setLocationId(caseEntity.getLocation().getId());
	    response.setLocationName(caseEntity.getLocation().getLocationName());
	    response.setCaseReceivedDate(caseEntity.getCaseReceivedDate());
	    response.setCaseInDate(caseEntity.getCaseInDate());
	    response.setCaseDueDate(caseEntity.getCaseDueDate());
	    response.setEmployment(caseEntity.getEmployment());
	    response.setFirstName(caseEntity.getFirstName());
	    response.setMiddleName(caseEntity.getMiddleName());
	    response.setLastName(caseEntity.getLastName());
	    response.setDateOfBirth(caseEntity.getDateOfBirth());
	    response.setFatherName(caseEntity.getFatherName());
	    response.setClientEmployeeId(caseEntity.getClientEmployeeId());
	    response.setDateOfJoining(caseEntity.getDateOfJoining());
	    response.setMobileNumber(encryptionService.decrypt(caseEntity.getMobileNumber()));
	    response.setEmail(encryptionService.decrypt(caseEntity.getEmail()));
	    response.setGender(caseEntity.getGender());
	    response.setStatus(caseEntity.getStatus());

	    // Report
	    response.setReport(caseEntity.getReport());

	    // Created By
	    if (caseEntity.getCreatedBy() != null) {

	        response.setCreatedBy(caseEntity.getCreatedBy().getId());

	        response.setCreatedByName(getUserFullName(caseEntity.getCreatedBy()));
	    }

	    // Updated By
	    if (caseEntity.getUpdatedBy() != null) {

	        response.setUpdatedBy(caseEntity.getUpdatedBy().getId());

	        response.setUpdatedByName(getUserFullName(caseEntity.getUpdatedBy()));
	    }

	    // Audit Dates
	    response.setCreatedAt(caseEntity.getCreatedAt());

	    response.setUpdatedAt(caseEntity.getUpdatedAt());

	    return response;
	}
	
	 private String getUserFullName(SystemUser user) {

	        String firstName = user.getFirstName();
	        String lastName = user.getLastName();

	        if (lastName == null || lastName.isBlank()) {
	            return firstName;
	        }

	        return firstName + " " + lastName;
	    }
	 
	 @Override
	 @Transactional(readOnly = true)
	 public List<CaseResponse> getMyCases() {

	     SystemUser loggedInUser =
	             loggedInUserService.getLoggedInUser();

	     List<Case> cases =
	             caseRepository.findByCreatedById(
	                     loggedInUser.getId());

	     return cases.stream()
	             .map(this::mapToResponse)
	             .toList();
	 }
	 
	 
	 @Override
	 @Transactional(readOnly = true)
	 public CaseStatus evaluateDataEntryStatus(Long caseId) {

	     // ---------------------------------------------------------
	     // 1. Validate Case
	     // ---------------------------------------------------------

	     Case caseEntity = caseRepository.findById(caseId)
	             .orElseThrow(() ->
	                     new ResourceNotFoundException(
	                             "Case not found with id : " + caseId));


	     // ---------------------------------------------------------
	     // 2. Get Case Package
	     // ---------------------------------------------------------

	     CasePackage casePackage = casePackageRepository
	             .findByCaseEntityId(caseId)
	             .orElseThrow(() ->
	                     new ResourceNotFoundException(
	                             "Case package not found for case id : "
	                                     + caseId));


	     // ---------------------------------------------------------
	     // 3. Get all check instances/components
	     // ---------------------------------------------------------

	     List<CasePackageComponent> components = casePackageComponentRepository
	                     .findByCasePackageId(casePackage.getId());


	     // No check yet
	     if (components == null || components.isEmpty()) {
	         return CaseStatus.NEW;
	     }


	     // ---------------------------------------------------------
	     // 4. Get status of every check
	     // ---------------------------------------------------------

	     List<DataEntryStatus> checkStatuses =  new ArrayList<>();
	     
	     	System.out.println(
	    	        "========== EVALUATING CASE =========="
	    	);

	    	System.out.println(
	    	        "CASE ID = " + caseId
	    	);


	     for (CasePackageComponent component : components) {

	         DataEntryStatus status =getDataEntryStatusForComponent(component);
	         
	         System.out.println(
	                 "COMPONENT ID = "
	                 + component.getId()
	                 + " | CODE = "
	                 + component.getComponent().getComponentCode()
	                 + " | STATUS = "
	                 + status
	         );

	         // Actual verification record does not exist yet
	         // means this check is still incomplete.
	         if (status == null) {
	             return CaseStatus.NEW;
	         }

	         checkStatuses.add(status);
	     }


	     // ---------------------------------------------------------
	     // 5. ANY check has L1 insufficiency
	     // ---------------------------------------------------------

	     boolean hasInsufficiency =
	             checkStatuses.stream()
	                     .anyMatch(status ->
	                             status == DataEntryStatus.L1_INSUFFICIENCY);

	     if (hasInsufficiency) {
	    	 System.out.println( "FINAL CASE STATUS = L1_INSUFFICIENCY");
	         return CaseStatus.L1_INSUFFICIENCY;
	     }


	     // ---------------------------------------------------------
	     // 6. ALL checks are SEND_TO_L1_QC
	     // ---------------------------------------------------------

	     boolean allSendToL1Qc =
	             checkStatuses.stream()
	                     .allMatch(status ->
	                             status == DataEntryStatus.SEND_TO_L1_QC);

	     if (allSendToL1Qc) {
	         return CaseStatus.WIP;
	     }


	     // ---------------------------------------------------------
	     // 7. Every check is completed from Data Entry perspective
	     //
	     // DATA_ENTRY_DONE + SEND_TO_L1_QC
	     // both mean Data Entry work is complete.
	     //
	     // But at least one must be DATA_ENTRY_DONE.
	     // ---------------------------------------------------------

	     boolean allCompleted =
	             checkStatuses.stream()
	                     .allMatch(status ->
	                             status == DataEntryStatus.DATA_ENTRY_DONE
	                             || status == DataEntryStatus.SEND_TO_L1_QC);


	     boolean hasDataEntryDone =
	             checkStatuses.stream()
	                     .anyMatch(status ->
	                             status == DataEntryStatus.DATA_ENTRY_DONE);


	     if (allCompleted && hasDataEntryDone) {
	         return CaseStatus.DATA_ENTRY_COMPLETE;
	     }



	     return CaseStatus.NEW;
	 }
	 
	 
	 
	 private DataEntryStatus getDataEntryStatusForComponent(
		        CasePackageComponent component) {

		    Long componentId = component.getId();

		    String componentCode = component.getComponent().getComponentCode();


		    if (ComponentCodes.EDUCATION.equals(componentCode)) {

		        return caseEducationRepository
		                .findByCasePackageComponentId(componentId)
		                .map(CaseEducationTable::getStatus)
		                .orElse(null);
		    }


		    // =========================================================
		    // EMPLOYMENT
		    // =========================================================

		    if (ComponentCodes.EMPLOYMENT.equals(componentCode)) {

		        List<CaseEmploymentTable> employmentRecords =
		                caseEmploymentRepository
		                        .findByCasePackageComponentId(componentId);

		        if (employmentRecords == null
		                || employmentRecords.isEmpty()) {

		            return null;
		        }


		        // If any employment record has insufficiency,
		        // this component is considered insufficient.
		        boolean hasInsufficiency =
		                employmentRecords.stream()
		                        .anyMatch(record ->
		                                record.getStatus()
		                                        == DataEntryStatus.L1_INSUFFICIENCY);

		        if (hasInsufficiency) {
		            return DataEntryStatus.L1_INSUFFICIENCY;
		        }


		        // All employment records must be completed
		        // from Data Entry perspective.
		        boolean allCompleted =
		                employmentRecords.stream()
		                        .allMatch(record ->
		                                record.getStatus()
		                                        == DataEntryStatus.DATA_ENTRY_DONE
		                                || record.getStatus()
		                                        == DataEntryStatus.SEND_TO_L1_QC);

		        if (!allCompleted) {
		            return null;
		        }


		        // At least one DATA_ENTRY_DONE means this component
		        // is not in the "all SEND_TO_L1_QC" situation.
		        boolean hasDataEntryDone =
		                employmentRecords.stream()
		                        .anyMatch(record ->
		                                record.getStatus()
		                                        == DataEntryStatus.DATA_ENTRY_DONE);

		        if (hasDataEntryDone) {
		            return DataEntryStatus.DATA_ENTRY_DONE;
		        }


		        // All records are SEND_TO_L1_QC.
		        return DataEntryStatus.SEND_TO_L1_QC;
		    }
		    // =========================================================
		    // UNSUPPORTED COMPONENT
		    // =========================================================
		    throw new IllegalArgumentException(
		            "Unsupported data entry component code: "
		                    + componentCode);
		}
}