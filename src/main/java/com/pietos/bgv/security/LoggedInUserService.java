package com.pietos.bgv.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.pietos.bgv.entity.SystemUser;
import com.pietos.bgv.exception.ResourceNotFoundException;
import com.pietos.bgv.repository.SystemUserRepository;



//SystemUser loggedInUser = loggedInUserService.getLoggedInUser();
@Service
public class LoggedInUserService {

    private final SystemUserRepository systemUserRepository;

    public LoggedInUserService(SystemUserRepository systemUserRepository) {
        this.systemUserRepository = systemUserRepository;
    }

    public SystemUser getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {

            throw new ResourceNotFoundException("No authenticated user found.");
        }

        return systemUserRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Logged in user not found."));
    }
}