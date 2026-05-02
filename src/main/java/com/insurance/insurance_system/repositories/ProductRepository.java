package com.insurance.insurance_system.repositories;

import com.insurance.insurance_system.model.Product;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ProductRepository {

    private final ConcurrentHashMap<String, Product> store = new ConcurrentHashMap<>();

    public Optional<Product> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public Product save(Product product) {
        store.put(product.getId(), product);
        return product;
    }

    public Collection<Product> findAll() {
        return store.values();
    }
}