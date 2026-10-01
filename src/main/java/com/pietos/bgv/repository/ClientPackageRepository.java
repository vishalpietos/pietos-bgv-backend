package com.pietos.bgv.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pietos.bgv.entity.ClientPackage;

@Repository
public interface ClientPackageRepository
        extends JpaRepository<ClientPackage, Long> {
	
	List<ClientPackage> findByClientInformationIdAndIsActiveTrue(
            Long clientId);
	
	  Optional<ClientPackage> findByIdAndClientInformationIdAndIsActiveTrue(
	            Long packageId,
	            Long clientId);

	 Optional<ClientPackage> findByIdAndIsActiveTrue(Long packageId);
}