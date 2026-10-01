package com.pietos.bgv.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pietos.bgv.service.EmailService;

@RestController
@RequestMapping("/api/test")
public class TestEmailController {

    private final EmailService emailService;

    public TestEmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/email")
    public ResponseEntity<String> sendTestEmail(
            @RequestParam String name,
            @RequestParam String email) {

    	String subject = "Test Email";

    	String body =
    	        "<h2>Hello " + name + "</h2>"
    	      + "<p>This is a test email from Pietos BGV.</p>"
    	      + "<p>Password : <b>Temp@1234</b></p>";

    	emailService.sendEmail(
    	        email,
    	        subject,
    	        body
    	);

        return ResponseEntity.ok("Email sent successfully.");
    }

}