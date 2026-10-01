package com.pietos.bgv.util;

import java.security.SecureRandom;

public class PasswordGenerator {
	
	public static final int DEFAULT_PASSWORD_LENGTH = 10;
    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz"
            + "0123456789"
            + "@#$%&*";

    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordGenerator() {
        // Prevent object creation
    }

    public static String generatePassword(int length) {

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(
                    CHARACTERS.charAt(
                            RANDOM.nextInt(CHARACTERS.length())));
        }

        return password.toString();
    }
}