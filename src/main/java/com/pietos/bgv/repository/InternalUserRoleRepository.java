package com.pietos.bgv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pietos.bgv.entity.InternalUserRole;

@Repository
public interface InternalUserRoleRepository
        extends JpaRepository<InternalUserRole, Long> {

    // Get all roles assigned to a system user
    List<InternalUserRole> findBySystemUserId(Long systemUserId);

    // Check whether a specific role is already assigned
    boolean existsBySystemUserIdAndRoleId(
            Long systemUserId,
            Long roleId
    );

    // Delete all role assignments of a system user
    void deleteBySystemUserId(Long systemUserId);
    
    List<InternalUserRole> findBySystemUserIdAndIsActiveTrue(
            Long systemUserId
    );
}