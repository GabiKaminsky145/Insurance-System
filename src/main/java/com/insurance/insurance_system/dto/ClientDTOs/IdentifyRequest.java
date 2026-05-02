package com.insurance.insurance_system.dto.ClientDTOs;

import com.insurance.insurance_system.model.ContactMethod;

import java.util.List;

public record IdentifyRequest(
        String clientId,
        String name,
        List<ContactMethodDto> contactMethods,
        ContactMethod.Type authMethodType,
        String authMethodValue
) {
    public record ContactMethodDto(ContactMethod.Type type, String value) {}
}