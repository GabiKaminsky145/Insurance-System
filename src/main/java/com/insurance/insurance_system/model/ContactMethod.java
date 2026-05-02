package com.insurance.insurance_system.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactMethod {

    private Type type;
    private String value;

    public enum Type {
        EMAIL,
        PHONE,
        MAIL
    }
}
