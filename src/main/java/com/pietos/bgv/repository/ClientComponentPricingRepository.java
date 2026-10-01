package com.pietos.bgv.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pietos.bgv.entity.ClientComponentPricing;

@Repository
public interface ClientComponentPricingRepository
        extends JpaRepository<ClientComponentPricing, Long> {

		
	
	Optional<ClientComponentPricing>
	findByClientPackageComponentIdAndIsActiveTrue(
	        Long clientPackageComponentId);
    // =========================================
    // FIND ALL ACTIVE PRICING OF PACKAGE
    // =========================================

    List<ClientComponentPricing>
    findByClientPackageIdAndIsActiveTrue(
            Long packageId);


    // =========================================
    // FIND ACTIVE PRICING OF PACKAGE
    // BY EFFECTIVE DATES
    // =========================================

    List<ClientComponentPricing>
    findByClientPackageIdAndEffectiveFromAndEffectiveToAndIsActiveTrue(
            Long packageId,
            LocalDate effectiveFrom,
            LocalDate effectiveTo);


    // =========================================
    // FIND EXISTING PRICING
    // PACKAGE + COMPONENT + DATES
    // =========================================

    @Query("""
        SELECT p
        FROM ClientComponentPricing p
        WHERE p.clientPackage.id = :packageId
          AND p.component.id = :componentId
          AND p.effectiveFrom = :effectiveFrom
          AND (
                (:effectiveTo IS NULL AND p.effectiveTo IS NULL)
                OR
                (:effectiveTo IS NOT NULL AND p.effectiveTo = :effectiveTo)
              )
          AND p.isActive = true
    """)
    Optional<ClientComponentPricing> findExistingPricing(
            @Param("packageId") Long packageId,
            @Param("componentId") Long componentId,
            @Param("effectiveFrom") LocalDate effectiveFrom,
            @Param("effectiveTo") LocalDate effectiveTo
    );


    // =========================================
    // CHECK OVERLAPPING PRICING
    // =========================================

    @Query("""
        SELECT COUNT(p) > 0
        FROM ClientComponentPricing p
        WHERE p.clientPackage.id = :packageId
          AND p.component.id = :componentId
          AND p.isActive = true
          AND p.effectiveFrom <= :effectiveTo
          AND (
                p.effectiveTo IS NULL
                OR p.effectiveTo >= :effectiveFrom
              )
    """)
    boolean existsOverlappingPricing(
            @Param("packageId") Long packageId,
            @Param("componentId") Long componentId,
            @Param("effectiveFrom") LocalDate effectiveFrom,
            @Param("effectiveTo") LocalDate effectiveTo
    );


    // =========================================
    // CHECK OVERLAPPING PRICING
    // WITHOUT END DATE
    // =========================================

    @Query("""
        SELECT COUNT(p) > 0
        FROM ClientComponentPricing p
        WHERE p.clientPackage.id = :packageId
          AND p.component.id = :componentId
          AND p.isActive = true
          AND (
                p.effectiveTo IS NULL
                OR p.effectiveTo >= :effectiveFrom
              )
    """)
    boolean existsOverlappingPricingWithoutEndDate(
            @Param("packageId") Long packageId,
            @Param("componentId") Long componentId,
            @Param("effectiveFrom") LocalDate effectiveFrom
    );


    // =========================================
    // UPDATE OVERLAP CHECK
    // EXCLUDE CURRENT PRICING
    // =========================================

    @Query("""
        SELECT COUNT(p) > 0
        FROM ClientComponentPricing p
        WHERE p.clientPackage.id = :packageId
          AND p.component.id = :componentId
          AND p.isActive = true
          AND p.id <> :pricingId
          AND p.effectiveFrom <= :effectiveTo
          AND (
                p.effectiveTo IS NULL
                OR p.effectiveTo >= :effectiveFrom
              )
    """)
    boolean existsOverlappingPricingForUpdate(
            @Param("pricingId") Long pricingId,
            @Param("packageId") Long packageId,
            @Param("componentId") Long componentId,
            @Param("effectiveFrom") LocalDate effectiveFrom,
            @Param("effectiveTo") LocalDate effectiveTo
    );


    // =========================================
    // UPDATE OVERLAP CHECK
    // WITHOUT END DATE
    // =========================================

    @Query("""
        SELECT COUNT(p) > 0
        FROM ClientComponentPricing p
        WHERE p.clientPackage.id = :packageId
          AND p.component.id = :componentId
          AND p.isActive = true
          AND p.id <> :pricingId
          AND (
                p.effectiveTo IS NULL
                OR p.effectiveTo >= :effectiveFrom
              )
    """)
    boolean existsOverlappingPricingForUpdateWithoutEndDate(
            @Param("pricingId") Long pricingId,
            @Param("packageId") Long packageId,
            @Param("componentId") Long componentId,
            @Param("effectiveFrom") LocalDate effectiveFrom
    );

    
    Optional<ClientComponentPricing>
    findTopByClientPackageIdAndComponentIdAndIsActiveTrueOrderByIdDesc(
            Long clientPackageId,
            Long componentId);
    
}