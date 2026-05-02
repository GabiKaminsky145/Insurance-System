package com.insurance.insurance_system.services;

import com.insurance.insurance_system.dto.ClientDTOs.ConnectResult;
import com.insurance.insurance_system.dto.ClientDTOs.IdentifyRequest;
import com.insurance.insurance_system.exceptions.ClientExceptions.ClientNotFoundException;
import com.insurance.insurance_system.exceptions.ClientExceptions.UnauthorizedException;
import com.insurance.insurance_system.exceptions.ProductExceptions.DuplicateProductException;
import com.insurance.insurance_system.exceptions.ProductExceptions.ProductNotFoundException;
import com.insurance.insurance_system.model.Client;
import com.insurance.insurance_system.model.ContactMethod;
import com.insurance.insurance_system.model.Product;
import com.insurance.insurance_system.repositories.ClientRepository;
import com.insurance.insurance_system.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    public ConnectResult identifyOrCreate(String clientId,
                                          String name,
                                          List<IdentifyRequest.ContactMethodDto> contacts,
                                          ContactMethod.Type authType,
                                          String authValue) {

        validateAuth(authType, authValue);
        Client client = clientRepository.findById(clientId)
                .orElseGet(() -> createNewClient(clientId, name, contacts));

        if (!isAuthorized(client, authType, authValue)) {
            throw new UnauthorizedException(clientId);
        }

        return new ConnectResult(clientId, client.getProducts());
    }

    public void buyProduct(String clientId,
                           ContactMethod.Type type,
                           String value,
                           String productId) {

        validateAuth(type, value);

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException(clientId));

        if (!isAuthorized(client, type, value)) {
            throw new UnauthorizedException(clientId);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (!client.getProducts().add(product)) {
            throw new DuplicateProductException(productId, clientId);
        }
    }

    // ---------- helpers ----------

    private Client createNewClient(String id, String name, List<IdentifyRequest.ContactMethodDto> contacts) {
        if (name == null || contacts == null || contacts.isEmpty()) {
            throw new IllegalArgumentException("New client requires name and contacts");
        }

        Client client = new Client(id, name);
        contacts.forEach(dto ->
                client.getContactMethods().add(new ContactMethod(dto.type(), dto.value()))
        );

        return clientRepository.save(client);
    }

    private boolean isAuthorized(Client client,
                                 ContactMethod.Type type,
                                 String value) {

        return client.getContactMethods().stream()
                .anyMatch(cm ->
                        cm.getType() == type &&
                                Objects.equals(cm.getValue(), value)
                );
    }

    private void validateAuth(ContactMethod.Type type, String value) {
        if (type == null || value == null) {
            throw new IllegalArgumentException("Authentication fields are required");
        }
    }
}