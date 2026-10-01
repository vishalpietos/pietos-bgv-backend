package com.pietos.bgv.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pietos.bgv.dto.request.InternalUserComponentRequest;
import com.pietos.bgv.dto.request.InternalUserRoleRequest;
import com.pietos.bgv.dto.request.InternalUserInfoRequest;
import com.pietos.bgv.dto.response.InternalUserComponentResponse;
import com.pietos.bgv.dto.response.InternalUserRoleResponse;
import com.pietos.bgv.dto.response.cases.AssignmentUserResponse;
import com.pietos.bgv.dto.response.InternalUserInfoResponse;
import com.pietos.bgv.service.InternalUserComponentService;
import com.pietos.bgv.service.InternalUserRoleService;
import com.pietos.bgv.service.InternalUserInfoService;

@RestController
@RequestMapping("/api/internal-users")
public class InternalUserInfoController {

    private final InternalUserInfoService internalUserInfoService;
    private final InternalUserRoleService internalUserRoleService;
    private final InternalUserComponentService internalUserComponentService;

    public InternalUserInfoController(
            InternalUserInfoService internalUserInfoService,
            InternalUserRoleService internalUserRoleService,
            InternalUserComponentService internalUserComponentService) {

        this.internalUserInfoService = internalUserInfoService;
        this.internalUserRoleService = internalUserRoleService;
        this.internalUserComponentService = internalUserComponentService;
    }

    // =====================================================
    // USER INFORMATION
    // =====================================================

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<InternalUserInfoResponse> createUser(
            @RequestBody InternalUserInfoRequest request) {

        return ResponseEntity.ok(
                internalUserInfoService.createUser(request)
        );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<List<InternalUserInfoResponse>> getAllUsers() {

        return ResponseEntity.ok(
                internalUserInfoService.getAllUsers()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<InternalUserInfoResponse> getUserById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                internalUserInfoService.getUserById(id)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<InternalUserInfoResponse> updateUser(
            @PathVariable Long id,
            @RequestBody InternalUserInfoRequest request) {

        return ResponseEntity.ok(
                internalUserInfoService.updateUser(id, request)
        );
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<Void> updateUserStatus(
            @PathVariable Long id,
            @RequestParam Boolean isActive) {

        internalUserInfoService.updateUserStatus(
                id,
                isActive
        );

        return ResponseEntity.ok().build();
    }

    // =====================================================
    // USER ROLES
    // =====================================================

    @PutMapping("/{systemUserId}/roles")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<List<InternalUserRoleResponse>> saveRoles(
            @PathVariable Long systemUserId,
            @RequestBody InternalUserRoleRequest request) {

        List<InternalUserRoleResponse> response =
                internalUserRoleService.saveRoles(
                        systemUserId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{systemUserId}/roles")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<List<InternalUserRoleResponse>> getRoles(
            @PathVariable Long systemUserId) {

        List<InternalUserRoleResponse> response =
                internalUserRoleService.getRoles(systemUserId);

        return ResponseEntity.ok(response);
    }


    // =====================================================
    // USER COMPONENTS
    // =====================================================

    @PutMapping("/{systemUserId}/components")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<List<InternalUserComponentResponse>> saveComponents(
            @PathVariable Long systemUserId,
            @RequestBody InternalUserComponentRequest request) {

        List<InternalUserComponentResponse> response =
                internalUserComponentService.saveComponents(
                        systemUserId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{systemUserId}/components")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'PROCESS_TEAM_LEADER')")
    public ResponseEntity<List<InternalUserComponentResponse>> getComponents(
            @PathVariable Long systemUserId) {

        List<InternalUserComponentResponse> response =
                internalUserComponentService.getComponents(
                        systemUserId
                );

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/assignment-users")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'PROCESS_TEAM_LEADER')")
    public ResponseEntity<List<AssignmentUserResponse>> getAssignmentUsers(
            @RequestParam String component) {

        return ResponseEntity.ok(
                internalUserComponentService
                        .getAssignmentUsers(component)
        );
    }	
    
    
}