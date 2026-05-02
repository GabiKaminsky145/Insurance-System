package com.insurance.insurance_system.exceptions.ProductExceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String id) {
        super("Product: " + id + " not found");
    }
}
