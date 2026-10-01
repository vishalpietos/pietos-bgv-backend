package com.pietos.bgv.entity.cases;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.pietos.bgv.entity.ClientPackageComponent;
import com.pietos.bgv.entity.Component;

@Entity
@Table(name = "case_package_components")
public class CasePackageComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // -------------------------------------------------
    // CASE PACKAGE
    // -------------------------------------------------
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "client_package_component_id",
        nullable = false
    )
    private ClientPackageComponent clientPackageComponent;
    
    

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "case_package_id",
        nullable = false,
        foreignKey = @ForeignKey(
            name = "fk_case_package_components_package"
        )
    )
    private CasePackage casePackage;

    // -------------------------------------------------
    // COMPONENT
    // -------------------------------------------------

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "component_id",
        nullable = false,
        foreignKey = @ForeignKey(
            name = "fk_case_package_components_component"
        )
    )
    private Component component;
    
    

    // -------------------------------------------------
    // CREATED AT
    // -------------------------------------------------

    @Column(
        name = "created_at",
        nullable = false
    )
    private LocalDateTime createdAt;

    // -------------------------------------------------
    // UPDATED AT
    // -------------------------------------------------

    @Column(
        name = "updated_at",
        nullable = false
    )
    private LocalDateTime updatedAt;


    // =================================================
    // GETTERS / SETTERS
    // =================================================

    public Long getId() {
        return id;
    }

    public CasePackage getCasePackage() {
        return casePackage;
    }

    public void setCasePackage(CasePackage casePackage) {
        this.casePackage = casePackage;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
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



	public void setId(Long id) {
		this.id = id;
	}

	public ClientPackageComponent getClientPackageComponent() {
		return clientPackageComponent;
	}

	public void setClientPackageComponent(ClientPackageComponent clientPackageComponent) {
		this.clientPackageComponent = clientPackageComponent;
	}
    
	
    
}