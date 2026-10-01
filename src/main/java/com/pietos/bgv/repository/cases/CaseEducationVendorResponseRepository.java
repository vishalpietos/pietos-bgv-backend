package com.pietos.bgv.repository.cases;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pietos.bgv.entity.cases.CaseEducationVendorResponse;

@Repository
public interface CaseEducationVendorResponseRepository
        extends JpaRepository<CaseEducationVendorResponse, Long> {

    Optional<CaseEducationVendorResponse>
            findByCaseEducationId(Long caseEducationId);
}