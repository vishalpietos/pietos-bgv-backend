package com.pietos.bgv.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(
    name = "internal_user_components",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_internal_user_component",
            columnNames = {
                "system_user_id",
                "component_id"
            }
        )
    }
)
public class InternalUserComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // SYSTEM USER
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "system_user_id",
        nullable = false
    )
    private SystemUser systemUser;

    // =====================================================
    // COMPONENT
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "component_id",
        nullable = false
    )
    private Component component;

    // =====================================================
    // STATUS
    // =====================================================

    @Column(
        name = "is_active",
        nullable = false
    )
    private Boolean isActive = true;

    // =====================================================
    // AUDIT
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private SystemUser createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private SystemUser updatedBy;

    @Column(
        name = "created_at",
        nullable = false
    )
    private LocalDateTime createdAt;

    @Column(
        name = "updated_at",
        nullable = false
    )
    private LocalDateTime updatedAt;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public InternalUserComponent() {
    }

    // =====================================================
    // LIFECYCLE
    // =====================================================

    @PrePersist
    public void prePersist() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (isActive == null) {
            isActive = true;
        }
    }

    @PreUpdate
    public void preUpdate() {

        updatedAt = LocalDateTime.now();
    }

    // =====================================================
    // GETTERS & SETTERS
    // =====================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SystemUser getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(SystemUser systemUser) {
        this.systemUser = systemUser;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public SystemUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(SystemUser createdBy) {
        this.createdBy = createdBy;
    }

    public SystemUser getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(SystemUser updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}