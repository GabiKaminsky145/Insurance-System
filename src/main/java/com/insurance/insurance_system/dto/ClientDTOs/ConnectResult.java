package com.insurance.insurance_system.dto.ClientDTOs;

import com.insurance.insurance_system.model.Product;

import java.util.Set;

public record ConnectResult(
        String clientId,
        Set<Product> products
) {}