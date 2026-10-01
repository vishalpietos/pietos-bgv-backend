package com.pietos.bgv.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.pietos.bgv.dto.request.RoleRequest;
import com.pietos.bgv.dto.response.RoleResponse;
import com.pietos.bgv.entity.Role;
import com.pietos.bgv.exception.DuplicateResourceException;
import com.pietos.bgv.exception.ResourceNotFoundException;
import com.pietos.bgv.mapper.RoleMapper;
import com.pietos.bgv.repository.RoleRepository;
import com.pietos.bgv.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleResponse createRole(RoleRequest request) {

        if (roleRepository.existsByRoleName(request.getRoleName())) { throw new DuplicateResourceException( "Role already exists with name : " + request.getRoleName());}

        Role role = RoleMapper.toEntity(request);

        Role savedRole = roleRepository.save(role);

        return RoleMapper.toResponse(savedRole);
    }

    @Override
    public List<RoleResponse> getAllRoles() {

        return roleRepository.findAll()
                .stream()
                .map(RoleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponse getRoleById(Long id) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found with id : " + id));

        return RoleMapper.toResponse(role);
    }

    @Override
    public RoleResponse updateRole(Long id, RoleRequest request) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found with id : " + id));

        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());

        Role updatedRole = roleRepository.save(role);

        return RoleMapper.toResponse(updatedRole);
    }

    @Override
    public void deactivateRole(Long id) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found with id : " + id));

        role.setIsActive(false);

        roleRepository.save(role);
    }
}