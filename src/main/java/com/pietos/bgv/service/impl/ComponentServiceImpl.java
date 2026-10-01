package com.pietos.bgv.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.pietos.bgv.dto.request.client.ComponentRequest;
import com.pietos.bgv.dto.response.client.ComponentResponse;
import com.pietos.bgv.entity.Component;
import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.exception.DuplicateResourceException;
import com.pietos.bgv.exception.ResourceNotFoundException;
import com.pietos.bgv.repository.ComponentRepository;
import com.pietos.bgv.service.ComponentService;


@Service
public class ComponentServiceImpl implements ComponentService {

    private final ComponentRepository componentRepository;
   

    public ComponentServiceImpl(
            ComponentRepository componentRepository) {

        this.componentRepository = componentRepository;
        
    }

    @Override
    public ComponentResponse createComponent(ComponentRequest request) {

        if (componentRepository.existsByComponentName(request.getComponentName())) {
            throw new DuplicateResourceException("Component already exists.");
        }

 
        Component component = new Component();
        component.setComponentName(request.getComponentName());
        component.setComponentCode(generateComponentCode(request.getComponentName()));
        component.setIsActive(true);
        component.setCreatedAt(LocalDateTime.now());
        component.setUpdatedAt(LocalDateTime.now());
        Component savedComponent = componentRepository.save(component);

        return mapToResponse(savedComponent);
    }
    
    @Override
    public List<ComponentResponse> getAllComponents() {

        return componentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ComponentResponse getComponentById(Long id) {

        Component component = componentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Component not found with id : " + id));

        return mapToResponse(component);
    }
    
    @Override
    public ComponentResponse updateComponent(
            Long id,
            ComponentRequest request) {

        Component component = componentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Component not found with id : " + id));

        if (!component.getComponentName().equalsIgnoreCase(request.getComponentName())
                && componentRepository.existsByComponentName(request.getComponentName())) {

            throw new DuplicateResourceException("Component already exists.");
        }

        

        component.setComponentName(request.getComponentName());

       
        component.setUpdatedAt(LocalDateTime.now());

        Component updatedComponent = componentRepository.save(component);

        return mapToResponse(updatedComponent);
    }

    @Override
    public void activateComponent(Long id) {

        Component component = componentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Component not found with id : " + id));

        component.setIsActive(true);

        componentRepository.save(component);
    }

    @Override
    public void deactivateComponent(Long id) {

        Component component = componentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Component not found with id : " + id));

        component.setIsActive(false);

        componentRepository.save(component);
    }
    
    
    private String generateComponentCode(String componentName) {

        return componentName
                .trim()
                .toUpperCase()
                .replaceAll("[^A-Z0-9]+", "_");
    }
    
    private ComponentResponse mapToResponse(Component component) {

        ComponentResponse response = new ComponentResponse();

        response.setId(component.getId());
        response.setComponentName(component.getComponentName());
        response.setComponentCode(component.getComponentCode());
        response.setIsActive(component.getIsActive());
        response.setCreatedAt(component.getCreatedAt());
        response.setUpdatedAt(component.getUpdatedAt());

        return response;
    }
}