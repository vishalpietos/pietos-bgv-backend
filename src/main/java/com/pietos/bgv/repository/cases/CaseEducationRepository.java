package com.pietos.bgv.repository.cases;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pietos.bgv.entity.cases.CaseEducationTable;
import com.pietos.bgv.enums.ComponentSubStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface CaseEducationRepository extends JpaRepository<CaseEducationTable, Long> {

    List<CaseEducationTable> findByCaseId(Long caseId);

    List<CaseEducationTable> findByCaseRef(String caseRef);

    Optional<CaseEducationTable>findTopByOrderByIdDesc();
    
    Optional<CaseEducationTable> findByCasePackageComponentId(
            Long casePackageComponentId);
    
    List<CaseEducationTable> findByAssignedToIsNull();
    
    List<CaseEducationTable> findByAssignedToIsNotNull();
    
    Optional<CaseEducationTable> findById(Long id);
    
    Page<CaseEducationTable>findByAssignedToIdAndProcessingStatusNotOrderBySubCaseIdDesc(Long assignedToId,ComponentSubStatus processingStatus, Pageable pageable);
    
    List<CaseEducationTable> findByCasePackageComponentIdInAndProcessingStatusOrderBySubCaseIdDesc(Collection<Long> casePackageComponentIds,ComponentSubStatus processingStatus);
   
}