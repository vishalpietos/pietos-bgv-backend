package com.pietos.bgv.service.cases;

import java.util.List;

import com.pietos.bgv.dto.response.cases.CasePackageComponentResponse;

public interface CasePackageComponentService {

    List<CasePackageComponentResponse>getComponentsByCasePackageId(Long casePackageId);

    void saveComponents(Long casePackageId,List<Long> componentIds);
    
    void copyPackageComponentsToCasePackage(Long casePackageId,Long packageId);

    void deleteComponentsByCasePackageId(Long casePackageId);
    
    CasePackageComponentResponse createFutureCheck(Long caseId,Long componentId);
}