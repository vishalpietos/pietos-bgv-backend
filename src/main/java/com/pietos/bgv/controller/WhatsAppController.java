package com.pietos.bgv.controller;

import com.pietos.bgv.service.WhatsAppService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsAppController {

    private final WhatsAppService whatsAppService;

    public WhatsAppController(WhatsAppService whatsAppService) {
        this.whatsAppService = whatsAppService;
    }

    @PostMapping("/send")
    public String sendMessage(
            @RequestParam String phoneNumber,
            @RequestParam String message) {

        return whatsAppService.sendTextMessage(phoneNumber, message);
    }
    
    @PostMapping("/send-template")
    public String sendTemplate() {

        return whatsAppService.sendTemplateMessage(
                "919811841162",
                "John Doe",
                "123456",
                "Aug 7, 2026"
        );
    }
}