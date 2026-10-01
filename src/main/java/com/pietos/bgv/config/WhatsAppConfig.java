package com.pietos.bgv.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WhatsAppConfig {

    @Value("${whatsapp.access-token}")
    private String accessToken;

    @Value("${whatsapp.phone-number-id}")
    private String phoneNumberId;

    @Value("${whatsapp.business-account-id}")
    private String businessAccountId;

    @Value("${whatsapp.api-version}")
    private String apiVersion;

    public String getAccessToken() {
        return accessToken;
    }

    public String getPhoneNumberId() {
        return phoneNumberId;
    }

    public String getBusinessAccountId() {
        return businessAccountId;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public String getMessagesUrl() {
        return "https://graph.facebook.com/"
                + apiVersion
                + "/"
                + phoneNumberId
                + "/messages";
    }


}
