package com.pietos.bgv.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(
    name = "internal_user_roles",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_internal_user_role",
            columnNames = {
                "system_user_id",
                "role_id"
            }
        )
    }
)
public class InternalUserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "system_user_id",
        nullable = false
    )
    private SystemUser systemUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "role_id",
        nullable = false
    )
    private Role role;


    @Column(
        name = "is_active",
        nullable = false
    )
    private Boolean isActive = true;

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


    public InternalUserRole() {
    }


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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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