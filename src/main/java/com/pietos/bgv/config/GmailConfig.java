package com.pietos.bgv.config;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

@Configuration
public class GmailConfig {

    private static final String APPLICATION_NAME = "Pietos BGV Portal";

    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    // JSON inside src/main/resources
    private static final String SERVICE_ACCOUNT_FILE =
            "smart-setting-362807-454a319351f2.json";

    // Email provided by your manager
    private static final String DELEGATED_USER =
            "bgv-noreply@pietos.com";

    @Bean
    public Gmail gmailService() throws GeneralSecurityException, IOException {

        NetHttpTransport httpTransport =
                GoogleNetHttpTransport.newTrustedTransport();

        ClassPathResource resource =
                new ClassPathResource(SERVICE_ACCOUNT_FILE);

        InputStream inputStream = resource.getInputStream();

        GoogleCredentials credentials =
                GoogleCredentials.fromStream(inputStream)
                        .createScoped(Collections.singleton(GmailScopes.GMAIL_SEND))
                        .createDelegated(DELEGATED_USER);

        return new Gmail.Builder(
                httpTransport,
                JSON_FACTORY,
                new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}