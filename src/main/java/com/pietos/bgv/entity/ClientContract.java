package com.pietos.bgv.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "client_contracts")
public class ClientContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================================
    // CLIENT
    // =========================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_information_id", nullable = false)
    private ClientInformation clientInformation;

    // =========================================
    // CONTRACT DETAILS
    // =========================================

    @Column(name = "contract_name", nullable = false, length = 200)
    private String contractName;

    @Column(name = "wef", nullable = false)
    private LocalDate wef;

    @Column(name = "wet", nullable = false)
    private LocalDate wet;

    // =========================================
    // DOCUMENTS
    // =========================================

    @OneToMany(
        mappedBy = "clientContract",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private List<ClientContractDocument> documents = new ArrayList<>();

    // =========================================
    // AUDIT
    // =========================================

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

    // =========================================
    // CONSTRUCTOR
    // =========================================

    public ClientContract() {
    }

    // =========================================
    // LIFECYCLE
    // =========================================

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // =========================================
    // GETTERS & SETTERS
    // =========================================

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

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public LocalDate getWef() {
        return wef;
    }

    public void setWef(LocalDate wef) {
        this.wef = wef;
    }

    public LocalDate getWet() {
        return wet;
    }

    public void setWet(LocalDate wet) {
        this.wet = wet;
    }

    public List<ClientContractDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<ClientContractDocument> documents) {
        this.documents = documents;
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