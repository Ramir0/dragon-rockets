package dev.amir.dragon.service;

import dev.amir.dragon.model.Mission;
import dev.amir.dragon.model.MissionStatus;
import dev.amir.dragon.repository.MissionRepository;

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
}
