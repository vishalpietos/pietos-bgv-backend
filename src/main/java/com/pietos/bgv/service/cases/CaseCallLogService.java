package com.pietos.bgv.service.cases;

import java.util.List;

import com.pietos.bgv.dto.request.cases.CaseCallLogRequest;
import com.pietos.bgv.dto.response.cases.CaseCallLogResponse;

public interface CaseCallLogService {

    CaseCallLogResponse createCallLog(Long caseId, CaseCallLogRequest request);

    List<CaseCallLogResponse> getCallLogsByCaseId(Long caseId);
}