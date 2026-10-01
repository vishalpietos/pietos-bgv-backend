package com.pietos.bgv.service.impl;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.pietos.bgv.service.EmailService;

import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    private final Gmail gmailService;

    public EmailServiceImpl(Gmail gmailService) {
        this.gmailService = gmailService;
    }

    @Override
    public void sendEmail(String to,
                          String subject,
                          String htmlBody) {

        try {

            MimeMessage mimeMessage =
                    createMail(to, subject, htmlBody);

            Message gmailMessage =
                    createMessage(mimeMessage);

            gmailService.users()
                    .messages()
                    .send("me", gmailMessage)
                    .execute();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
   
    private MimeMessage createMail(String to,
            String subject,
            String htmlBody) throws Exception {

Session session =
Session.getDefaultInstance(System.getProperties());

MimeMessage email = new MimeMessage(session);

email.setFrom(new InternetAddress("bgv-noreply@pietos.com"));

email.addRecipient(
jakarta.mail.Message.RecipientType.TO,
new InternetAddress(to));

email.setSubject(subject, "UTF-8");

email.setContent(htmlBody, "text/html; charset=UTF-8");

return email;
}

private Message createMessage(MimeMessage mimeMessage) throws Exception {

ByteArrayOutputStream buffer = new ByteArrayOutputStream();

mimeMessage.writeTo(buffer);

String encodedEmail = Base64.getUrlEncoder()
.withoutPadding()
.encodeToString(buffer.toByteArray());

Message message = new Message();

message.setRaw(encodedEmail);

return message;
}

}