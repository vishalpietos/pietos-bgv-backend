package com.pietos.bgv.service;

import java.util.List;

import com.pietos.bgv.dto.request.client.SubComponentRequest;
import com.pietos.bgv.dto.response.client.SubComponentResponse;

public interface SubComponentService {

    SubComponentResponse createSubComponent(SubComponentRequest request);

    List<SubComponentResponse> getAllSubComponents();

    SubComponentResponse getSubComponentById(Long id);

    List<SubComponentResponse> getSubComponentsByComponentId(Long componentId);

    SubComponentResponse updateSubComponent(
            Long id,
            SubComponentRequest request);

    void activateSubComponent(Long id);

    void deactivateSubComponent(Long id);

    void deleteSubComponent(Long id);
}