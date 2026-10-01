package com.pietos.bgv.security;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    private static final String ALGORITHM = "AES/GCM/NoPadding";

    private static final int GCM_TAG_LENGTH = 128;

    private static final int IV_LENGTH = 12;

    private final SecretKeySpec secretKey;

    public EncryptionService(
            @Value("${app.encryption.key}") String encryptionKey) {

    	byte[] keyBytes =
    	        Base64.getDecoder().decode(encryptionKey);
    	
    	if (keyBytes.length != 32) {
    	    throw new IllegalArgumentException(
    	            "Encryption key must be exactly 32 bytes.");
    	}

        this.secretKey =
                new SecretKeySpec(keyBytes, "AES");
    }


    public String encrypt(String value) {

        if (value == null || value.isBlank()) {
            return value;
        }

        try {

            // Generate a new random IV for every encryption
            byte[] iv = new byte[IV_LENGTH];

            SecureRandom secureRandom = new SecureRandom();

            secureRandom.nextBytes(iv);


            // Create cipher
            Cipher cipher =
                    Cipher.getInstance(ALGORITHM);

            GCMParameterSpec parameterSpec =
                    new GCMParameterSpec(
                            GCM_TAG_LENGTH,
                            iv);

            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    secretKey,
                    parameterSpec);


            // Encrypt
            byte[] encryptedBytes =
                    cipher.doFinal(
                            value.getBytes(
                                    StandardCharsets.UTF_8));


            // Store IV + encrypted data together
            byte[] combined =
                    new byte[iv.length + encryptedBytes.length];

            System.arraycopy(
                    iv,
                    0,
                    combined,
                    0,
                    iv.length);

            System.arraycopy(
                    encryptedBytes,
                    0,
                    combined,
                    iv.length,
                    encryptedBytes.length);


            return Base64.getEncoder()
                    .encodeToString(combined);

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Unable to encrypt data.",
                    e);
        }
    }


    public String decrypt(String encryptedValue) {

        if (encryptedValue == null
                || encryptedValue.isBlank()) {

            return encryptedValue;
        }

        try {

            // Decode Base64
            byte[] combined =
                    Base64.getDecoder()
                            .decode(encryptedValue);


            // Extract IV
            byte[] iv =
                    new byte[IV_LENGTH];

            System.arraycopy(
                    combined,
                    0,
                    iv,
                    0,
                    IV_LENGTH);


            // Extract encrypted data
            byte[] encryptedBytes =
                    new byte[
                            combined.length - IV_LENGTH];

            System.arraycopy(
                    combined,
                    IV_LENGTH,
                    encryptedBytes,
                    0,
                    encryptedBytes.length);


            // Create cipher
            Cipher cipher =
                    Cipher.getInstance(ALGORITHM);

            GCMParameterSpec parameterSpec =
                    new GCMParameterSpec(
                            GCM_TAG_LENGTH,
                            iv);

            cipher.init(
                    Cipher.DECRYPT_MODE,
                    secretKey,
                    parameterSpec);


            // Decrypt
            byte[] decryptedBytes =
                    cipher.doFinal(
                            encryptedBytes);


            return new String(
                    decryptedBytes,
                    StandardCharsets.UTF_8);

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Unable to decrypt data.",
                    e);
        }
    }
}