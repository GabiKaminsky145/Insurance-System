package com.insurance.insurance_system.controllers;

import com.insurance.insurance_system.dto.ProductDTOs.CreateProductRequest;
import com.insurance.insurance_system.dto.ProductDTOs.UpdateProductRequest;
import com.insurance.insurance_system.model.Product;
import com.insurance.insurance_system.services.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Tag(name = "Product API", description = "Manage products")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody CreateProductRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)  // FIX: was returning 200
                .body(service.create(req.id(), req.name(), req.description()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable String id,
                                          @RequestBody UpdateProductRequest req) {
        return ResponseEntity.ok(service.update(id, req.name(), req.description()));
    }

    @GetMapping
    public ResponseEntity<Collection<Product>> all() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> get(@PathVariable String id) {
        return ResponseEntity.ok(service.get(id));
    }
}
