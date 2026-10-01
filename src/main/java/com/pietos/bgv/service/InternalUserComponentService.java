package com.pietos.bgv.service;

import com.pietos.bgv.dto.request.InternalUserComponentRequest;
import com.pietos.bgv.dto.response.InternalUserComponentResponse;
import com.pietos.bgv.dto.response.cases.AssignmentUserResponse;

import java.util.List;

public interface InternalUserComponentService {

    List<InternalUserComponentResponse> saveComponents(Long systemUserId,InternalUserComponentRequest request);

    List<InternalUserComponentResponse> getComponents(Long systemUserId);
    
    List<AssignmentUserResponse> getAssignmentUsers(String componentName);
    
    
}