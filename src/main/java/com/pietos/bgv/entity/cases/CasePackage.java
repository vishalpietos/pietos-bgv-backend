package com.pietos.bgv.entity.cases;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.pietos.bgv.entity.ClientPackage;
import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.entity.cases.Case;

@Entity
@Table(
    name = "case_packages",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_case_packages_case",
            columnNames = "case_id"
        )
    }
)
public class CasePackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // -------------------------------------------------
    // CASE
    // -------------------------------------------------

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "case_id",
        nullable = false,
        foreignKey = @ForeignKey(
            name = "fk_case_packages_case"
        )
    )
    private Case caseEntity;

    // -------------------------------------------------
    // PACKAGE
    // -------------------------------------------------

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "package_id",
        foreignKey = @ForeignKey(
            name = "fk_case_packages_package"
        )
    )
    private ClientPackage clientPackage;

    // -------------------------------------------------
    // CREATED BY
    // -------------------------------------------------

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "created_by",
        nullable = false,
        foreignKey = @ForeignKey(
            name = "fk_case_packages_created_by"
        )
    )
    private SystemUser createdBy;

    // -------------------------------------------------
    // CREATED AT
    // -------------------------------------------------

    @Column(
        name = "created_at",
        nullable = false
    )
    private LocalDateTime createdAt;

    // -------------------------------------------------
    // UPDATED BY
    // -------------------------------------------------

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "updated_by",
        nullable = false,
        foreignKey = @ForeignKey(
            name = "fk_case_packages_updated_by"
        )
    )
    private SystemUser updatedBy;

    // -------------------------------------------------
    // UPDATED AT
    // -------------------------------------------------

    @Column(
        name = "updated_at",
        nullable = false
    )
    private LocalDateTime updatedAt;


    // =================================================
    // GETTERS / SETTERS
    // =================================================

    public Long getId() {
        return id;
    }

    public Case getCaseEntity() {
        return caseEntity;
    }

    public void setCaseEntity(Case caseEntity) {
        this.caseEntity = caseEntity;
    }

    public ClientPackage getClientPackage() {
        return clientPackage;
    }

    public void setClientPackage(ClientPackage clientPackage) {
        this.clientPackage = clientPackage;
    }

    public SystemUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(SystemUser createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public SystemUser getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(SystemUser updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}