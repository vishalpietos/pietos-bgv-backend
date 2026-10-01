package com.pietos.bgv.service.cases;

import java.util.List;

import com.pietos.bgv.dto.request.cases.CaseRequest;
import com.pietos.bgv.dto.response.cases.CaseResponse;
import com.pietos.bgv.enums.CaseStatus;

public interface CaseService {

    
    CaseResponse createCase(CaseRequest request);
    
    CaseResponse getCaseById(Long id);
 
    List<CaseResponse> getAllCases();

    CaseResponse updateCase(Long id,CaseRequest request);
    
    List<CaseResponse> getMyCases();
    
    CaseStatus evaluateDataEntryStatus(Long caseId);
}