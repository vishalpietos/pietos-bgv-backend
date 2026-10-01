	package com.pietos.bgv.repository.cases;
	
	import java.util.List;
	import java.util.Optional;
	
	import org.springframework.data.jpa.repository.JpaRepository;
	import org.springframework.stereotype.Repository;
	
	import com.pietos.bgv.entity.cases.Case;
	
	@Repository
	public interface CaseRepository extends JpaRepository<Case, Long> {
	
	    // Check whether Case Reference already exists
	    boolean existsByCaseRef(String caseRef);
	
	    // Find case by Case Reference
	    Optional<Case> findByCaseRef(String caseRef);
	    
	    List<Case> findByCreatedById(Long userId);
	    
	    boolean existsByClientEmployeeId(String clientEmployeeId);
	
	    boolean existsByMobileNumber(String mobileNumber);
	
	    boolean existsByEmailIgnoreCase(String email);
	
	    
	}