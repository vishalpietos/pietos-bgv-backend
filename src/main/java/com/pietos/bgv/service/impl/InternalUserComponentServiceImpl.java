package com.pietos.bgv.service.impl;

import com.pietos.bgv.dto.request.InternalUserComponentRequest;
import com.pietos.bgv.dto.response.InternalUserComponentResponse;
import com.pietos.bgv.dto.response.cases.AssignmentUserResponse;
import com.pietos.bgv.entity.Component;
import com.pietos.bgv.entity.InternalUserComponent;
import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.repository.ComponentRepository;
import com.pietos.bgv.repository.InternalUserComponentRepository;
import com.pietos.bgv.repository.SystemUserRepository;
import com.pietos.bgv.service.InternalUserComponentService;
import com.pietos.bgv.security.LoggedInUserService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InternalUserComponentServiceImpl
        implements InternalUserComponentService {

    private final InternalUserComponentRepository internalUserComponentRepository;
    private final SystemUserRepository systemUserRepository;
    private final ComponentRepository componentRepository;
    private final LoggedInUserService loggedInUserService;

    public InternalUserComponentServiceImpl(
            InternalUserComponentRepository internalUserComponentRepository,
            SystemUserRepository systemUserRepository,
            ComponentRepository componentRepository,
            LoggedInUserService loggedInUserService) {

        this.internalUserComponentRepository = internalUserComponentRepository;
        this.systemUserRepository = systemUserRepository;
        this.componentRepository = componentRepository;
        this.loggedInUserService = loggedInUserService;
    }

    @Transactional
    @Override
    public List<InternalUserComponentResponse> saveComponents(
            Long systemUserId,
            InternalUserComponentRequest request) {

        // =====================================================
        // FIND SYSTEM USER
        // =====================================================

        SystemUser systemUser = systemUserRepository
                .findById(systemUserId)
                .orElseThrow(() ->
                        new RuntimeException("Internal user not found"));


        // =====================================================
        // VALIDATE REQUEST
        // =====================================================

        if (request == null
                || request.getComponentIds() == null) {

            throw new RuntimeException(
                    "Component selection cannot be null");
        }


        // =====================================================
        // LOGGED-IN USER
        // =====================================================

        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();


        // =====================================================
        // GET EXISTING COMPONENT ASSIGNMENTS
        // =====================================================

        List<InternalUserComponent> existingComponents =
                internalUserComponentRepository
                        .findBySystemUserId(systemUserId);


        // =====================================================
        // CREATE LIST OF SELECTED COMPONENT IDS
        // =====================================================

        List<Long> selectedComponentIds =
                request.getComponentIds();


        // =====================================================
        // UPDATE EXISTING COMPONENTS
        // =====================================================

        for (InternalUserComponent existing :
                existingComponents) {

            Long existingComponentId =
                    existing.getComponent().getId();

            boolean selected =
                    selectedComponentIds.contains(
                            existingComponentId
                    );

       

            existing.setIsActive(selected);

            existing.setUpdatedBy(loggedInUser);

            internalUserComponentRepository.save(existing);
        }


       

        for (Long componentId : selectedComponentIds) {

            boolean alreadyExists =
                    existingComponents.stream()
                            .anyMatch(existing ->
                                    existing.getComponent()
                                            .getId()
                                            .equals(componentId)
                            );


            if (!alreadyExists) {

                Component component =
                        componentRepository
                                .findById(componentId)
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "Component not found with id: "
                                                        + componentId
                                ));


                InternalUserComponent userComponent =
                        new InternalUserComponent();

                userComponent.setSystemUser(systemUser);

                userComponent.setComponent(component);

                userComponent.setIsActive(true);

                // Audit fields
                userComponent.setCreatedBy(loggedInUser);
                userComponent.setUpdatedBy(loggedInUser);

                internalUserComponentRepository.save(
                        userComponent
                );
            }
        }


        // =====================================================
        // RETURN ACTIVE COMPONENTS
        // =====================================================

        return getComponents(systemUserId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<InternalUserComponentResponse> getComponents(
            Long systemUserId) {

        systemUserRepository
                .findById(systemUserId)
                .orElseThrow(() ->
                        new RuntimeException("Internal user not found"));

        List<InternalUserComponent> components =
                internalUserComponentRepository
                        .findBySystemUserIdAndIsActiveTrue(
                                systemUserId
                        );

        return components.stream()
                .map(this::mapToResponse)
                .toList();
    }


    private InternalUserComponentResponse mapToResponse(
            InternalUserComponent userComponent) {

        return new InternalUserComponentResponse(
                userComponent.getId(),
                userComponent.getSystemUser().getId(),
                userComponent.getComponent().getId(),
                userComponent.getComponent().getComponentName(),
                userComponent.getIsActive(),
                userComponent.getCreatedAt(),
                userComponent.getUpdatedAt()
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AssignmentUserResponse> getAssignmentUsers(
            String componentName) {

        if (componentName == null || componentName.isBlank()) {
            throw new IllegalArgumentException(
                    "Component name is required.");
        }

        List<InternalUserComponent> assignments =
                internalUserComponentRepository
                        .findByComponent_ComponentNameAndComponent_IsActiveTrueAndIsActiveTrueAndSystemUser_IsActiveTrue(componentName);

        return assignments.stream()
                .map(assignment -> {

                    SystemUser user =
                            assignment.getSystemUser();

                    return new AssignmentUserResponse(
                            user.getId(),
                            user.getFirstName(),
                            user.getLastName()
                    );
                })
                .collect(Collectors.toList());
    } 
}