package com.pietos.bgv.service.impl.cases;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pietos.bgv.dto.response.cases.CaseDocumentResponse;
import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.entity.cases.Case;
import com.pietos.bgv.entity.cases.CaseDocument;
import com.pietos.bgv.exception.ResourceNotFoundException;
import com.pietos.bgv.repository.cases.CaseDocumentRepository;
import com.pietos.bgv.repository.cases.CaseRepository;
import com.pietos.bgv.security.LoggedInUserService;
import com.pietos.bgv.service.FileStorageService;
import com.pietos.bgv.service.cases.CaseDocumentService;

@Service
public class CaseDocumentServiceImpl
        implements CaseDocumentService {

    private final CaseDocumentRepository caseDocumentRepository;

    private final CaseRepository caseRepository;

    private final LoggedInUserService loggedInUserService;

    private final FileStorageService fileStorageService;

    public CaseDocumentServiceImpl(
            CaseDocumentRepository caseDocumentRepository,
            CaseRepository caseRepository,
            LoggedInUserService loggedInUserService,
            FileStorageService fileStorageService) {

        this.caseDocumentRepository = caseDocumentRepository;
        this.caseRepository = caseRepository;
        this.loggedInUserService = loggedInUserService;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public CaseDocumentResponse uploadDocument(
            Long caseId,
            Long casePackageComponentsId,
            String description,
            MultipartFile file) {

        /*
         * Validate case
         */
        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Case not found with id : "
                                        + caseId));

        /*
         * Validate file
         */
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "File is required.");
        }

        /*
         * Validate description
         */
        if (description == null
                || description.isBlank()) {

            throw new IllegalArgumentException(
                    "Document description is required.");
        }

        /*
         * Logged-in user
         */
        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();

        /*
         * Store file
         *
         * Example:
         * case-documents/25/101
         */
        String folderName =
                "case-documents/"
                        + caseId
                        + "/"
                        + casePackageComponentsId;

        String filePath =
                fileStorageService.uploadFile(
                        file,
                        folderName);

        String fileName =
                Paths.get(filePath)
                        .getFileName()
                        .toString();

        /*
         * Create document
         */
        CaseDocument document =
                new CaseDocument();

        document.setCaseId(
                caseEntity.getId());

        /*
         * Case reference comes from backend
         */
        document.setCaseRef(caseEntity.getCaseRef());

        document.setCasePackageComponentsId(casePackageComponentsId);

        document.setFileName(fileName);

        document.setDescription(description);

        document.setFilePath(filePath);

        /*
         * Since uploadedBy is Long
         */
        document.setUploadedBy(loggedInUser.getId());

        document.setUploadedAt(LocalDateTime.now());

        document.setIsActive(true);

        CaseDocument savedDocument =caseDocumentRepository.save(document);

        return mapToResponse(savedDocument);
    }

    @Override
    public List<CaseDocumentResponse>
            getDocumentsByCaseId(
                    Long caseId) {

        if (!caseRepository.existsById(caseId)) {

            throw new ResourceNotFoundException(
                    "Case not found with id : "
                            + caseId);
        }

        return caseDocumentRepository
                .findByCaseIdAndIsActiveTrue(
                        caseId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CaseDocumentResponse>
            getDocumentsByCaseRef(
                    String caseRef) {

        return caseDocumentRepository
                .findByCaseRefAndIsActiveTrue(
                        caseRef)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CaseDocumentResponse>
            getDocumentsByCasePackageComponentsId(
                    Long casePackageComponentsId) {

        return caseDocumentRepository
                .findByCasePackageComponentsIdAndIsActiveTrue(
                        casePackageComponentsId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CaseDocumentResponse getDocumentById(
            Long id) {

        CaseDocument document =
                caseDocumentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Document not found with id : "
                                                + id));

        return mapToResponse(document);
    }

    @Override
    public void deactivateDocument(
            Long id) {

        CaseDocument document =
                caseDocumentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Document not found with id : "
                                                + id));

        SystemUser loggedInUser =
                loggedInUserService.getLoggedInUser();

        document.setIsActive(false);

     
        document.setDeactivatedBy(
                loggedInUser.getId());

        document.setDeactivatedAt(
                LocalDateTime.now());

        caseDocumentRepository.save(
                document);
    }

    private CaseDocumentResponse mapToResponse(
            CaseDocument document) {

        CaseDocumentResponse response =
                new CaseDocumentResponse();

        response.setId(
                document.getId());

        response.setCaseId(
                document.getCaseId());

        response.setCaseRef(
                document.getCaseRef());

        response.setCasePackageComponentsId(
                document.getCasePackageComponentsId());

        response.setFileName(
                document.getFileName());

        response.setDescription(
                document.getDescription());
       
        response.setUploadedByUserId(
                document.getUploadedBy());

        response.setUploadedAt(
                document.getUploadedAt());

        return response;
    }
}