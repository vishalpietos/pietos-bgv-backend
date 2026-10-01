package com.pietos.bgv.service;

public interface EmailService {

    void sendEmail(
            String to,
            String subject,
            String htmlBody
    );

}