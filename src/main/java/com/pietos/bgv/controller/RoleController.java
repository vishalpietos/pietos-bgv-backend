package com.pietos.bgv.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pietos.bgv.dto.request.RoleRequest;
import com.pietos.bgv.dto.response.RoleResponse;
import com.pietos.bgv.service.RoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@RequestBody RoleRequest request) {

        RoleResponse response = roleService.createRole(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RoleResponse>> getAllRoles() {

        List<RoleResponse> response = roleService.getAllRoles();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable Long id) {

        RoleResponse response = roleService.getRoleById(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> updateRole(
            @PathVariable Long id,
            @RequestBody RoleRequest request) {

        RoleResponse response = roleService.updateRole(id, request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateRole(@PathVariable Long id) {

        roleService.deactivateRole(id);

        return ResponseEntity.ok("Role deactivated successfully.");
    }

}