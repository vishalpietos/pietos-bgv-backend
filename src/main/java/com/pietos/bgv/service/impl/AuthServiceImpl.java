package com.pietos.bgv.service.impl;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pietos.bgv.dto.request.LoginRequest;
import com.pietos.bgv.dto.response.LoginResponse;
import com.pietos.bgv.entity.InternalUserRole;
import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.repository.InternalUserRoleRepository;
import com.pietos.bgv.repository.SystemUserRepository;
import com.pietos.bgv.security.CustomUserDetailsService;
import com.pietos.bgv.security.JwtService;
import com.pietos.bgv.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final SystemUserRepository userRepository;
    private final InternalUserRoleRepository internalUserRoleRepository;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtService jwtService,
                           CustomUserDetailsService customUserDetailsService,
                           SystemUserRepository userRepository,
                           InternalUserRoleRepository internalUserRoleRepository) {

        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.userRepository = userRepository;
        this.internalUserRoleRepository = internalUserRoleRepository;
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        SystemUser user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

       

        // Authenticate email and password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(request.getEmail());

        String token = jwtService.generateToken(userDetails);
        
        List<String> roles =
                internalUserRoleRepository
                        .findBySystemUserIdAndIsActiveTrue(user.getId())
                        .stream()
                        .map(InternalUserRole::getRole)
                        .map(role -> role.getRoleName())
                        .toList();
        
        
        

        LoginResponse response = new LoginResponse();

        response.setToken(token);
        response.setType("Bearer");
        response.setUserId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().getRoleName());
        
        response.setRoles(roles);
        return response;
    }
}