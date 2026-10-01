package com.pietos.bgv.service.cases;

import java.util.List;

import com.pietos.bgv.dto.request.cases.CaseAssignmentRequest;
import com.pietos.bgv.dto.request.cases.CaseEducationRequest;
import com.pietos.bgv.dto.response.cases.CaseEducationMyCaseResponse;
import com.pietos.bgv.dto.response.cases.CaseEducationOpenCaseResponse;
import com.pietos.bgv.dto.response.cases.CaseEducationReassignResponse;
import com.pietos.bgv.dto.response.cases.CaseEducationResponse;
import com.pietos.bgv.enums.ComponentSubStatus;
import com.pietos.bgv.enums.DispositionStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CaseEducationService {

    CaseEducationResponse createEducation(CaseEducationRequest request);

    List<CaseEducationResponse> getEducationByCaseId(Long caseId);

    List<CaseEducationResponse> getEducationByCaseRef(String caseRef);

    List<CaseEducationResponse> getEducationByCasePackageComponentId(Long casePackageComponentId);

    CaseEducationResponse updateEducation(Long id, CaseEducationRequest request);
    
    CaseEducationResponse updateSelectedEducationFields(Long id,CaseEducationRequest request);
    
    List<CaseEducationOpenCaseResponse> getOpenEducationCases();
    
    void assignEducationCases(CaseAssignmentRequest request);
    
    List<CaseEducationReassignResponse> getAssignedEducationCases();
    
    void reassignEducationCases(CaseAssignmentRequest request);
    
    Page<CaseEducationMyCaseResponse> getMyEducationCases(Pageable pageable);
    
    String refuseEducationCase(Long id, String rejectionComment);
    
    CaseEducationResponse getEducationById(Long id);
    
    //when open the case check it assign WIP 
    void startProcessing(Long id);
    
    //update the Processed status
    void updateProcessingStatus(Long id,ComponentSubStatus status,DispositionStatus dispositionStatus,String activity);
    
}