package dev.amir.dragon.repository;

import dev.amir.dragon.model.Mission;
import dev.amir.dragon.model.Rocket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class MissionInMemoryRepository implements MissionRepository {
    private final Map<String, Mission> memory = new HashMap<>();

    @Override
    public Mission save(Mission mission) {
        return mission.getId() == null ? insert(mission) : update(mission);
    }

    @Override
    public Mission getById(String id) {
        return null;
    }

    @Override
    public Mission getByRocketId(String rocketId) {
        return null;
    }

    @Override
    public List<Mission> getAll() {
        return List.of();
    }

    private Mission update(Mission mission) {
        if (!memory.containsKey(mission.getId())) {
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
        memory.put(savedMission.getId(), savedMission);
        return savedMission;
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
