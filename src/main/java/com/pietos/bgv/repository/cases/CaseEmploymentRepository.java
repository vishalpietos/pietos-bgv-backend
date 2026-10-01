package com.pietos.bgv.repository.cases;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pietos.bgv.entity.cases.CaseEmploymentTable;
import com.pietos.bgv.enums.ComponentSubStatus;

@Repository
public interface CaseEmploymentRepository
        extends JpaRepository<CaseEmploymentTable, Long> {

    List<CaseEmploymentTable> findByCaseId(Long caseId);

    List<CaseEmploymentTable> findByCaseRef(String caseRef);

    List<CaseEmploymentTable>
    findByCasePackageComponentId(
            Long casePackageComponentId);

    Optional<CaseEmploymentTable>findTopByOrderByIdDesc();
    
    List<CaseEmploymentTable> findByAssignedToIsNull();
    
    List<CaseEmploymentTable> findByAssignedToIsNotNull();
    
    Page<CaseEmploymentTable> findByAssignedToIdAndProcessingStatusNotOrderBySubCaseIdDesc( Long assignedToId, ComponentSubStatus processingStatus, Pageable pageable);
    
    List<CaseEmploymentTable> findByCasePackageComponentIdInAndProcessingStatusOrderBySubCaseIdDesc(Collection<Long> casePackageComponentIds,ComponentSubStatus processingStatus);
}