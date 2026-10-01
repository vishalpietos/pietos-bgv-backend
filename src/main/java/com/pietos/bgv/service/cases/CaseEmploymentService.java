package com.pietos.bgv.service.cases;

import java.util.List;

import com.pietos.bgv.dto.request.cases.CaseAssignmentRequest;
import com.pietos.bgv.dto.request.cases.CaseEmploymentRequest;
import com.pietos.bgv.dto.response.cases.CaseEmploymentMyCaseResponse;
import com.pietos.bgv.dto.response.cases.CaseEmploymentOpenCaseResponse;
import com.pietos.bgv.dto.response.cases.CaseEmploymentReassignResponse;
import com.pietos.bgv.dto.response.cases.CaseEmploymentResponse;
import com.pietos.bgv.enums.ComponentSubStatus;
import com.pietos.bgv.enums.DispositionStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CaseEmploymentService {

    CaseEmploymentResponse createEmployment(CaseEmploymentRequest request);

    CaseEmploymentResponse updateEmployment( Long id, CaseEmploymentRequest request);
    
    CaseEmploymentResponse updateSelectedEmploymentFields( Long id,CaseEmploymentRequest request);

    CaseEmploymentResponse getEmploymentById(Long id);

    List<CaseEmploymentResponse> getEmploymentByCaseId(Long caseId);

    List<CaseEmploymentResponse> getEmploymentByCaseRef(String caseRef);

    List<CaseEmploymentResponse> getEmploymentByCasePackageComponentId(Long casePackageComponentId);
    
    void assignEmploymentCases(CaseAssignmentRequest request);
    
    List<CaseEmploymentOpenCaseResponse> getOpenEmploymentCases();
    
    List<CaseEmploymentReassignResponse> getAssignedEmploymentCases();
    
    void reassignEmploymentCases(CaseAssignmentRequest request);
    
    Page<CaseEmploymentMyCaseResponse> getMyEmploymentCases(Pageable pageable);

    String refuseEmploymentCase(Long id,String rejectionComment);
    
    void startProcessing(Long id);
    
    void updateProcessingStatus(Long id,ComponentSubStatus status,DispositionStatus dispositionStatus,String activity);
}