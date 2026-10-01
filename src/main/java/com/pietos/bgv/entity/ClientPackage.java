package com.pietos.bgv.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.pietos.bgv.enums.HolidayType;
import com.pietos.bgv.enums.PackageType;

import jakarta.persistence.*;

@Entity
@Table(name = "client_packages")
public class ClientPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Client to whom this package belongs
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_information_id", nullable = false)
    private ClientInformation clientInformation;

    @Column(name = "package_name", nullable = false, length = 150)
    private String packageName;

    // PACKAGE / COMPONENT
    @Enumerated(EnumType.STRING)
    @Column(name = "package_type", nullable = false, length = 20)
    private PackageType packageType;

    // Package TAT
    @Column(name = "package_tat")
    private Integer packageTat;

    // Internal TAT
    @Column(name = "internal_tat")
    private Integer internalTat;

    // Package Rate
    @Column(name = "package_rate", precision = 12, scale = 2)
    private BigDecimal packageRate;

    // Holiday Type
    @Enumerated(EnumType.STRING)
    @Column(name = "holiday_type", length = 30)
    private HolidayType holidayType;

    // Who created the package
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private SystemUser createdBy;

    // Who last updated the package
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private SystemUser updatedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Package -> Multiple Components
    @OneToMany(
            mappedBy = "clientPackage",
            cascade = CascadeType.ALL
    )
    private List<ClientPackageComponent> packageComponents =
            new ArrayList<>();

    // Active / Inactive
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;


    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "internal_effective_to")
    private LocalDate internalEffectiveTo;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClientInformation getClientInformation() {
        return clientInformation;
    }

    public void setClientInformation(ClientInformation clientInformation) {
        this.clientInformation = clientInformation;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public Integer getPackageTat() {
        return packageTat;
    }

    public void setPackageTat(Integer packageTat) {
        this.packageTat = packageTat;
    }

    public Integer getInternalTat() {
        return internalTat;
    }

    public void setInternalTat(Integer internalTat) {
        this.internalTat = internalTat;
    }

    public BigDecimal getPackageRate() {
        return packageRate;
    }

    public void setPackageRate(BigDecimal packageRate) {
        this.packageRate = packageRate;
    }

    public HolidayType getHolidayType() {
        return holidayType;
    }

    public void setHolidayType(HolidayType holidayType) {
        this.holidayType = holidayType;
    }

    public SystemUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(SystemUser createdBy) {
        this.createdBy = createdBy;
    }

    public SystemUser getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(SystemUser updatedBy) {
        this.updatedBy = updatedBy;
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

    public List<ClientPackageComponent> getPackageComponents() {
        return packageComponents;
    }

    public void setPackageComponents(
            List<ClientPackageComponent> packageComponents) {
        this.packageComponents = packageComponents;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

	public LocalDate getEffectiveFrom() {
		return effectiveFrom;
	}

	public void setEffectiveFrom(LocalDate effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public LocalDate getEffectiveTo() {
		return effectiveTo;
	}

	public void setEffectiveTo(LocalDate effectiveTo) {
		this.effectiveTo = effectiveTo;
	}

	public LocalDate getInternalEffectiveTo() {
		return internalEffectiveTo;
	}

	public void setInternalEffectiveTo(LocalDate internalEffectiveTo) {
		this.internalEffectiveTo = internalEffectiveTo;
	}
    
    
}