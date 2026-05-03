package com.insurance.insurance_system.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthService {

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    public void validate(String username, String password) {
        if (!adminUsername.equals(username) || !adminPassword.equals(password)) {
            throw new SecurityException("Admin authentication failed");
        }
    }
}