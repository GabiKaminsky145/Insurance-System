package com.insurance.insurance_system.exceptions.ClientExceptions;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(String id) {
        super("Client: " + id + " not found");
    }
}
