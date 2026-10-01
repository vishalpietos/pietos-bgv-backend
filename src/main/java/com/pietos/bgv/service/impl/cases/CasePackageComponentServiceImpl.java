package com.pietos.bgv.service.impl.cases;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pietos.bgv.dto.response.cases.CasePackageComponentResponse;
import com.pietos.bgv.entity.ClientPackageComponent;
import com.pietos.bgv.entity.Component;
import com.pietos.bgv.entity.cases.CasePackage;
import com.pietos.bgv.entity.cases.CasePackageComponent;
import com.pietos.bgv.exception.ResourceNotFoundException;
import com.pietos.bgv.repository.ClientPackageComponentRepository;
import com.pietos.bgv.repository.ComponentRepository;
import com.pietos.bgv.repository.cases.CasePackageComponentRepository;
import com.pietos.bgv.repository.cases.CasePackageRepository;
import com.pietos.bgv.service.cases.CasePackageComponentService;

@Service
@Transactional
public class CasePackageComponentServiceImpl
        implements CasePackageComponentService {

    private final CasePackageComponentRepository
            casePackageComponentRepository;

    private final CasePackageRepository
            casePackageRepository;

    private final ComponentRepository componentRepository;
    
    private final ClientPackageComponentRepository
    clientPackageComponentRepository;


    public CasePackageComponentServiceImpl(
            CasePackageComponentRepository casePackageComponentRepository,
            CasePackageRepository casePackageRepository,
            ComponentRepository componentRepository,
            ClientPackageComponentRepository clientPackageComponentRepository) {

        this.casePackageComponentRepository =
                casePackageComponentRepository;

        this.casePackageRepository =
                casePackageRepository;

        this.componentRepository =
                componentRepository;

        this.clientPackageComponentRepository =
                clientPackageComponentRepository;
    }


    // =====================================================
    // GET COMPONENTS
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<CasePackageComponentResponse>
            getComponentsByCasePackageId(
                    Long casePackageId) {

        List<CasePackageComponent> components =
                casePackageComponentRepository
                        .findByCasePackageId(casePackageId);


        List<CasePackageComponentResponse> response =
                new ArrayList<>();


        for (CasePackageComponent item : components) {

            CasePackageComponentResponse dto =
                    new CasePackageComponentResponse();

            dto.setId(item.getId());

            dto.setComponentId(
                    item.getComponent().getId());

            dto.setComponentName(
                    item.getComponent().getComponentName());

            response.add(dto);
        }


        return response;
    }


    // =====================================================
    // SAVE COMPONENTS
    // =====================================================
    @Override
    public void saveComponents(
            Long casePackageId,
            List<Long> componentIds) {

        CasePackage casePackage =
                casePackageRepository
                        .findById(casePackageId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Case package not found with id : "
                                                + casePackageId));

        if (componentIds == null || componentIds.isEmpty()) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        for (Long componentId : componentIds) {

            Component component =
                    componentRepository
                            .findById(componentId)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Component not found with id : "
                                                    + componentId));

            CasePackageComponent casePackageComponent =
                    new CasePackageComponent();

            casePackageComponent.setCasePackage(casePackage);

            casePackageComponent.setComponent(component);

            casePackageComponent.setCreatedAt(now);

            casePackageComponent.setUpdatedAt(now);

            casePackageComponentRepository.save(
                    casePackageComponent);
        }
    }


    // =====================================================
    // DELETE COMPONENTS
    // =====================================================

    @Override
    public void deleteComponentsByCasePackageId(
            Long casePackageId) {

        casePackageComponentRepository
                .deleteByCasePackageId(
                        casePackageId);
    }


    @Override
    public void copyPackageComponentsToCasePackage(
            Long casePackageId,
            Long packageId) {

        CasePackage casePackage =
                casePackageRepository
                        .findById(casePackageId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Case package not found with id : "
                                                + casePackageId));

        List<ClientPackageComponent> packageComponents =
                clientPackageComponentRepository
                        .findByClientPackageIdAndIsActiveTrue(
                                packageId);

        LocalDateTime now =
                LocalDateTime.now();

        for (ClientPackageComponent packageComponent
                : packageComponents) {

            CasePackageComponent casePackageComponent =
                    new CasePackageComponent();

            casePackageComponent.setCasePackage(casePackage);

            casePackageComponent.setComponent(
                    packageComponent.getComponent());
            
            casePackageComponent.setClientPackageComponent(
                    packageComponent);

            casePackageComponent.setCreatedAt(now);

            casePackageComponent.setUpdatedAt(now);

            casePackageComponentRepository.save(
                    casePackageComponent);
        }
    }
   
 // =====================================================
 // CREATE FUTURE CHECK
 // =====================================================

    @Override
    public CasePackageComponentResponse createFutureCheck(
            Long caseId,
            Long componentId) {

        // ---------------------------------------------
        // FIND CASE PACKAGE
        // ---------------------------------------------

        CasePackage casePackage =
                casePackageRepository
                        .findByCaseEntityId(caseId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Case package not found for case id : "
                                                + caseId));

        // ---------------------------------------------
        // FIND CLIENT PACKAGE COMPONENT
        // ---------------------------------------------

        ClientPackageComponent clientPackageComponent =
                clientPackageComponentRepository
                        .findByClientPackageIdAndComponentIdAndIsActiveTrue(
                                casePackage.getClientPackage().getId(),
                                componentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Client package component not found for component id : "
                                                + componentId));

        // ---------------------------------------------
        // COMPONENT
        // ---------------------------------------------

        Component component =
                clientPackageComponent.getComponent();

        if (component == null) {
            throw new ResourceNotFoundException(
                    "Component not found with id : "
                            + componentId);
        }

        // ---------------------------------------------
        // CREATE NEW CASE PACKAGE COMPONENT
        // ---------------------------------------------

        LocalDateTime now =
                LocalDateTime.now();

        CasePackageComponent casePackageComponent =
                new CasePackageComponent();

        casePackageComponent.setCasePackage(
                casePackage);

        casePackageComponent.setComponent(
                component);

        // ---------------------------------------------
        // IMPORTANT:
        // LINK CLIENT PACKAGE COMPONENT
        // ---------------------------------------------

        casePackageComponent.setClientPackageComponent(
                clientPackageComponent);

        casePackageComponent.setCreatedAt(now);

        casePackageComponent.setUpdatedAt(now);

        // ---------------------------------------------
        // SAVE
        // ---------------------------------------------

        CasePackageComponent saved =
                casePackageComponentRepository.save(
                        casePackageComponent);

        // ---------------------------------------------
        // RESPONSE
        // ---------------------------------------------

        CasePackageComponentResponse response =
                new CasePackageComponentResponse();

        response.setId(
                saved.getId());

        response.setComponentId(
                saved.getComponent().getId());

        response.setComponentName(
                saved.getComponent().getComponentName());

        return response;
    } 
    
}