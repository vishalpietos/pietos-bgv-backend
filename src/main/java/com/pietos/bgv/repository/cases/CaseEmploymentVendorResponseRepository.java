package com.pietos.bgv.repository.cases;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pietos.bgv.entity.cases.CaseEmploymentVendorResponse;

@Repository
public interface CaseEmploymentVendorResponseRepository
        extends JpaRepository<CaseEmploymentVendorResponse, Long> {

    Optional<CaseEmploymentVendorResponse>
            findByCaseEmploymentId(Long caseEmploymentId);
}