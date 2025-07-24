package dev.amir.dragon.repository;

import dev.amir.dragon.model.Rocket;

/**
 * Repository interface for managing Rocket entities.
 */
public interface RocketRepository {
    Rocket save(Rocket rocket);

    Rocket getById(String id);
}
