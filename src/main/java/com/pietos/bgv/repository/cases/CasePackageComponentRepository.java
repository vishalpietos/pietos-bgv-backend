package com.pietos.bgv.repository.cases;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pietos.bgv.entity.cases.CasePackageComponent;

@Repository
public interface CasePackageComponentRepository
        extends JpaRepository<CasePackageComponent, Long> {

    List<CasePackageComponent> findByCasePackageId(
            Long casePackageId);

    void deleteByCasePackageId(
            Long casePackageId);
    
    
}