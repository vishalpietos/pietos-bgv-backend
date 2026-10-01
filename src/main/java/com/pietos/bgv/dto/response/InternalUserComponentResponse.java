package com.pietos.bgv.dto.response;

import java.time.LocalDateTime;

public class InternalUserComponentResponse {

    private Long id;
    private Long systemUserId;
    private Long componentId;
    private String componentName;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public InternalUserComponentResponse() {
    }

    public InternalUserComponentResponse(
            Long id,
            Long systemUserId,
            Long componentId,
            String componentName,
            Boolean isActive,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.id = id;
        this.systemUserId = systemUserId;
        this.componentId = componentId;
        this.componentName = componentName;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSystemUserId() {
        return systemUserId;
    }

    public void setSystemUserId(Long systemUserId) {
        this.systemUserId = systemUserId;
    }

    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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