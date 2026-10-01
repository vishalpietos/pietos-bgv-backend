package com.pietos.bgv.repository.cases;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pietos.bgv.entity.cases.CasePackage;

@Repository
public interface CasePackageRepository
        extends JpaRepository<CasePackage, Long> {

    Optional<CasePackage> findByCaseEntityId(Long caseId);

    boolean existsByCaseEntityId(Long caseId);
    
    
}