package com.insurance.insurance_system.controllers;

import com.insurance.insurance_system.dto.ProductDTOs.CreateProductRequest;
import com.insurance.insurance_system.dto.ProductDTOs.UpdateProductRequest;
import com.insurance.insurance_system.model.Product;
import com.insurance.insurance_system.services.AdminAuthService;
import com.insurance.insurance_system.services.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Tag(name = "Admin Product API", description = "Admin operations — manage product catalog")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;
    private final AdminAuthService adminAuthService;

    @PostMapping
    public ResponseEntity<Product> create(
            @RequestHeader("X-Admin-Username") String username,
            @RequestHeader("X-Admin-Password") String password,
            @RequestBody CreateProductRequest req) {

        adminAuthService.validate(username, password);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(req.id(), req.name(), req.description()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(
            @RequestHeader("X-Admin-Username") String username,
            @RequestHeader("X-Admin-Password") String password,
            @PathVariable String id,
            @RequestBody UpdateProductRequest req) {

        adminAuthService.validate(username, password);
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
