package com.pietos.bgv.service.impl.cases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pietos.bgv.entity.ClientComponentTat;
import com.pietos.bgv.entity.ClientPackage;
import com.pietos.bgv.entity.ClientPackageComponent;
import com.pietos.bgv.entity.Component;
import com.pietos.bgv.entity.cases.Case;
import com.pietos.bgv.entity.cases.CaseEducationTable;
import com.pietos.bgv.entity.cases.CasePackage;
import com.pietos.bgv.entity.cases.CasePackageComponent;
import com.pietos.bgv.enums.CaseStatus;
import com.pietos.bgv.enums.DataEntryStatus;
import com.pietos.bgv.enums.PackageType;
import com.pietos.bgv.exception.ResourceNotFoundException;
import com.pietos.bgv.repository.ClientComponentTatRepository;
import com.pietos.bgv.repository.cases.CaseEducationRepository;
import com.pietos.bgv.repository.cases.CasePackageComponentRepository;
import com.pietos.bgv.repository.cases.CasePackageRepository;
import com.pietos.bgv.repository.cases.CaseRepository;
import com.pietos.bgv.service.TatDateCalculationService;
import com.pietos.bgv.service.cases.CaseStatusService;

@Service
@Transactional
public class CaseStatusServiceImpl implements CaseStatusService {

    private final CaseRepository caseRepository;
    private final CaseEducationRepository caseEducationRepository;
    private final CasePackageRepository casePackageRepository;
    private final CasePackageComponentRepository casePackageComponentRepository;
    private final ClientComponentTatRepository clientComponentTatRepository;
    private final TatDateCalculationService tatDateCalculationService;

    public CaseStatusServiceImpl(
            CaseRepository caseRepository,
            CaseEducationRepository caseEducationRepository,
            CasePackageRepository casePackageRepository,
            CasePackageComponentRepository casePackageComponentRepository,
            ClientComponentTatRepository clientComponentTatRepository,
            TatDateCalculationService tatDateCalculationService) {

        this.caseRepository = caseRepository;
        this.caseEducationRepository = caseEducationRepository;
        this.casePackageRepository = casePackageRepository;
        this.casePackageComponentRepository =
                casePackageComponentRepository;
        this.clientComponentTatRepository =
                clientComponentTatRepository;
        this.tatDateCalculationService =
                tatDateCalculationService;
    }

    @Override
    @Transactional(readOnly = true)
    public LocalDate calculateCaseDueDate(Long caseId) {

        if (caseId == null) {
            throw new IllegalArgumentException(
                    "Case ID is required.");
        }

        // -------------------------------------------------
        // GET CASE
        // -------------------------------------------------

        Case caseEntity =
                caseRepository.findById(caseId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Case not found with id : "
                                                + caseId));

        if (caseEntity.getCaseReceivedDate() == null) {
            throw new IllegalArgumentException(
                    "Case received date is required.");
        }

        // -------------------------------------------------
        // GET CASE PACKAGE
        // -------------------------------------------------

        CasePackage casePackage =
                casePackageRepository
                        .findByCaseEntityId(caseId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Case package not found for case : "
                                                + caseId));

        // -------------------------------------------------
        // GET CLIENT PACKAGE
        // -------------------------------------------------

        ClientPackage clientPackage =
                casePackage.getClientPackage();

        if (clientPackage == null) {
            throw new ResourceNotFoundException(
                    "Client package not found for case package.");
        }

  

        List<CasePackageComponent> casePackageComponents =
                casePackageComponentRepository
                        .findByCasePackageId(
                                casePackage.getId());

        if (casePackageComponents == null
                || casePackageComponents.isEmpty()) {

            throw new ResourceNotFoundException(
                    "No case package components found for case : "
                            + caseId);
        }

        LocalDateTime receivedDateTime =
                caseEntity.getCaseReceivedDate()
                        .atStartOfDay();

     if (PackageType.PACKAGE.equals(
             clientPackage.getPackageType())) {

         Integer internalTat =
                 clientPackage.getInternalTat();

         if (internalTat == null
                 || internalTat < 0) {

             throw new IllegalArgumentException(
                     "Internal TAT is not configured for this package.");
         }

         return tatDateCalculationService.calculateEffectiveTo(
                 receivedDateTime,
                 internalTat,
                 clientPackage.getHolidayType());
     }

     if (PackageType.INDIVIDUAL.equals(
             clientPackage.getPackageType())) {

         int highestTat = 0;

         for (CasePackageComponent packageComponent
                 : casePackageComponents) {

             ClientPackageComponent clientPackageComponent =
                     packageComponent.getClientPackageComponent();

             if (clientPackageComponent == null) {
                 continue;
             }

             Integer componentTatDays =
                     clientPackageComponent.getTatDays();

             if (componentTatDays == null
                     || componentTatDays < 0) {
                 continue;
             }

             highestTat = Math.max(
                     highestTat,
                     componentTatDays);
         }

         if (highestTat == 0) {
             throw new IllegalArgumentException(
                     "No component TAT is configured for this package.");
         }

         return tatDateCalculationService.calculateEffectiveTo(
                 receivedDateTime,
                 highestTat,
                 clientPackage.getHolidayType());
     }

     throw new IllegalArgumentException(
             "Invalid package type: "
                     + clientPackage.getPackageType());
        }
    
	
	
    }