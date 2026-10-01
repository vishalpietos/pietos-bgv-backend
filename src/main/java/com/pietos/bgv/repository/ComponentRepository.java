package com.pietos.bgv.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pietos.bgv.entity.Component;

@Repository
public interface ComponentRepository extends
        JpaRepository<Component, Long>,
        JpaSpecificationExecutor<Component> {

    boolean existsByComponentName(String componentName);
    boolean existsByComponentCode(String componentCode);

    Optional<Component> findByComponentName(String componentName);

}