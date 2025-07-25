package dev.amir.dragon.repository;

import dev.amir.dragon.model.Mission;

import java.util.List;

/**
 * Repository interface for managing Mission entities.
 */
public interface MissionRepository {
    Mission save(Mission mission);

    Mission getById(String id);

    Mission getByRocketId(String rocketId);

    List<Mission> getAll();
}
