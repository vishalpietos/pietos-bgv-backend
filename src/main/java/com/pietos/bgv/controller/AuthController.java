package com.pietos.bgv.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pietos.bgv.dto.request.LoginRequest;
import com.pietos.bgv.dto.response.LoginResponse;
import com.pietos.bgv.service.AuthService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
@Tag(
	    name = "Authentication",
	    description = "Authentication and authorization APIs"
	)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}