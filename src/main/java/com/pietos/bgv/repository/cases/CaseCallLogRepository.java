package com.pietos.bgv.repository.cases;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pietos.bgv.entity.cases.CaseCallLog;

@Repository
public interface CaseCallLogRepository
        extends JpaRepository<CaseCallLog, Long> {

    List<CaseCallLog> findByCaseIdOrderByCreatedAtDesc(Long caseId);
}