package com.insurance.insurance_system.exceptions.ProductExceptions;

public class DuplicateProductException extends RuntimeException {
    public DuplicateProductException(String productId) {
        super("Product already exists with id: " + productId);
    }

    public DuplicateProductException(String productId, String clientId) {
        super("Client " + clientId + " already owns product: " + productId);
    }
}