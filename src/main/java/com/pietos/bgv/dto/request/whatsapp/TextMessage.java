package com.pietos.bgv.dto.request.whatsapp;



public class TextMessage {

    private String body;
    private boolean preview_url;

    public TextMessage() {
    }

    public TextMessage(String body, boolean preview_url) {
        this.body = body;
        this.preview_url = preview_url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isPreview_url() {
        return preview_url;
    }

    public void setPreview_url(boolean preview_url) {
        this.preview_url = preview_url;
    }
}