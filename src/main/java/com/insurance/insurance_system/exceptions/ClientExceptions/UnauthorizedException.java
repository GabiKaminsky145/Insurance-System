package com.insurance.insurance_system.exceptions.ClientExceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String clientId) {
        super("Authentication failed for client " + clientId);
    }
}
