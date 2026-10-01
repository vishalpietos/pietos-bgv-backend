package com.pietos.bgv.mapper;

import com.pietos.bgv.dto.request.RoleRequest;
import com.pietos.bgv.dto.response.RoleResponse;
import com.pietos.bgv.entity.Role;

public class RoleMapper {

    private RoleMapper() {
    }

    public static Role toEntity(RoleRequest request) {

        Role role = new Role();

        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());

        return role;
    }

    public static RoleResponse toResponse(Role role) {

        RoleResponse response = new RoleResponse();

        response.setId(role.getId());
        response.setRoleName(role.getRoleName());
        response.setDescription(role.getDescription());
        response.setIsActive(role.getIsActive());

        return response;
    }

}