package dev.amir.dragon.repository;

import dev.amir.dragon.model.Mission;
import dev.amir.dragon.model.Rocket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class MissionInMemoryRepository implements MissionRepository {
    private final Map<String, Mission> missionById = new HashMap<>();
    private final Map<String, Mission> missionByRocketId = new HashMap<>();

    @Override
    public Mission save(Mission mission) {
        return mission.getId() == null ? insert(mission) : update(mission);
    }

    @Override
    public Optional<Mission> findById(String id) {
        return findBy(missionById.get(id));
    }

    @Override
    public Optional<Mission> findByRocketId(String rocketId) {
        return findBy(missionByRocketId.get(rocketId));
    }

    @Override
    public List<Mission> findAll() {
        return missionById.values().stream().map(this::copyMission).collect(Collectors.toList());
    }

    private Mission update(Mission mission) {
        if (!missionById.containsKey(mission.getId())) {
            throw new IllegalArgumentException(String.format("Mission with ID: %s does not exist", mission.getId()));
        }
        return save(mission.getId(), mission);
    }

    private Mission insert(Mission mission) {
        return save(UUID.randomUUID().toString(), mission);
    }

    private Mission save(String id, Mission mission) {
        Mission savedMission = new Mission();
        savedMission.setId(id);
        savedMission.setName(mission.getName());
        savedMission.setStatus(mission.getStatus());
        savedMission.addRockets(copyRockets(mission.getRockets()));
        missionById.put(savedMission.getId(), savedMission);
        savedMission.getRockets().forEach(rocket -> missionByRocketId.put(rocket.getId(), savedMission));
        return savedMission;
    }

    private Optional<Mission> findBy(Mission inMemoryMission) {
        if (inMemoryMission == null) {
            return Optional.empty();
        }
        return Optional.of(copyMission(inMemoryMission));
    }

    private Mission copyMission(Mission mission) {
        Mission copyOfMission = new Mission();
        copyOfMission.setId(mission.getId());
        copyOfMission.setName(mission.getName());
        copyOfMission.setStatus(mission.getStatus());
        copyOfMission.addRockets(copyRockets(mission.getRockets()));
        return copyOfMission;
    }

    private List<Rocket> copyRockets(List<Rocket> rockets) {
        return rockets.stream().map(rocket -> {
            Rocket savedRocket = new Rocket();
            savedRocket.setId(rocket.getId());
            savedRocket.setName(rocket.getName());
            savedRocket.setStatus(rocket.getStatus());
            return savedRocket;
        }).collect(Collectors.toList());
    }
}
