package com.pietos.bgv.repository;

import com.pietos.bgv.entity.InternalUserComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternalUserComponentRepository
        extends JpaRepository<InternalUserComponent, Long> {

    List<InternalUserComponent> findBySystemUserId(Long systemUserId);

    List<InternalUserComponent> findBySystemUserIdAndIsActiveTrue(Long systemUserId);

    boolean existsBySystemUserIdAndComponentId(Long systemUserId,Long componentId);
    
    List<InternalUserComponent> findByComponent_ComponentNameAndComponent_IsActiveTrueAndIsActiveTrueAndSystemUser_IsActiveTrue(String componentName);
}