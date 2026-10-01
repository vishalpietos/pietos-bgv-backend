package com.pietos.bgv.service;

import java.util.List;

import com.pietos.bgv.dto.request.InternalUserRoleRequest;
import com.pietos.bgv.dto.response.InternalUserRoleResponse;

public interface InternalUserRoleService {

    // Assign / update roles for an internal user
    List<InternalUserRoleResponse> saveRoles(
            Long systemUserId,
            InternalUserRoleRequest request
    );

    // Get all roles assigned to an internal user
    List<InternalUserRoleResponse> getRoles(
            Long systemUserId
    );
}