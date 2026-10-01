package com.pietos.bgv.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pietos.bgv.dto.request.InternalUserInfoRequest;
import com.pietos.bgv.dto.response.InternalUserInfoResponse;
import com.pietos.bgv.entity.InternalUserInfo;
import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.exception.DuplicateResourceException;
import com.pietos.bgv.exception.ResourceNotFoundException;
import com.pietos.bgv.repository.InternalUserInfoRepository;
import com.pietos.bgv.repository.SystemUserRepository;
import com.pietos.bgv.service.EmailService;
import com.pietos.bgv.email.EmailTemplateService;
import com.pietos.bgv.service.InternalUserInfoService;
import com.pietos.bgv.security.LoggedInUserService;
import com.pietos.bgv.util.PasswordGenerator;
import com.pietos.bgv.email.EmailSubject;

@Service
@Transactional
public class InternalUserInfoServiceImpl
        implements InternalUserInfoService {

    private final InternalUserInfoRepository internalUserInfoRepository;
    private final SystemUserRepository systemUserRepository;
    private final LoggedInUserService loggedInUserService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EmailTemplateService emailTemplateService;

    public InternalUserInfoServiceImpl(
            InternalUserInfoRepository internalUserInfoRepository,
            SystemUserRepository systemUserRepository,
            LoggedInUserService loggedInUserService,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            EmailTemplateService emailTemplateService) {

        this.internalUserInfoRepository = internalUserInfoRepository;
        this.systemUserRepository = systemUserRepository;
        this.loggedInUserService = loggedInUserService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.emailTemplateService = emailTemplateService;
    }

    // =====================================================
    // CREATE INTERNAL USER
    // =====================================================

    @Transactional
    public InternalUserInfoResponse createUser(
            InternalUserInfoRequest request) {

        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();

        // =====================================================
        // VALIDATE REQUEST
        // =====================================================

        if (request == null) {

            throw new IllegalArgumentException(
                    "Internal user request cannot be null.");
        }

        if (request.getFirstName() == null
                || request.getFirstName().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "First name is required.");
        }

        if (request.getLastName() == null
                || request.getLastName().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Last name is required.");
        }

        if (request.getEmail() == null
                || request.getEmail().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Email is required.");
        }

        if (request.getMobileNumber() == null
                || request.getMobileNumber().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Mobile number is required.");
        }

        String email =
                request.getEmail().trim();

        String mobileNumber =
                request.getMobileNumber().trim();

        // =====================================================
        // CHECK DUPLICATE EMAIL
        // =====================================================

        if (internalUserInfoRepository.existsByEmail(email)) {

            throw new DuplicateResourceException(
                    "Email already exists.");
        }

        if (systemUserRepository.findByEmail(email).isPresent()) {

            throw new DuplicateResourceException(
                    "Email already exists.");
        }

        // =====================================================
        // CHECK DUPLICATE MOBILE
        // =====================================================

        if (internalUserInfoRepository
                .existsByMobileNumber(mobileNumber)) {

            throw new DuplicateResourceException(
                    "Mobile number already exists.");
        }

        if (systemUserRepository
                .existsByMobileNumber(mobileNumber)) {

            throw new DuplicateResourceException(
                    "Mobile number already exists.");
        }

        // =====================================================
        // GENERATE PASSWORD
        // =====================================================

        String generatedPassword =PasswordGenerator.generatePassword(PasswordGenerator.DEFAULT_PASSWORD_LENGTH);

        // =====================================================
        // CREATE SYSTEM USER FIRST
        // =====================================================

        SystemUser systemUser =new SystemUser();

        systemUser.setFirstName(request.getFirstName().trim());

        systemUser.setLastName(request.getLastName().trim());

        systemUser.setEmail(email);

        systemUser.setMobileNumber(mobileNumber);

        systemUser.setAlternateNumber(request.getAlternateNumber());

        systemUser.setPassword(passwordEncoder.encode(generatedPassword));

        /*
         * IMPORTANT:
         *
         * Role is intentionally NOT set here.
         *
         * Role will be assigned from Role Manager.
         *
         * Primary Role:
         *      system_users.role_id
         *
         * All assigned roles:
         *      internal_user_roles
         */

        systemUser.setIsActive(request.getIsActive() != null
                        ? request.getIsActive()
                        : true
        );

        systemUser.setCreatedBy(loggedInUser);

        systemUser.setUpdatedBy(loggedInUser);

        systemUser.setCreatedAt(
                LocalDateTime.now()
        );

        systemUser.setUpdatedAt(
                LocalDateTime.now()
        );

        // SAVE SYSTEM USER FIRST
        SystemUser savedSystemUser =systemUserRepository.save(systemUser);

        // =====================================================
        // CREATE INTERNAL USER INFO
        // =====================================================

        InternalUserInfo internalUser = new InternalUserInfo();

        // IMPORTANT:
        // Connect InternalUserInfo -> SystemUser
        internalUser.setSystemUser(savedSystemUser);

        internalUser.setFirstName(request.getFirstName().trim());

        internalUser.setLastName(request.getLastName().trim());

        internalUser.setEmail(email);

        internalUser.setMobileNumber(mobileNumber);

        internalUser.setAlternateNumber(request.getAlternateNumber());

        internalUser.setLocation(request.getLocation());

        internalUser.setAddress(request.getAddress());

        internalUser.setCountry(request.getCountry());

        internalUser.setState(request.getState());

        internalUser.setCity(request.getCity());

        internalUser.setPincode(request.getPincode());

        internalUser.setJoiningDate(request.getJoiningDate());

        internalUser.setIsActive( request.getIsActive() != null ? request.getIsActive(): true);

        internalUser.setCreatedBy(loggedInUser);

        internalUser.setUpdatedBy(loggedInUser);

        internalUser.setCreatedAt(LocalDateTime.now());

        internalUser.setUpdatedAt(LocalDateTime.now());

        // SAVE INTERNAL USER INFO
        InternalUserInfo savedInternalUser =internalUserInfoRepository.save(internalUser);

        // =====================================================
        // SEND CONFIRMATION EMAIL TO MAM
        // =====================================================

        try {

            String fullName = savedInternalUser.getFirstName() + " " + savedInternalUser.getLastName();

            String confirmationHtml =
                    emailTemplateService
                            .internalUserCreatedConfirmationTemplate(
                                    fullName,
                                    savedInternalUser.getEmail(),
                                    savedInternalUser.getMobileNumber(),
                                    generatedPassword
                            );

            String superAdminEmail =
                    "Vishalp@Pietos.com";

            emailService.sendEmail(
                    superAdminEmail,
                    EmailSubject.INTERNAL_USER_CREATED_CONFIRMATION,
                    confirmationHtml
            );

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Internal User creation failed because confirmation email could not be sent.",
                    e
            );
        }

        // =====================================================
        // RETURN RESPONSE
        // =====================================================

        return mapToResponse(
                savedInternalUser
        );
    }
    // =====================================================
    // GET ALL INTERNAL USERS
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<InternalUserInfoResponse> getAllUsers() {

        return internalUserInfoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET INTERNAL USER BY ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public InternalUserInfoResponse getUserById(
            Long id) {

        InternalUserInfo internalUser =
                internalUserInfoRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Internal user not found with id : "
                                                + id));

        return mapToResponse(internalUser);
    }

    // =====================================================
    // UPDATE INTERNAL USER
    // =====================================================

    @Transactional
    public InternalUserInfoResponse updateUser(Long id, InternalUserInfoRequest request) {

        SystemUser loggedInUser = loggedInUserService.getLoggedInUser();

        // =====================================================
        // VALIDATE REQUEST
        // =====================================================

        if (request == null) {
        	throw new IllegalArgumentException(
                    "Internal user request cannot be null.");
        }

        if (request.getFirstName() == null
                || request.getFirstName().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "First name is required.");
        }

        if (request.getLastName() == null
                || request.getLastName().trim().isEmpty()) {
        	throw new IllegalArgumentException(
                    "Last name is required.");
        }

        if (request.getEmail() == null
                || request.getEmail().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Email is required.");
        }

        if (request.getMobileNumber() == null
                || request.getMobileNumber().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Mobile number is required.");
        }

        String email =
                request.getEmail().trim();

        String mobileNumber =
                request.getMobileNumber().trim();

        // =====================================================
        // FIND INTERNAL USER
        // =====================================================

        InternalUserInfo internalUser =
                internalUserInfoRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Internal user not found with id : "
                                                + id
                                )
                        );

        // =====================================================
        // FIND CONNECTED SYSTEM USER
        // =====================================================

        SystemUser systemUser =
                internalUser.getSystemUser();

        if (systemUser == null) {

            throw new ResourceNotFoundException(
                    "System user not found for internal user."
            );
        }

        // =====================================================
        // CHECK DUPLICATE EMAIL
        // =====================================================

        if (internalUserInfoRepository
                .existsByEmailAndIdNot(email, id)) {

            throw new DuplicateResourceException(
                    "Email already exists."
            );
        }

        // Check email in system_users,
        // excluding the current system user
        if (!email.equalsIgnoreCase(systemUser.getEmail())
                && systemUserRepository.findByEmail(email).isPresent()) {

            throw new DuplicateResourceException(
                    "Email already exists."
            );
        }

        // =====================================================
        // CHECK DUPLICATE MOBILE
        // =====================================================

        if (!mobileNumber.equals(systemUser.getMobileNumber())) {

            if (internalUserInfoRepository
                    .existsByMobileNumber(mobileNumber)) {

                throw new DuplicateResourceException(
                        "Mobile number already exists."
                );
            }

            if (systemUserRepository
                    .existsByMobileNumber(mobileNumber)) {

                throw new DuplicateResourceException(
                        "Mobile number already exists."
                );
            }
        }

        // =====================================================
        // UPDATE INTERNAL USER
        // =====================================================

        internalUser.setFirstName(
                request.getFirstName().trim()
        );

        internalUser.setLastName(
                request.getLastName().trim()
        );

        internalUser.setEmail(email);

        internalUser.setMobileNumber(mobileNumber);

        internalUser.setAlternateNumber(
                request.getAlternateNumber()
        );

        internalUser.setLocation(
                request.getLocation()
        );

        internalUser.setAddress(
                request.getAddress()
        );

        internalUser.setCountry(
                request.getCountry()
        );

        internalUser.setState(
                request.getState()
        );

        internalUser.setCity(
                request.getCity()
        );

        internalUser.setPincode(
                request.getPincode()
        );

        internalUser.setJoiningDate(
                request.getJoiningDate()
        );

        
        internalUser.setUpdatedBy(
                loggedInUser
        );

        internalUser.setUpdatedAt(
                LocalDateTime.now()
        );
        
        if (request.getIsActive() != null) {

            Boolean isActive = request.getIsActive();

            internalUser.setIsActive(isActive);

            systemUser.setIsActive(isActive);
        }

        // =====================================================
        // UPDATE SYSTEM USER
        // =====================================================

        systemUser.setFirstName(
                request.getFirstName().trim()
        );

        systemUser.setLastName(
                request.getLastName().trim()
        );

        systemUser.setEmail(email);

        systemUser.setMobileNumber(mobileNumber);

        systemUser.setAlternateNumber(
                request.getAlternateNumber()
        );

         
        systemUser.setUpdatedBy(
                loggedInUser
        );

        systemUser.setUpdatedAt(
                LocalDateTime.now()
        );


        systemUserRepository.save(systemUser);

        InternalUserInfo savedInternalUser =
                internalUserInfoRepository.save(
                        internalUser
                );


        return mapToResponse(
                savedInternalUser
        );
    }

    @Override
    @Transactional
    public void updateUserStatus(
            Long id,
            Boolean isActive) {

        if (isActive == null) {

            throw new IllegalArgumentException(
                    "Status cannot be null.");
        }


        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();

        
        InternalUserInfo internalUser =
                internalUserInfoRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Internal user not found with id : "
                                                + id
                                ));

 

        SystemUser systemUser =
                internalUser.getSystemUser();

        if (systemUser == null) {

            throw new ResourceNotFoundException(
                    "System user not found for internal user."
            );
        }

        // =====================================================
        // UPDATE INTERNAL USER STATUS
        // =====================================================

        internalUser.setIsActive(isActive);

        internalUser.setUpdatedBy(
                loggedInUser
        );

        internalUser.setUpdatedAt(
                LocalDateTime.now()
        );

        // =====================================================
        // UPDATE SYSTEM USER STATUS
        // =====================================================

        systemUser.setIsActive(isActive);

        systemUser.setUpdatedBy(
                loggedInUser
        );

        systemUser.setUpdatedAt(
                LocalDateTime.now()
        );

        // =====================================================
        // SAVE
        // =====================================================

        systemUserRepository.save(systemUser);

        internalUserInfoRepository.save(internalUser);
    }
    private InternalUserInfoResponse mapToResponse(InternalUserInfo user) {

        InternalUserInfoResponse response =new InternalUserInfoResponse();

        response.setId(user.getId());

        response.setFirstName(user.getFirstName());

        response.setLastName(user.getLastName());

        response.setEmail( user.getEmail());

        response.setMobileNumber(user.getMobileNumber());

        response.setAlternateNumber(user.getAlternateNumber());

        response.setLocation(user.getLocation());

        response.setAddress(user.getAddress());

        response.setCountry(user.getCountry());

        response.setState(user.getState());

        response.setCity(user.getCity());

        response.setPincode(user.getPincode());

        response.setJoiningDate(user.getJoiningDate());

        response.setIsActive(user.getIsActive());
        
        response.setSystemUserId(user.getSystemUser() != null? user.getSystemUser().getId(): null);

        response.setRoleName(user.getSystemUser() != null && user.getSystemUser().getRole() != null ? user.getSystemUser().getRole().getRoleName(): null);

        response.setCreatedBy(user.getCreatedBy() != null
                        ? user.getCreatedBy().getId()
                        : null);

        response.setUpdatedBy( user.getUpdatedBy() != null
                        ? user.getUpdatedBy().getId()
                        : null);

        response.setCreatedAt(user.getCreatedAt());

        response.setUpdatedAt(user.getUpdatedAt());

        return response;
    }


 
}