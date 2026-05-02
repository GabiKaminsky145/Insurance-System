package com.insurance.insurance_system.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class Client {

    private String id;
    private String name;
    private List<ContactMethod> contactMethods = new ArrayList<>();
    private Set<Product> products = new HashSet<>();

    public Client(String id, String name) {
        this.id = id;
        this.name = name;
    }
}