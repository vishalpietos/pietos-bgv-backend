package com.pietos.bgv.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.pietos.bgv.enums.HolidayType;

import jakarta.persistence.*;

@Entity
@Table(name = "client_holiday_configuration")
public class ClientHolidayConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_information_id", nullable = false)
    private ClientInformation clientInformation;

    @Column(name = "wef", nullable = false)
    private LocalDate wef;

    @Column(name = "wet", nullable = false)
    private LocalDate wet;

    @Enumerated(EnumType.STRING)
    @Column(name = "holiday_type", nullable = false)
    private HolidayType holidayType;

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

    public ClientHolidayConfiguration() {
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
}