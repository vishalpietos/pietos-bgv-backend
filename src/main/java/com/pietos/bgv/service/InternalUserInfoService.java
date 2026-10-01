package com.pietos.bgv.service;

import java.util.List;

import com.pietos.bgv.dto.request.InternalUserInfoRequest;
import com.pietos.bgv.dto.response.InternalUserInfoResponse;

public interface InternalUserInfoService {

    InternalUserInfoResponse createUser(InternalUserInfoRequest request);

    List<InternalUserInfoResponse> getAllUsers();

    InternalUserInfoResponse getUserById(Long id);

    InternalUserInfoResponse updateUser(Long id, InternalUserInfoRequest request);

    void updateUserStatus(Long id, Boolean isActive);
}