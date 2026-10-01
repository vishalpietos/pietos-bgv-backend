package com.pietos.bgv.service.cases;

import com.pietos.bgv.dto.request.cases.CaseVendorResponseRequest;
import com.pietos.bgv.dto.response.cases.CaseEducationVendorResponseDTO;
import com.pietos.bgv.dto.response.cases.CaseEmploymentVendorResponseDTO;

public interface CaseVendorResponseService {

    CaseEducationVendorResponseDTO saveEducationVendorResponse(
            Long caseEducationId,
            CaseVendorResponseRequest request);

    CaseEmploymentVendorResponseDTO saveEmploymentVendorResponse(
            Long caseEmploymentId,
            CaseVendorResponseRequest request);
    
    CaseEducationVendorResponseDTO getEducationVendorResponse(Long caseEducationId);
    
    CaseEmploymentVendorResponseDTO getEmploymentVendorResponse(Long caseEducationId);
}