package com.insurance.insurance_system;

import com.insurance.insurance_system.dto.ClientDTOs.IdentifyRequest;
import com.insurance.insurance_system.exceptions.ClientExceptions.ClientNotFoundException;
import com.insurance.insurance_system.exceptions.ClientExceptions.UnauthorizedException;
import com.insurance.insurance_system.exceptions.ProductExceptions.DuplicateProductException;
import com.insurance.insurance_system.exceptions.ProductExceptions.ProductNotFoundException;
import com.insurance.insurance_system.model.ContactMethod;
import com.insurance.insurance_system.model.Product;
import com.insurance.insurance_system.repositories.ClientRepository;
import com.insurance.insurance_system.repositories.ProductRepository;
import com.insurance.insurance_system.services.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientServiceTest {

    private ClientService service;
    private ProductRepository productRepo;

    @BeforeEach
    void setup() {
        ClientRepository clientRepo = new ClientRepository();
        productRepo = new ProductRepository();
        service = new ClientService(clientRepo, productRepo);
    }

    private List<IdentifyRequest.ContactMethodDto> defaultContacts() {
        return List.of(new IdentifyRequest.ContactMethodDto(ContactMethod.Type.EMAIL, "test@mail.com"));
    }

    // -------------------------
    // IDENTIFY / CREATE FLOW
    // -------------------------

    @Test
    void shouldCreateAndAuthenticateNewClient() {
        var result = service.identifyOrCreate(
                "1",
                "John",
                defaultContacts(),
                ContactMethod.Type.EMAIL,
                "test@mail.com"
        );

        assertEquals("1", result.clientId());
        assertTrue(result.products().isEmpty());
    }

    @Test
    void shouldFailCreatingClientWithoutName() {
        assertThrows(IllegalArgumentException.class, () ->
                service.identifyOrCreate(
                        "1",
                        null,
                        defaultContacts(),
                        ContactMethod.Type.EMAIL,
                        "test@mail.com"
                ));
    }

    @Test
    void shouldFailCreatingClientWithoutContacts() {
        assertThrows(IllegalArgumentException.class, () ->
                service.identifyOrCreate(
                        "1",
                        "John",
                        null,
                        ContactMethod.Type.EMAIL,
                        "test@mail.com"
                ));
    }

    @Test
    void shouldAuthenticateExistingClient() {
        // First call creates the client
        service.identifyOrCreate("1", "John", defaultContacts(),
                ContactMethod.Type.EMAIL, "test@mail.com");

        var result = service.identifyOrCreate(
                "1",
                null,
                null,
                ContactMethod.Type.EMAIL,
                "test@mail.com"
        );

        assertEquals("1", result.clientId());
    }

    @Test
    void shouldFailAuthenticationWithWrongValue() {
        service.identifyOrCreate("1", "John", defaultContacts(),
                ContactMethod.Type.EMAIL, "test@mail.com");

        assertThrows(UnauthorizedException.class, () ->
                service.identifyOrCreate(
                        "1",
                        null,
                        null,
                        ContactMethod.Type.EMAIL,
                        "wrong@mail.com"  // wrong credential
                ));
    }

    @Test
    void shouldFailAuthenticationWithoutAuthFields() {
        assertThrows(IllegalArgumentException.class, () ->
                service.identifyOrCreate(
                        "1",
                        "John",
                        defaultContacts(),
                        null,  // missing auth type
                        null   // missing auth value
                ));
    }

    // -------------------------
    // BUY PRODUCT FLOW
    // -------------------------

    @Test
    void shouldBuyProductSuccessfully() {
        // FIX: Product constructor is now (id, name, description) — no id field removed here,
        // but confirming 3-arg constructor still matches after model cleanup
        productRepo.save(new Product("p1", "prod", "desc"));

        service.identifyOrCreate("1", "John", defaultContacts(),
                ContactMethod.Type.EMAIL, "test@mail.com");

        service.buyProduct("1", ContactMethod.Type.EMAIL, "test@mail.com", "p1");

        var result = service.identifyOrCreate(
                "1",
                null,
                null,
                ContactMethod.Type.EMAIL,
                "test@mail.com"
        );

        assertEquals(1, result.products().size());
    }

    @Test
    void shouldFailBuyingNonExistingProduct() {
        service.identifyOrCreate("1", "John", defaultContacts(),
                ContactMethod.Type.EMAIL, "test@mail.com");

        assertThrows(ProductNotFoundException.class, () ->
                service.buyProduct("1", ContactMethod.Type.EMAIL, "test@mail.com", "p999"));
    }

    @Test
    void shouldFailBuyingWithWrongAuth() {
        productRepo.save(new Product("p1", "prod", "desc"));

        service.identifyOrCreate("1", "John", defaultContacts(),
                ContactMethod.Type.EMAIL, "test@mail.com");

        assertThrows(UnauthorizedException.class, () ->
                service.buyProduct("1", ContactMethod.Type.EMAIL, "wrong@mail.com", "p1"));
    }

    @Test
    void shouldFailDuplicateProductPurchase() {
        productRepo.save(new Product("p1", "prod", "desc"));

        service.identifyOrCreate("1", "John", defaultContacts(),
                ContactMethod.Type.EMAIL, "test@mail.com");

        service.buyProduct("1", ContactMethod.Type.EMAIL, "test@mail.com", "p1");

        assertThrows(DuplicateProductException.class, () ->
                service.buyProduct("1", ContactMethod.Type.EMAIL, "test@mail.com", "p1"));
    }

    @Test
    void shouldFailBuyingForNonExistingClient() {
        productRepo.save(new Product("p1", "prod", "desc"));

        assertThrows(ClientNotFoundException.class, () ->
                service.buyProduct("unknown", ContactMethod.Type.EMAIL, "test@mail.com", "p1"));
    }
}