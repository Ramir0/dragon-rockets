package dev.amir.dragon.service;

import dev.amir.dragon.model.Mission;
import dev.amir.dragon.model.MissionStatus;
import dev.amir.dragon.model.Rocket;
import dev.amir.dragon.model.RocketStatus;
import dev.amir.dragon.repository.MissionRepository;

import java.util.List;

/**
 * Service class to perform mission operations.
 */
public class MissionService {
    private final MissionRepository missionRepository;

    public MissionService(MissionRepository missionRepository) {
        this.missionRepository = missionRepository;
    }

    public Mission create(String name) {
        Mission newMission = new Mission();
        newMission.setName(name);
        newMission.setStatus(MissionStatus.SCHEDULED);
        return missionRepository.save(newMission);
    }

    public boolean changeStatus(String missionId, MissionStatus newStatus) {
        Mission existingMission = missionRepository.getById(missionId);
        validateStatusTransition(existingMission, newStatus);
        existingMission.setStatus(newStatus);
        Mission modifiedMission = missionRepository.save(existingMission);
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

    private boolean hasRocketsInRepair(List<Rocket> rockets) {
        return rockets.stream()
                .anyMatch(rocket -> rocket.getStatus() == RocketStatus.IN_REPAIR);
    }
}
