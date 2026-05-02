package com.insurance.insurance_system.repositories;

import com.insurance.insurance_system.model.Client;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ClientRepository {

    private final ConcurrentHashMap<String, Client> store = new ConcurrentHashMap<>();

    public Optional<Client> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public Client save(Client client) {
        store.put(client.getId(), client);
        return client;
    }
}
