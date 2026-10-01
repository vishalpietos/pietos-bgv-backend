package com.pietos.bgv.dto.request;

import java.util.List;

public class InternalUserRoleRequest {

    private Long primaryRoleId;

    private List<Long> roleIds;

    public InternalUserRoleRequest() {
    }

    public Long getPrimaryRoleId() {
        return primaryRoleId;
    }

    public void setPrimaryRoleId(Long primaryRoleId) {
        this.primaryRoleId = primaryRoleId;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }
}