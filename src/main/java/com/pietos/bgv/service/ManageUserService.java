package com.pietos.bgv.service;

import java.util.List;

import com.pietos.bgv.dto.request.ManageUser.ManageUserCreateRequest;
import com.pietos.bgv.dto.request.ManageUser.ManageUserPatchRequest;
import com.pietos.bgv.dto.request.ManageUser.ManageUserSearchRequest;
import com.pietos.bgv.dto.request.ManageUser.ManageUserUpdateRequest;
import com.pietos.bgv.dto.response.ManageUserResponse;
import com.pietos.bgv.dto.response.client.ClientLocationDataResponse;

public interface ManageUserService {

    ManageUserResponse createUser(ManageUserCreateRequest request);

    List<ManageUserResponse> getAllUsers();

    ManageUserResponse getUserById(Long id);

    ManageUserResponse updateUser(Long id,ManageUserUpdateRequest request);

    ManageUserResponse patchUser(Long id,ManageUserPatchRequest request);

    void activateUser(Long id);

    void deactivateUser(Long id);

    List<ManageUserResponse> searchUsers(ManageUserSearchRequest request);

    List<ManageUserResponse> getUsersByClientId(Long clientId);

    List<ManageUserResponse> getUsersByLocationId(Long locationId);
    
    ClientLocationDataResponse getClientLocationData(Long clientId);
}