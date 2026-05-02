package com.insurance.insurance_system.services;

import com.insurance.insurance_system.exceptions.ProductExceptions.DuplicateProductException;
import com.insurance.insurance_system.exceptions.ProductExceptions.ProductNotFoundException;
import com.insurance.insurance_system.model.Product;
import com.insurance.insurance_system.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product create(String id, String name, String desc) {
        if (productRepository.findById(id).isPresent()) {
            throw new DuplicateProductException(id);
        }
        return productRepository.save(new Product(id, name, desc));
    }

    public Product update(String id, String name, String desc) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        p.setName(name);
        p.setDescription(desc);

        return productRepository.save(p);
    }

    public Product get(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Collection<Product> getAll() {
        return productRepository.findAll();
    }
}