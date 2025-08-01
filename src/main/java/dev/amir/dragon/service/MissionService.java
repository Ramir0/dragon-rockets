package dev.amir.dragon.service;

import dev.amir.dragon.model.Mission;
import dev.amir.dragon.model.MissionStatus;
import dev.amir.dragon.model.Rocket;
import dev.amir.dragon.model.RocketStatus;
import dev.amir.dragon.repository.MissionRepository;
import dev.amir.dragon.repository.RocketRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service class to perform mission operations.
 */
public class MissionService {
    private final MissionRepository missionRepository;
    private final RocketRepository rocketRepository;

    public MissionService(MissionRepository missionRepository, RocketRepository rocketRepository) {
        this.missionRepository = missionRepository;
        this.rocketRepository = rocketRepository;
    }

    public Mission create(String name) {
        Mission newMission = new Mission();
        newMission.setName(name);
        newMission.setStatus(MissionStatus.SCHEDULED);
        return missionRepository.save(newMission);
    }

    public boolean changeStatus(String missionId, MissionStatus newStatus) {
        Mission existingMission = missionRepository.findById(missionId)
                .orElseThrow(() -> new IllegalStateException(String.format("Mission with ID: %s was not found", missionId)));
        validateStatusTransition(existingMission, newStatus);
        existingMission.setStatus(newStatus);
        Mission modifiedMission = missionRepository.save(existingMission);
        return modifiedMission != null;
    }

    public boolean assignRocketToMission(String missionId, String rocketId) {
        Optional<Mission> existingMission = missionRepository.findByRocketId(rocketId);
        if (existingMission.isPresent()) {
            return false;
        }

        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new IllegalStateException(String.format("Mission with ID: %s was not found", missionId)));
        if (mission.getStatus() == MissionStatus.ENDED) {
            throw new IllegalStateException("Cannot assign rockets to an ended mission");
        }
        Rocket rocket = rocketRepository.findById(rocketId)
                .orElseThrow(() -> new IllegalStateException(String.format("Rocket with ID: %s was not found", rocketId)));

        mission.addRockets(List.of(rocket));
        updateMissionStatusBasedOnRockets(mission);
        Mission modifiedMission = missionRepository.save(mission);
        return modifiedMission != null;
    }

    private void validateStatusTransition(Mission mission, MissionStatus newStatus) {
        List<Rocket> rockets = mission.getRockets();

        switch (newStatus) {
            case SCHEDULED -> {
                if (!rockets.isEmpty()) {
                    throw new IllegalStateException("Cannot set mission to SCHEDULED when rockets are assigned");
                }
            }
            case PENDING -> {
                if (rockets.isEmpty()) {
                    throw new IllegalStateException("Cannot set mission to PENDING without assigned rockets");
                }
            }
            case IN_PROGRESS -> {
                if (rockets.isEmpty()) {
                    throw new IllegalStateException("Cannot set mission to IN_PROGRESS without assigned rockets");
                }
                if (hasRocketsInRepair(rockets)) {
                    throw new IllegalStateException("Cannot set mission to IN_PROGRESS when rockets are in repair");
                }
            }
            case ENDED -> {
                if (!rockets.isEmpty()) {
                    throw new IllegalStateException("Cannot set mission to ENDED when rockets are assigned");
                }
            }
        }
    }

    private void updateMissionStatusBasedOnRockets(Mission mission) {
        List<Rocket> rockets = mission.getRockets();
        if (hasRocketsInRepair(rockets)) {
            mission.setStatus(MissionStatus.PENDING);
        } else {
            mission.setStatus(MissionStatus.IN_PROGRESS);
        }
    }

    private boolean hasRocketsInRepair(List<Rocket> rockets) {
        return rockets.stream()
                .anyMatch(rocket -> rocket.getStatus() == RocketStatus.IN_REPAIR);
    }
}
