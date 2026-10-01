package com.pietos.bgv.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pietos.bgv.dto.request.ManageUser.ManageUserCreateRequest;
import com.pietos.bgv.dto.request.ManageUser.ManageUserPatchRequest;
import com.pietos.bgv.dto.request.ManageUser.ManageUserSearchRequest;
import com.pietos.bgv.dto.request.ManageUser.ManageUserUpdateRequest;
import com.pietos.bgv.dto.response.ManageUserResponse;
import com.pietos.bgv.dto.response.client.ClientLocationDataResponse;
import com.pietos.bgv.email.EmailSubject;
import com.pietos.bgv.email.EmailTemplateService;
import com.pietos.bgv.entity.ClientInformation;
import com.pietos.bgv.entity.ClientLocation;
import com.pietos.bgv.entity.ManageUser;
import com.pietos.bgv.entity.Role;
import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.enums.ClientLocationStatus;
import com.pietos.bgv.exception.DuplicateResourceException;
import com.pietos.bgv.exception.ResourceNotFoundException;
import com.pietos.bgv.repository.ClientInformationRepository;
import com.pietos.bgv.repository.ClientLocationRepository;
import com.pietos.bgv.repository.ManageUserRepository;
import com.pietos.bgv.repository.RoleRepository;
import com.pietos.bgv.repository.SystemUserRepository;
import com.pietos.bgv.security.LoggedInUserService;
import com.pietos.bgv.service.EmailService;
import com.pietos.bgv.service.ManageUserService;
import com.pietos.bgv.specification.ManageUserSpecification;
import com.pietos.bgv.util.PasswordGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@Transactional
@Service
public class ManageUserServiceImpl implements ManageUserService {
	
	private final ManageUserRepository manageUserRepository;
	private final ClientInformationRepository clientRepository;
	private final ClientLocationRepository locationRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;
	private final SystemUserRepository systemUserRepository;
	private final EmailTemplateService emailTemplateService;
	private final LoggedInUserService loggedInUserService;
	
	
	public ManageUserServiceImpl(
	        ManageUserRepository manageUserRepository,
	        ClientInformationRepository clientRepository,
	        ClientLocationRepository locationRepository,
	        RoleRepository roleRepository,
	        PasswordEncoder passwordEncoder,
	        EmailService emailService,
	        SystemUserRepository systemUserRepository,
	        LoggedInUserService loggedInUserService,
	        EmailTemplateService emailTemplateService) {

	    this.manageUserRepository = manageUserRepository;
	    this.clientRepository = clientRepository;
	    this.locationRepository = locationRepository;
	    this.roleRepository = roleRepository;
	    this.passwordEncoder = passwordEncoder;
	    this.emailService = emailService;
	    this.systemUserRepository = systemUserRepository;
	    this.emailTemplateService= emailTemplateService;
	    this.loggedInUserService = loggedInUserService;
	}
	
