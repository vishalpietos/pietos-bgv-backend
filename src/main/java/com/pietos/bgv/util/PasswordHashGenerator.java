package com.pietos.bgv.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder();

        String password = "Admin@123";

        String encodedPassword = encoder.encode(password);

        System.out.println("Encoded Password: " + encodedPassword);
    }
}