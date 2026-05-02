package com.insurance.insurance_system;

import com.insurance.insurance_system.exceptions.ProductExceptions.ProductNotFoundException;
import com.insurance.insurance_system.repositories.ProductRepository;
import com.insurance.insurance_system.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private ProductService service;

    @BeforeEach
    void setup() {
        service = new ProductService(new ProductRepository());
    }

    @Test
    void shouldCreateProduct() {
        var product = service.create("p1", "prod", "desc");

        assertEquals("p1", product.getId());
        assertEquals("prod", product.getName());
    }

    @Test
    void shouldUpdateProduct() {
        service.create("p1", "prod", "desc");

        var updated = service.update("p1", "new", "newDesc");

        assertEquals("new", updated.getName());
        assertEquals("newDesc", updated.getDescription());
    }

    @Test
    void shouldFailUpdatingNonExistingProduct() {
        assertThrows(ProductNotFoundException.class, () ->
                service.update("p999", "name", "desc"));
    }

    @Test
    void shouldGetProduct() {
        service.create("p1", "prod", "desc");

        var product = service.get("p1");

        assertEquals("p1", product.getId());
    }

    @Test
    void shouldFailGettingNonExistingProduct() {
        assertThrows(ProductNotFoundException.class, () ->
                service.get("p999"));
    }
}
