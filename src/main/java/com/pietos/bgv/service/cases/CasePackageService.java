package com.pietos.bgv.service.cases;

import com.pietos.bgv.dto.request.cases.CasePackageRequest;
import com.pietos.bgv.dto.response.cases.CasePackageResponse;

public interface CasePackageService {

    CasePackageResponse createCasePackage(
            CasePackageRequest request);

    CasePackageResponse getCasePackageByCaseId(
            Long caseId);

    CasePackageResponse updateCasePackage(
            Long caseId,
            CasePackageRequest request);
}