package dev.amir.dragon.repository;

import dev.amir.dragon.model.Rocket;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class RocketInMemoryRepository implements RocketRepository {
    private final Map<String, Rocket> memory = new HashMap<>();

    @Override
    public Rocket save(Rocket rocket) {
        return rocket.getId() == null ? insert(rocket) : update(rocket);
    }

    @Override
    public Optional<Rocket> findById(String id) {
        Rocket inMemoryRocket = memory.get(id);
        if (inMemoryRocket == null) {
            return Optional.empty();
        }
        Rocket copyOfRocket = new Rocket();
        copyOfRocket.setId(inMemoryRocket.getId());
        copyOfRocket.setName(inMemoryRocket.getName());
        copyOfRocket.setStatus(inMemoryRocket.getStatus());
        return Optional.of(copyOfRocket);
    }

    private Rocket update(Rocket rocket) {
        if (!memory.containsKey(rocket.getId())) {
            throw new IllegalArgumentException(String.format("Rocket with ID: %s does not exist", rocket.getId()));
        }
        Rocket savedRocket = new Rocket();
        savedRocket.setId(rocket.getId());
        savedRocket.setName(rocket.getName());
        savedRocket.setStatus(rocket.getStatus());
        memory.put(savedRocket.getId(), savedRocket);
        return savedRocket;
    }

    private Rocket insert(Rocket rocket) {
        Rocket savedRocket = new Rocket();
        savedRocket.setId(UUID.randomUUID().toString());
        savedRocket.setName(rocket.getName());
        savedRocket.setStatus(rocket.getStatus());
        memory.put(savedRocket.getId(), savedRocket);
        return savedRocket;
    }
}
