package com.pietos.bgv.mapper;

import com.pietos.bgv.dto.request.InternalUserInfoRequest;
import com.pietos.bgv.dto.response.InternalUserInfoResponse;
import com.pietos.bgv.entity.InternalUserInfo;

public class InternalUserInfoMapper {

    private InternalUserInfoMapper() {
    }

    // =====================================================
    // REQUEST DTO -> ENTITY
    // =====================================================

    public static InternalUserInfo toEntity(
            InternalUserInfoRequest request) {

        InternalUserInfo user = new InternalUserInfo();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setMobileNumber(request.getMobileNumber());
        user.setAlternateNumber(request.getAlternateNumber());
        user.setLocation(request.getLocation());
        user.setAddress(request.getAddress());
        user.setCountry(request.getCountry());
        user.setState(request.getState());
        user.setCity(request.getCity());
        user.setPincode(request.getPincode());
        user.setJoiningDate(request.getJoiningDate());
        user.setIsActive(request.getIsActive());

        return user;
    }

    // =====================================================
    // ENTITY -> RESPONSE DTO
    // =====================================================

    public static InternalUserInfoResponse toResponse(
            InternalUserInfo user) {

        InternalUserInfoResponse response =
                new InternalUserInfoResponse();

        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setMobileNumber(user.getMobileNumber());
        response.setAlternateNumber(user.getAlternateNumber());
        response.setLocation(user.getLocation());
        response.setAddress(user.getAddress());
        response.setCountry(user.getCountry());
        response.setState(user.getState());
        response.setCity(user.getCity());
        response.setPincode(user.getPincode());
        response.setJoiningDate(user.getJoiningDate());
        response.setIsActive(user.getIsActive());

        // =================================================
        // AUDIT INFORMATION
        // =================================================

        if (user.getCreatedBy() != null) {
            response.setCreatedBy(
                    user.getCreatedBy().getId()
            );
        }

        if (user.getUpdatedBy() != null) {
            response.setUpdatedBy(
                    user.getUpdatedBy().getId()
            );
        }

        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        return response;
    }
}