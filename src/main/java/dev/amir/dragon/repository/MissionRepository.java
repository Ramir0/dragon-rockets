package dev.amir.dragon.repository;

import dev.amir.dragon.model.Mission;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Mission entities.
 */
public interface MissionRepository {
    Mission save(Mission mission);

    Optional<Mission> findById(String id);

    Optional<Mission> findByRocketId(String rocketId);

    List<Mission> findAll();
}
