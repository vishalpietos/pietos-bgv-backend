package com.pietos.bgv.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pietos.bgv.dto.request.InternalUserRoleRequest;
import com.pietos.bgv.dto.response.InternalUserRoleResponse;
import com.pietos.bgv.entity.InternalUserRole;
import com.pietos.bgv.entity.Role;
import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.exception.ResourceNotFoundException;
import com.pietos.bgv.repository.InternalUserRoleRepository;
import com.pietos.bgv.repository.RoleRepository;
import com.pietos.bgv.repository.SystemUserRepository;
import com.pietos.bgv.security.LoggedInUserService;
import com.pietos.bgv.service.InternalUserRoleService;

@Service
public class InternalUserRoleServiceImpl implements InternalUserRoleService {

    private final InternalUserRoleRepository internalUserRoleRepository;
    private final SystemUserRepository systemUserRepository;
    private final RoleRepository roleRepository;
    private final LoggedInUserService loggedInUserService;

    public InternalUserRoleServiceImpl(
            InternalUserRoleRepository internalUserRoleRepository,
            SystemUserRepository systemUserRepository,
            RoleRepository roleRepository,
            LoggedInUserService loggedInUserService) {

        this.internalUserRoleRepository =
                internalUserRoleRepository;

        this.systemUserRepository =
                systemUserRepository;

        this.roleRepository =
                roleRepository;

        this.loggedInUserService =
                loggedInUserService;
    }

    @Transactional
    @Override
    public List<InternalUserRoleResponse> saveRoles(
            Long systemUserId,
            InternalUserRoleRequest request) {

        // -------------------------------------------------
        // VALIDATE SYSTEM USER
        // -------------------------------------------------

        SystemUser systemUser =
                systemUserRepository.findById(systemUserId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "System user not found with id: "
                                                + systemUserId
                                )
                        );


        // -------------------------------------------------
        // VALIDATE REQUEST
        // -------------------------------------------------

        if (request == null
                || request.getRoleIds() == null
                || request.getRoleIds().isEmpty()) {

            throw new IllegalArgumentException(
                    "At least one role must be selected."
            );
        }

        if (request.getPrimaryRoleId() == null) {

            throw new IllegalArgumentException(
                    "Primary role is required."
            );
        }


        if (!request.getRoleIds()
                .contains(request.getPrimaryRoleId())) {

            throw new IllegalArgumentException(
                    "Primary role must be one of the selected roles."
            );
        }


        // -------------------------------------------------
        // LOGGED-IN USER
        // -------------------------------------------------

        SystemUser loggedInUser = loggedInUserService.getLoggedInUser();


        // -------------------------------------------------
        // GET EXISTING ROLE ASSIGNMENTS
        // -------------------------------------------------

        List<InternalUserRole> existingRoles =
                internalUserRoleRepository
                        .findBySystemUserId(systemUserId);


        // -------------------------------------------------
        // UPDATE EXISTING ROLE ASSIGNMENTS
        // -------------------------------------------------

        for (InternalUserRole existingRole : existingRoles) {

            Long existingRoleId =
                    existingRole.getRole().getId();

            boolean selected =
                    request.getRoleIds()
                            .contains(existingRoleId);

            /*
             * Selected   -> ACTIVE
             * Unselected -> INACTIVE
             */

            existingRole.setIsActive(selected);

            existingRole.setUpdatedBy(loggedInUser);

            internalUserRoleRepository.save(existingRole);
        }


        // -------------------------------------------------
        // SET PRIMARY ROLE
        // -------------------------------------------------

        Role primaryRole =
                roleRepository
                        .findById(request.getPrimaryRoleId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Primary role not found with id: "
                                                + request.getPrimaryRoleId()
                                )
                        );

        systemUser.setRole(primaryRole);

        systemUser.setUpdatedBy(loggedInUser);

        systemUser.setUpdatedAt(LocalDateTime.now());

        systemUserRepository.save(systemUser);


        // -------------------------------------------------
        // ADD NEW ROLE ASSIGNMENTS
        // -------------------------------------------------

        List<InternalUserRoleResponse> responses =
                new ArrayList<>();


        for (Long roleId : request.getRoleIds()) {

            // Check whether this role was already assigned
            boolean alreadyExists =
                    existingRoles.stream()
                            .anyMatch(existingRole ->
                                    existingRole.getRole()
                                            .getId()
                                            .equals(roleId)
                            );


            // ---------------------------------------------
            // ONLY INSERT IF ROLE NEVER EXISTED
            // ---------------------------------------------

            if (!alreadyExists) {

                Role role =
                        roleRepository
                                .findById(roleId)
                                .orElseThrow(() ->
                                        new ResourceNotFoundException(
                                                "Role not found with id: "
                                                        + roleId
                                        )
                                );


                InternalUserRole internalUserRole =
                        new InternalUserRole();

                internalUserRole.setSystemUser(systemUser);

                internalUserRole.setRole(role);

                internalUserRole.setIsActive(true);


                // -----------------------------------------
                // AUDIT FIELDS
                // -----------------------------------------

                internalUserRole.setCreatedBy(
                        loggedInUser
                );

                internalUserRole.setUpdatedBy(
                        loggedInUser
                );


                InternalUserRole saved =
                        internalUserRoleRepository.save(
                                internalUserRole
                        );


                responses.add(
                        mapToResponse(saved)
                );
            }
        }


        // -------------------------------------------------
        // RETURN ACTIVE ROLES
        // -------------------------------------------------

        return getRoles(systemUserId);
    }

    // =====================================================
    // GET USER ROLES
    // =====================================================

    @Transactional(readOnly = true)
    @Override
    public List<InternalUserRoleResponse> getRoles(
            Long systemUserId) {

        // -------------------------------------------------
        // VALIDATE SYSTEM USER
        // -------------------------------------------------

        if (!systemUserRepository.existsById(systemUserId)) {

            throw new ResourceNotFoundException(
                    "System user not found with id: "
                            + systemUserId
            );
        }


        // -------------------------------------------------
        // GET ACTIVE ROLE ASSIGNMENTS
        // -------------------------------------------------

        List<InternalUserRole> userRoles =
                internalUserRoleRepository
                        .findBySystemUserIdAndIsActiveTrue(
                                systemUserId
                        );


        // -------------------------------------------------
        // MAP RESPONSE
        // -------------------------------------------------

        return userRoles.stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // MAPPER
    // =====================================================

    private InternalUserRoleResponse mapToResponse(
            InternalUserRole userRole) {

        InternalUserRoleResponse response =
                new InternalUserRoleResponse();

        response.setId(userRole.getId());

        response.setSystemUserId(
                userRole.getSystemUser().getId()
        );

        response.setRoleId(
                userRole.getRole().getId()
        );

        response.setRoleName(
                userRole.getRole().getRoleName()
        );

        response.setIsActive(
                userRole.getIsActive()
        );

        response.setCreatedAt(
                userRole.getCreatedAt()
        );

        response.setUpdatedAt(
                userRole.getUpdatedAt()
        );

        return response;
    }
}