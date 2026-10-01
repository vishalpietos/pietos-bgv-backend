	package com.pietos.bgv.entity;
	
	import java.time.LocalDateTime;
	
	import jakarta.persistence.*;
	
	@Entity
	@Table(name = "components")
	public class Component {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	
	    @Column(name = "component_name", nullable = false, unique = true, length = 150)
	    private String componentName;
	    
	    @Column( name = "component_code", unique = true,length = 50)
	    private String componentCode;
	
	    @Column(name = "is_active", nullable = false)
	    private Boolean isActive = true;
	
	    @Column(name = "created_at", updatable = false)
	    private LocalDateTime createdAt;
	
	    @Column(name = "updated_at")
	    private LocalDateTime updatedAt;
	
	    @PrePersist
	    public void prePersist() {
	        createdAt = LocalDateTime.now();
	        updatedAt = LocalDateTime.now();
	    }
	
	    @PreUpdate
	    public void preUpdate() {
	        updatedAt = LocalDateTime.now();
	    }
	
	    // ==========================
	    // Getters and Setters
	    // ==========================
	
	    public Long getId() {
	        return id;
	    }
	
	    public void setId(Long id) {
	        this.id = id;
	    }
	
	    public String getComponentName() {
	        return componentName;
	    }
	
	    public void setComponentName(String componentName) {
	        this.componentName = componentName;
	    }
	
	    public Boolean getIsActive() {
	        return isActive;
	    }
	
	    public void setIsActive(Boolean isActive) {
	        this.isActive = isActive;
	    }
	
	    public LocalDateTime getCreatedAt() {
	        return createdAt;
	    }
	
	    public void setCreatedAt(LocalDateTime createdAt) {
	        this.createdAt = createdAt;
	    }
	
	    public LocalDateTime getUpdatedAt() {
	        return updatedAt;
	    }
	
	    public void setUpdatedAt(LocalDateTime updatedAt) {
	        this.updatedAt = updatedAt;
	    }
	
		public String getComponentCode() {
			return componentCode;
		}
	
		public void setComponentCode(String componentCode) {
			this.componentCode = componentCode;
		}
	    
	    
	}