package com.pietos.bgv.security;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.repository.SystemUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final SystemUserRepository userRepository;

    public CustomUserDetailsService(
            SystemUserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        SystemUser user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "User not found with email : "
                                                + email));

        // =====================================================
        // CHECK USER STATUS
        // =====================================================

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new UsernameNotFoundException(
                    "User account is inactive.");
        }

        // =====================================================
        // PRIMARY ROLE FROM SYSTEM_USERS
        // =====================================================

        List<SimpleGrantedAuthority> authorities =
                user.getRole() != null
                        ? List.of(
                                new SimpleGrantedAuthority(
                                        user.getRole().getRoleName()
                                )
                        )
                        : Collections.emptyList();

        // =====================================================
        // DEBUG
        // =====================================================

        System.out.println(
                "User Authorities: " + authorities
        );

        // =====================================================
        // CREATE USER DETAILS
        // =====================================================

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}