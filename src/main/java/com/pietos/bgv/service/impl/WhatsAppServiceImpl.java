package com.pietos.bgv.service.impl;

import com.pietos.bgv.config.WhatsAppConfig;
import com.pietos.bgv.dto.request.whatsapp.TextMessage;
import com.pietos.bgv.dto.request.whatsapp.WhatsAppMessageRequest;
import com.pietos.bgv.service.WhatsAppService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.pietos.bgv.dto.request.whatsapp.*;
import java.util.List;

@Service
public class WhatsAppServiceImpl implements WhatsAppService {

    private final WhatsAppConfig config;

    public WhatsAppServiceImpl(WhatsAppConfig config) {
        this.config = config;
    }

    @Override
    public String sendTextMessage(String phoneNumber, String message) {

        RestTemplate restTemplate = new RestTemplate();

        WhatsAppMessageRequest request = new WhatsAppMessageRequest();

        request.setMessaging_product("whatsapp");
        request.setRecipient_type("individual");
        request.setTo(phoneNumber);
        request.setType("text");

        TextMessage text = new TextMessage();
        text.setBody(message);
        text.setPreview_url(false);

        request.setText(text);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(config.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<WhatsAppMessageRequest> entity =
                new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                config.getMessagesUrl(),
                HttpMethod.POST,
                entity,
                String.class);

        return response.getBody();
    }

    @Override
    public String sendTemplateMessage(String phoneNumber,
                                      String customerName,
                                      String orderId,
                                      String orderDate) {

        RestTemplate restTemplate = new RestTemplate();

        Parameter p1 = new Parameter("text", customerName);
        Parameter p2 = new Parameter("text", orderId);
        Parameter p3 = new Parameter("text", orderDate);

        Component component = new Component();
        component.setType("body");
        component.setParameters(List.of(p1, p2, p3));

        Language language = new Language();
        language.setCode("en_US");

        Template template = new Template();
        template.setName("jaspers_market_order_confirmation_v1");
        template.setLanguage(language);
        template.setComponents(List.of(component));

        TemplateMessageRequest request = new TemplateMessageRequest();
        request.setMessaging_product("whatsapp");
        request.setTo(phoneNumber);
        request.setType("template");
        request.setTemplate(template);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(config.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TemplateMessageRequest> entity =
                new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                config.getMessagesUrl(),
                HttpMethod.POST,
                entity,
                String.class);

        return response.getBody();
    }
}