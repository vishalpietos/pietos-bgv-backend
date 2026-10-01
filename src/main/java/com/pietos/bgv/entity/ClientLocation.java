package com.pietos.bgv.entity;

import java.time.LocalDateTime;

import com.pietos.bgv.enums.ClientLocationStatus;

import jakarta.persistence.*;

@Entity
@Table(name = "client_locations")
public class ClientLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= Client =================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientInformation clientInformation;

    // ================= Location Details =================

    @Column(name = "location_name", nullable = false, length = 150)
    private String locationName;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(nullable = false, length = 100)
    private String country;

    @Column(nullable = false, length = 100)
    private String state;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(length = 10)
    private String pincode;

    @Column(name = "contact_person", length = 150)
    private String contactPerson;

    @Column(name = "official_email", length = 150)
    private String officialEmail;

    @Column(length = 20)
    private String mobile;

    @Column(length = 20)
    private String landline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientLocationStatus status;

    @Column(length = 500)
    private String remarks;

    // ================= Audit =================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private SystemUser createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private SystemUser updatedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public ClientLocation() {
    }

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

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getOfficialEmail() {
		return officialEmail;
	}

	public void setOfficialEmail(String officialEmail) {
		this.officialEmail = officialEmail;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getLandline() {
		return landline;
	}

	public void setLandline(String landline) {
		this.landline = landline;
	}

	public ClientLocationStatus getStatus() {
		return status;
	}

	public void setStatus(ClientLocationStatus status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

    
}