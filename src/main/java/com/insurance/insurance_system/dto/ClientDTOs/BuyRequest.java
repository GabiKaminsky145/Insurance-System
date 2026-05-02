package com.insurance.insurance_system.dto.ClientDTOs;

import com.insurance.insurance_system.model.ContactMethod;

public record BuyRequest(
        ContactMethod.Type authMethodType,
        String authMethodValue
) {}
