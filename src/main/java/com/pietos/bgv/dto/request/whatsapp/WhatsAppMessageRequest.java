package com.pietos.bgv.dto.request.whatsapp;

public class WhatsAppMessageRequest {

    private String messaging_product;
    private String recipient_type;
    private String to;
    private String type;
    private TextMessage text;

    public WhatsAppMessageRequest() {
    }

    public String getMessaging_product() {
        return messaging_product;
    }

    public void setMessaging_product(String messaging_product) {
        this.messaging_product = messaging_product;
    }

    public String getRecipient_type() {
        return recipient_type;
    }

    public void setRecipient_type(String recipient_type) {
        this.recipient_type = recipient_type;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TextMessage getText() {
        return text;
    }

    public void setText(TextMessage text) {
        this.text = text;
    }

}
