package dev.amir.dragon.repository;

import dev.amir.dragon.model.Mission;

public interface MissionRepository {
    Mission save(Mission mission);

    Mission getById(String id);

    Mission getByRocketId(String rocketId);
}
