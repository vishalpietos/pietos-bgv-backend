package com.pietos.bgv.service;

import java.util.List;

import com.pietos.bgv.dto.request.client.ComponentRequest;
import com.pietos.bgv.dto.response.client.ComponentResponse;

public interface ComponentService {

    ComponentResponse createComponent(ComponentRequest request);

    List<ComponentResponse> getAllComponents();

    ComponentResponse getComponentById(Long id);

    ComponentResponse updateComponent(Long id, ComponentRequest request);

    void activateComponent(Long id);

    void deactivateComponent(Long id);
}