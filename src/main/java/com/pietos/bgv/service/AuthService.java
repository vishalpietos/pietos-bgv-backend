package com.pietos.bgv.service;

import com.pietos.bgv.dto.request.LoginRequest;
import com.pietos.bgv.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

}