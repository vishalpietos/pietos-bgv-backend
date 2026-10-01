package com.pietos.bgv.service;

import java.util.List;

import com.pietos.bgv.dto.request.RoleRequest;
import com.pietos.bgv.dto.response.RoleResponse;

public interface RoleService {

    RoleResponse createRole(RoleRequest request);

    List<RoleResponse> getAllRoles();

    RoleResponse getRoleById(Long id);

    RoleResponse updateRole(Long id, RoleRequest request);

    void deactivateRole(Long id);
}