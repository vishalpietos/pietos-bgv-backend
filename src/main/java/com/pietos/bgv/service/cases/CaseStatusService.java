package com.pietos.bgv.service.cases;

import java.time.LocalDate;

import com.pietos.bgv.enums.CaseStatus;

public interface CaseStatusService {

    
    LocalDate calculateCaseDueDate(Long caseId);

}