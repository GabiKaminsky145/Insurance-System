package com.insurance.insurance_system.dto.ProductDTOs;

public record CreateProductRequest(
        String id,
        String name,
        String description
) {}
