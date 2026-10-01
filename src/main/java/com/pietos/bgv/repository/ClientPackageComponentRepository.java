package com.pietos.bgv.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pietos.bgv.entity.ClientPackageComponent;

@Repository
public interface ClientPackageComponentRepository
        extends JpaRepository<ClientPackageComponent, Long> {
	
	List<ClientPackageComponent>
    findByClientPackageIdAndIsActiveTrue(Long packageId);
	
	Optional<ClientPackageComponent>
	findByIdAndIsActiveTrue(Long id);
	
	Optional<ClientPackageComponent>
	findByClientPackageIdAndComponentIdAndIsActiveTrue(
	        Long packageId,
	        Long componentId);
	
	Optional<ClientPackageComponent>
	findByIdAndClientPackageIdAndIsActiveTrue(
	        Long id,
	        Long packageId);
	
	
	boolean existsByClientPackageIdAndComponentIdAndIsActiveTrue(
	        Long packageId,
	        Long componentId);

}