package com.pietos.bgv.dto.request.whatsapp;

import java.util.List;

public class Component {

    private String type;
    private List<Parameter> parameters;

    public Component() {
    }

    public Component(String type, List<Parameter> parameters) {
        this.type = type;
        this.parameters = parameters;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}