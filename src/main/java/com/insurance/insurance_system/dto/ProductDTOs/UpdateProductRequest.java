package com.insurance.insurance_system.dto.ProductDTOs;

public record UpdateProductRequest(
        String id,
         String name,
         String description
) {}