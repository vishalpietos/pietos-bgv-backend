package com.pietos.bgv.util;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {

    public static void main(String[] args) {

        // AES-256 = 32 bytes
        byte[] key = new byte[32];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(key);

        String encodedKey =
                Base64.getEncoder().encodeToString(key);

        System.out.println("Generated Encryption Key:");
        System.out.println(encodedKey);
    }
}