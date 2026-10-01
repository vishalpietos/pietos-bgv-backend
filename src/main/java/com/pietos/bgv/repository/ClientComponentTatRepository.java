package com.pietos.bgv.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pietos.bgv.entity.ClientComponentTat;

@Repository
public interface ClientComponentTatRepository
        extends JpaRepository<ClientComponentTat, Long> {


    // =========================================
    // FIND ALL ACTIVE TAT OF PACKAGE
    // =========================================

    List<ClientComponentTat>
    findByClientPackageComponentClientPackageIdAndIsActiveTrue(
            Long packageId);


    // =========================================
    // FIND ACTIVE TAT BY PACKAGE COMPONENT
    // =========================================

    Optional<ClientComponentTat>
    findByClientPackageComponentIdAndIsActiveTrue(
            Long clientPackageComponentId);


    // =========================================
    // FIND EXISTING TAT
    // PACKAGE + COMPONENT + DATES
    // =========================================

    Optional<ClientComponentTat>
    findByClientPackageComponentClientPackageIdAndClientPackageComponentComponentIdAndEffectiveFromAndEffectiveToAndIsActiveTrue(
            Long packageId,
            Long componentId,
            LocalDate effectiveFrom,
            LocalDate effectiveTo);


    // =========================================
    // CHECK OVERLAPPING TAT
    // =========================================

    @Query("""
        SELECT COUNT(t) > 0
        FROM ClientComponentTat t
        WHERE t.clientPackageComponent.clientPackage.id = :packageId
          AND t.clientPackageComponent.component.id = :componentId
          AND t.isActive = true
          AND t.effectiveFrom <= :effectiveTo
          AND (
                t.effectiveTo IS NULL
                OR t.effectiveTo >= :effectiveFrom
              )
    """)
    boolean existsOverlappingTat(
            @Param("packageId") Long packageId,
            @Param("componentId") Long componentId,
            @Param("effectiveFrom") LocalDate effectiveFrom,
            @Param("effectiveTo") LocalDate effectiveTo);


    // =========================================
    // CHECK OVERLAPPING TAT
    // WITHOUT END DATE
    // =========================================

    @Query("""
        SELECT COUNT(t) > 0
        FROM ClientComponentTat t
        WHERE t.clientPackageComponent.clientPackage.id = :packageId
          AND t.clientPackageComponent.component.id = :componentId
          AND t.isActive = true
          AND (
                t.effectiveTo IS NULL
                OR t.effectiveTo >= :effectiveFrom
              )
    """)
    boolean existsOverlappingTatWithoutEndDate(
            @Param("packageId") Long packageId,
            @Param("componentId") Long componentId,
            @Param("effectiveFrom") LocalDate effectiveFrom);


    // =========================================
    // UPDATE OVERLAP CHECK
    // EXCLUDE CURRENT TAT
    // =========================================

    @Query("""
        SELECT COUNT(t) > 0
        FROM ClientComponentTat t
        WHERE t.id <> :tatId
          AND t.clientPackageComponent.clientPackage.id = :packageId
          AND t.clientPackageComponent.component.id = :componentId
          AND t.isActive = true
          AND t.effectiveFrom <= :effectiveTo
          AND (
                t.effectiveTo IS NULL
                OR t.effectiveTo >= :effectiveFrom
              )
    """)
    boolean existsOverlappingTatForUpdate(
            @Param("tatId") Long tatId,
            @Param("packageId") Long packageId,
            @Param("componentId") Long componentId,
            @Param("effectiveFrom") LocalDate effectiveFrom,
            @Param("effectiveTo") LocalDate effectiveTo);


    // =========================================
    // UPDATE OVERLAP CHECK
    // WITHOUT END DATE
    // =========================================

    @Query("""
        SELECT COUNT(t) > 0
        FROM ClientComponentTat t
        WHERE t.id <> :tatId
          AND t.clientPackageComponent.clientPackage.id = :packageId
          AND t.clientPackageComponent.component.id = :componentId
          AND t.isActive = true
          AND (
                t.effectiveTo IS NULL
                OR t.effectiveTo >= :effectiveFrom
              )
    """)
    boolean existsOverlappingTatWithoutEndDateForUpdate(
            @Param("tatId") Long tatId,
            @Param("packageId") Long packageId,
            @Param("componentId") Long componentId,
            @Param("effectiveFrom") LocalDate effectiveFrom);
    
    
 
}