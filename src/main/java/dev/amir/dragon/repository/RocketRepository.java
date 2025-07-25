package dev.amir.dragon.repository;

import dev.amir.dragon.model.Rocket;

import java.util.Optional;

/**
 * Repository interface for managing Rocket entities.
 */
public interface RocketRepository {
    Rocket save(Rocket rocket);

    Optional<Rocket> findById(String id);
}