	@Transactional
	@Override
	public ManageUserResponse createUser(ManageUserCreateRequest request) {
		ClientInformation client;

		SystemUser loggedInUser =
		        loggedInUserService.getLoggedInUser();

		String loggedInRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
		                .stream().map(GrantedAuthority::getAuthority).findFirst().orElse(null);

		if ("SUPER_ADMIN".equals(loggedInRole)) 
		{
		    client = clientRepository.findById(request.getClientId()).orElseThrow(() ->
		                    new ResourceNotFoundException("Client not found with id : "+ request.getClientId()));
		} else if ("CLIENT_ADMIN".equals(loggedInRole)) 
		 {
		    client = clientRepository.findBySystemUser(loggedInUser).orElseThrow(() ->
		                    new ResourceNotFoundException("Client not found for logged-in user."));
		  }else {
			  		throw new IllegalArgumentException(
		            "You are not authorized to create Manage Users.");
			  	}

		
		ClientLocation location =locationRepository.findById(request.getLocationId()).orElseThrow(() 
									->new ResourceNotFoundException("Location not found with id : "+ request.getLocationId()));


		// =====================================================
		// Make sure Location belongs to selected Client
		// =====================================================

		if (!location.getClientInformation().getId().equals(client.getId())) {
		    throw new IllegalArgumentException(
		            "Selected location does not belong to the selected client.");
		}


		// =====================================================
		// Check Role
		// Only CLIENT_ADMIN and HR_USER are allowed
		// =====================================================

		Role role = roleRepository.findById(request.getRoleId()).orElseThrow(() ->
		                new ResourceNotFoundException("Role not found with id : "+ request.getRoleId()));

		String roleName = role.getRoleName();

		if (!"CLIENT_ADMIN".equals(roleName)&& !"HR_USER".equals(roleName)) {
		    throw new IllegalArgumentException("Only CLIENT_ADMIN and HR_USER roles can be assigned.");
		}
		// =====================================================
		// Check Duplicate Email in SystemUser
		// =====================================================
		if (systemUserRepository.findByEmail(request.getEmail()).isPresent()) {
		    throw new DuplicateResourceException("Email already exists.");
		}
		// =====================================================
		// Check Duplicate Email in ManageUser
		// =====================================================
		if (manageUserRepository.existsByEmail(request.getEmail())) {
		    throw new DuplicateResourceException("Email already exists.");
		}
		// =====================================================
		// Check Duplicate Mobile in ManageUser
		// =====================================================
		if (manageUserRepository.existsByMobile(request.getMobile())) {
		    throw new DuplicateResourceException("Mobile number already exists.");
		}
		if (systemUserRepository.existsByMobileNumber(request.getMobile())) {
		    throw new DuplicateResourceException("Mobile number already exists.");
		}
		String generatedPassword =PasswordGenerator.generatePassword(PasswordGenerator.DEFAULT_PASSWORD_LENGTH);
		SystemUser systemUser = new SystemUser();
		systemUser.setFirstName(request.getFirstName());
		systemUser.setLastName(request.getLastName());
		systemUser.setEmail(request.getEmail());
		systemUser.setMobileNumber(request.getMobile());
		systemUser.setPassword(passwordEncoder.encode(generatedPassword));
		systemUser.setRole(role);
		systemUser.setIsActive(true);
		systemUser.setCreatedBy(loggedInUser);
		systemUser.setUpdatedBy(loggedInUser);
		systemUser.setCreatedAt(LocalDateTime.now());
		systemUser.setUpdatedAt(LocalDateTime.now());
		SystemUser savedSystemUser =systemUserRepository.save(systemUser);


		ManageUser user = new ManageUser();
		user.setSystemUser(savedSystemUser);
		user.setClientInformation(client);
		user.setClientLocation(location);
		user.setRole(role);
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());
		user.setMobile(request.getMobile());
		user.setState(request.getState());
		user.setCity(request.getCity());
		user.setIsActive(true);
		user.setCreatedBy(loggedInUser);
		user.setUpdatedBy(loggedInUser);
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());
		ManageUser savedUser =manageUserRepository.save(user);
		// =====================================================
		// Send Welcome Email
		// =====================================================
		try { 
			String html =emailTemplateService.manageUserWelcomeTemplate(request.getFirstName()+ " "+ request.getLastName(),request.getEmail(),generatedPassword,roleName);
		    String emailSubject;
		    if ("CLIENT_ADMIN".equals(roleName)) {
		        emailSubject =EmailSubject.MANAGE_CLIENT_ADMIN_CREATED;
		    } else if ("HR_USER".equals(roleName)) {
		        emailSubject =EmailSubject.MANAGE_HR_USER_CREATED;
		    } else {
		    	throw new IllegalArgumentException("Invalid Manage User role.");
		    }
		    emailService.sendEmail(request.getEmail(),emailSubject,html);
		    String superAdminEmail = "Vishalpanchaloffical@gmail.com";
////	            systemUserRepository
////		                    .findSuperAdminEmail()
////		                    .orElseThrow(() ->
////		                            new ResourceNotFoundException(
////		                                    "Super Admin email not found."
////		                            ))

		    // =====================================================
		    // Send Confirmation Email to Super Admin
		    // =====================================================

		    String confirmationHtml =
		            emailTemplateService
		                    .manageUserCreatedConfirmationTemplate(
		                            request.getFirstName()
		                                    + " "
		                                    + request.getLastName(),
		                            request.getEmail(),
		                            roleName,
		                            client.getClientName(),
		                            location.getLocationName()
		                    );

		    emailService.sendEmail(
		            superAdminEmail,
		            EmailSubject.MANAGE_USER_CREATED_CONFIRMATION,
		            confirmationHtml
		    );

		} catch (Exception e) {

		    throw new RuntimeException(
		            "Manage User account creation failed because welcome email could not be sent.",
		            e
		    );
		}

		return mapToResponse(savedUser);
	}
	
	
	@Override
	public List<ManageUserResponse> getAllUsers() {

	    return manageUserRepository.findAll()
	            .stream()
	            .map(this::mapToResponse)
	            .toList();
	}

	@Override
	public ManageUserResponse getUserById(Long id) {

	    ManageUser user = manageUserRepository.findById(id)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "User not found with id : " + id));

	    return mapToResponse(user);
	}

	@Override
	public ManageUserResponse updateUser(Long id, ManageUserUpdateRequest request) {
	    // =====================================================
	    // Check User Exists
	    // =====================================================
	    ManageUser user = manageUserRepository.findById(id).orElseThrow(() ->
	                    new ResourceNotFoundException("User not found with id : " + id));
	    // =====================================================
	    // Get Client
	    // =====================================================
	    ClientInformation client = user.getClientInformation();

	    if (client == null) {
	        throw new ResourceNotFoundException("Client not found for this Manage User.");
	    }

	    ClientLocation location =locationRepository.findById(request.getLocationId()).orElseThrow(() ->
	    							new ResourceNotFoundException("Location not found with id : "+ request.getLocationId()));

	    Role role = roleRepository.findById(request.getRoleId()).orElseThrow(() ->
	                 new ResourceNotFoundException("Role not found with id : "+ request.getRoleId()));

	    String roleName = role.getRoleName();

	    if (!"CLIENT_ADMIN".equals(roleName)&& !"HR_USER".equals(roleName)) {
	     throw new IllegalArgumentException("Only CLIENT_ADMIN and HR_USER roles can be assigned.");
	 }
	    	SystemUser systemUser = user.getSystemUser();
	    	
	    	if (user.getMobile() != null && !user.getMobile().equals(request.getMobile())
	    			&& manageUserRepository.existsByMobile(request.getMobile())) 
	    		{
	    		throw new DuplicateResourceException("Mobile number already exists.");
	    		}
	    	// =====================================================
	    	// Check Duplicate Mobile in SystemUser
	    	// =====================================================
	    	if (!systemUser.getMobileNumber().equals(request.getMobile())
	    			&& systemUserRepository.existsByMobileNumberAndIdNot(request.getMobile(),systemUser.getId()))
	    		{
	    		throw new DuplicateResourceException("Mobile number already exists in System Users.");
	    		}

	    SystemUser loggedInUser =loggedInUserService.getLoggedInUser();
	    systemUser.setFirstName(request.getFirstName());
	    systemUser.setLastName(request.getLastName());
	    systemUser.setMobileNumber(request.getMobile());
	    systemUser.setRole(role);
	    systemUser.setUpdatedBy(loggedInUser);
	    systemUser.setUpdatedAt(LocalDateTime.now());
	    systemUserRepository.save(systemUser);
	    user.setClientLocation(location);
	    user.setFirstName(request.getFirstName());
	    user.setLastName(request.getLastName());
	    user.setMobile(request.getMobile());
	    // New fields
	    user.setState(location.getState());
	    user.setCity(location.getCity());
	    // Status
	    if (request.getIsActive() != null) {
	        user.setIsActive(request.getIsActive());
	    }
	    // Audit
	    user.setUpdatedBy(loggedInUser);
	    user.setUpdatedAt(LocalDateTime.now());
	    ManageUser updatedUser =manageUserRepository.save(user);
	    return mapToResponse(updatedUser);
	}

	@Override
	public ManageUserResponse patchUser(Long id, ManageUserPatchRequest request) {
	    // Check User Exists
	    ManageUser user = manageUserRepository.findById(id).orElseThrow(() ->
	                    new ResourceNotFoundException( "User not found with id : " + id));
	    // Check Location Exists
	    if (request.getLocationId() != null) {
	        ClientLocation location = locationRepository.findById(request.getLocationId()).orElseThrow(() ->
	                        new ResourceNotFoundException( "Location not found with id : " + request.getLocationId()));
	        user.setClientLocation(location);
	    }

	    // Check Duplicate Email
	    if (request.getEmail() != null&& !request.getEmail().equalsIgnoreCase(user.getSystemUser().getEmail())
	            && systemUserRepository.existsByEmail(request.getEmail())) {
	        throw new DuplicateResourceException("Email already exists.");
	    }
	    // Check Duplicate Mobile
	    if (request.getMobile() != null && !request.getMobile().equals(user.getMobile()) 
	    		&& manageUserRepository.existsByMobile(request.getMobile())) {
	        throw new DuplicateResourceException("Mobile number already exists.");
	    }
	    // Update Fields
	    if (request.getFirstName() != null)
	    	user.setFirstName(request.getFirstName());
	    if (request.getLastName() != null)
	        user.setLastName(request.getLastName());
	    if (request.getEmail() != null) 
	    	{
	        SystemUser systemUser = user.getSystemUser();
	        systemUser.setEmail(request.getEmail());
	        systemUserRepository.save(systemUser);
	    }
	    if (request.getMobile() != null)
	        user.setMobile(request.getMobile());
	    if (request.getIsActive() != null)
	        user.setIsActive(request.getIsActive());
	    // TODO:
	    // user.setUpdatedBy(loggedInUserId);
	    user.setUpdatedAt(LocalDateTime.now());
	    ManageUser updatedUser = manageUserRepository.save(user);
	    return mapToResponse(updatedUser);
	}

	@Override
	public void activateUser(Long id) {

	    ManageUser user = manageUserRepository.findById(id)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "User not found with id : " + id));

	    user.setIsActive(true);

	    // TODO:
	    // user.setUpdatedBy(loggedInUserId);

	    user.setUpdatedAt(LocalDateTime.now());

	    manageUserRepository.save(user);
	}

	@Override
	public void deactivateUser(Long id) {

	    ManageUser user = manageUserRepository.findById(id)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "User not found with id : " + id));

	    user.setIsActive(false);

	    // TODO:
	    // user.setUpdatedBy(loggedInUserId);

	    user.setUpdatedAt(LocalDateTime.now());

	    manageUserRepository.save(user);
	}

	@Override
	public List<ManageUserResponse> searchUsers(
	        ManageUserSearchRequest request) {

	    Specification<ManageUser> specification =
	            ManageUserSpecification.search(request);

	    return manageUserRepository.findAll(specification)
	            .stream()
	            .map(this::mapToResponse)
	            .toList();
	}



	@Override
	public List<ManageUserResponse> getUsersByClientId(Long clientId) {

	    // Check Client Exists
	    clientRepository.findById(clientId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Client not found with id : " + clientId));
	    
	    List<ManageUser> users = manageUserRepository.findByClientInformationId(clientId);
	    
	    if (users.isEmpty()) {
	        throw new ResourceNotFoundException(
	        		"No manage users have been created for this location yet.");
	    }
	    
	    return manageUserRepository.findByClientInformationId(clientId)
	            .stream()
	            .map(this::mapToResponse)
	            .toList();
	}
	
	
	@Override
	public List<ManageUserResponse> getUsersByLocationId(Long locationId) {

	    // Check Location Exists
	    locationRepository.findById(locationId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Location not found with id : " + locationId));
	    
	    List<ManageUser> users =manageUserRepository.findByClientLocationId(locationId);
	    
	    if (users.isEmpty()) {
	        throw new ResourceNotFoundException(
	        		"No manage users have been created for this location yet.");
	    }
	    

	    return manageUserRepository.findByClientLocationId(locationId)
	            .stream()
	            .map(this::mapToResponse)
	            .toList();
	}
	
	@Override
	@Transactional(readOnly = true)
	public ClientLocationDataResponse getClientLocationData(Long clientId) {

	    // =====================================================
	    // GET CLIENT
	    // =====================================================

	    ClientInformation client =
	            clientRepository.findById(clientId)
	                    .orElseThrow(() ->
	                            new ResourceNotFoundException(
	                                    "Client not found with id : "
	                                            + clientId));

	    // =====================================================
	    // GET ACTIVE LOCATIONS
	    // =====================================================

	    List<ClientLocation> locations =
	            locationRepository
	                    .findByClientInformationIdAndStatus(
	                            clientId,
	                            ClientLocationStatus.ACTIVE);

	    // =====================================================
	    // CREATE RESPONSE
	    // =====================================================

	    ClientLocationDataResponse response =
	            new ClientLocationDataResponse();

	    response.setClientId(
	            client.getId());

	    response.setClientName(
	            client.getClientName());

	    // =====================================================
	    // LOCATION DATA
	    // =====================================================

	    List<ClientLocationDataResponse.LocationData> locationData =
	            locations.stream()
	                    .map(location -> {

	                        ClientLocationDataResponse.LocationData data =
	                                new ClientLocationDataResponse.LocationData();

	                        data.setId(
	                                location.getId());

	                        data.setLocationName(
	                                location.getLocationName());

	                        data.setState(
	                                location.getState());

	                        data.setCity(
	                                location.getCity());

	                        return data;
	                    })
	                    .toList();

	    response.setLocations(
	            locationData);

	    // =====================================================
	    // ROLE DATA
	    // =====================================================

	    List<Role> roles =
	            roleRepository.findAll();

	    List<ClientLocationDataResponse.RoleData> roleData =
	            roles.stream()
	                    .filter(role ->
	                            "CLIENT_ADMIN".equals(
	                                    role.getRoleName())
	                                    ||
	                            "HR_USER".equals(
	                                    role.getRoleName()))
	                    .map(role -> {

	                        ClientLocationDataResponse.RoleData data =
	                                new ClientLocationDataResponse.RoleData();

	                        data.setId(
	                                role.getId());

	                        data.setRoleName(
	                                role.getRoleName());

	                        return data;
	                    })
	                    .toList();

	    response.setRoles(
	            roleData);

	    return response;
	}
	
	
	private ManageUserResponse mapToResponse(ManageUser user) {

	    ManageUserResponse response =
	            new ManageUserResponse();

	    response.setId(user.getId());

	    // =====================================================
	    // CLIENT DETAILS
	    // =====================================================
	    response.setClientId(user.getClientInformation().getId());
	    response.setClientName(user.getClientInformation().getClientName());

	    // =====================================================
	    // LOCATION DETAILS
	    // =====================================================

	    response.setLocationId(
	            user.getClientLocation()
	                    .getId());

	    response.setLocationName(
	            user.getClientLocation()
	                    .getLocationName());

	    response.setState(
	            user.getClientLocation()
	                    .getState());

	    response.setCity(
	            user.getClientLocation()
	                    .getCity());

	    // =====================================================
	    // ROLE DETAILS
	    // =====================================================

	    response.setRoleId(
	            user.getSystemUser()
	                    .getRole()
	                    .getId());

	    response.setRoleName(
	            user.getSystemUser()
	                    .getRole()
	                    .getRoleName());

	    // =====================================================
	    // USER DETAILS
	    // =====================================================

	    response.setFirstName(
	            user.getFirstName());

	    response.setLastName(
	            user.getLastName());

	    response.setEmail(
	            user.getSystemUser()
	                    .getEmail());

	    response.setMobile(
	            user.getMobile());

	    response.setIsActive(
	            user.getIsActive());

	    // =====================================================
	    // AUDIT DETAILS
	    // =====================================================

	    response.setCreatedAt(
	            user.getCreatedAt());

	    response.setUpdatedAt(
	            user.getUpdatedAt());

	    return response;
	}

	
}
