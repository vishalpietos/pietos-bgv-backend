package com.pietos.bgv.service.cases;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.pietos.bgv.dto.response.cases.CaseDocumentResponse;

public interface CaseDocumentService {

    CaseDocumentResponse uploadDocument(
            Long caseId,
            Long casePackageComponentsId,
            String description,
            MultipartFile file);

    List<CaseDocumentResponse> getDocumentsByCaseId(
            Long caseId);

    CaseDocumentResponse getDocumentById(
            Long id);

    List<CaseDocumentResponse> getDocumentsByCaseRef(
            String caseRef);

    List<CaseDocumentResponse> getDocumentsByCasePackageComponentsId(
            Long casePackageComponentsId);

    void deactivateDocument(
            Long id);
}