package dev.amir.dragon.service;

import dev.amir.dragon.dto.MissionSummary;
import dev.amir.dragon.dto.RocketSummary;
import dev.amir.dragon.model.Mission;
import dev.amir.dragon.model.Rocket;
import dev.amir.dragon.repository.MissionRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class to summarize the missions
 */
public class MissionSummaryService {
    private final MissionRepository missionRepository;

    public MissionSummaryService(MissionRepository missionRepository) {
        this.missionRepository = missionRepository;
    }

    public List<MissionSummary> generateMissionSummary() {
        return missionRepository.findAll().stream()
                .map(this::convertToMissionSummary)
                .sorted(this::compareMissions)
                .collect(Collectors.toList());
    }

    private MissionSummary convertToMissionSummary(Mission mission) {
        List<RocketSummary> rocketSummaries = mission.getRockets().stream()
                .map(this::convertToRocketSummary)
                .sorted(Comparator.comparing(RocketSummary::name))
                .collect(Collectors.toList());

        return new MissionSummary(mission.getName(), mission.getStatus(), rocketSummaries);
    }

    private RocketSummary convertToRocketSummary(Rocket rocket) {
        return new RocketSummary(rocket.getName(), rocket.getStatus());
    }

    private int compareMissions(MissionSummary mission1, MissionSummary mission2) {
        int rocketsNumberComparison = Integer.compare(mission2.rockets().size(), mission1.rockets().size());
        if (rocketsNumberComparison != 0) {
            return rocketsNumberComparison;
        }

        // If same rockets number, compare by name alphabetically
        return mission1.name().compareToIgnoreCase(mission2.name());
    }
}
