package dev.amir.dragon.service;

import dev.amir.dragon.dto.MissionSummary;
import dev.amir.dragon.model.Mission;
import dev.amir.dragon.model.MissionStatus;
import dev.amir.dragon.model.Rocket;
import dev.amir.dragon.model.RocketStatus;
import dev.amir.dragon.repository.MissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for Mission Summary Service.
 */
@DisplayName("Mission Summary Service Tests")
@ExtendWith(MockitoExtension.class)
class MissionSummaryServiceTest {

    @Mock
    private MissionRepository missionRepository;

    private MissionSummaryService missionSummaryService;

    @BeforeEach
    void setUp() {
        this.missionSummaryService = new MissionSummaryService(missionRepository);
    }

    @Test
    @DisplayName("Should generate empty summary")
    void shouldGenerateEmptySummary() {
        // Given
        when(missionRepository.findAll()).thenReturn(List.of());

        // When
        List<MissionSummary> summary = missionSummaryService.generateMissionSummary();

        // Then
        assertTrue(summary.isEmpty());
        verify(missionRepository).findAll();
    }

    @Test
    @DisplayName("Should generate summary with correct sorting by rocket number")
    void shouldGenerateSummaryWithCorrectSortingByRocketNumber() {
        // Given
        Mission missionA = buildMission("Luna 2", MissionStatus.SCHEDULED, List.of());
        Rocket rocketB1 = buildRocket("Dragon 1", RocketStatus.ON_GROUND);
        Mission missionB = buildMission("Vertical Landing 1", MissionStatus.IN_PROGRESS, List.of(rocketB1));
        Rocket rocketC1 = buildRocket("Red Dragon 1", RocketStatus.ON_GROUND);
        Rocket rocketC2 = buildRocket("Dragon 2", RocketStatus.IN_SPACE);
        Mission missionC = buildMission("Mars 1", MissionStatus.ENDED, List.of(rocketC1, rocketC2));
        List<Mission> allMissions = List.of(missionA, missionB, missionC);
        when(missionRepository.findAll()).thenReturn(allMissions);

        // When
        List<MissionSummary> summary = missionSummaryService.generateMissionSummary();

        // Then
        verify(missionRepository).findAll();
        assertEquals(3, summary.size());
        // Mission: Mars 1
        MissionSummary missionSummary1 = summary.removeFirst();
        assertEquals(missionC.getName(), missionSummary1.name());
        assertEquals(missionC.getStatus(), missionSummary1.status());
        assertEquals(missionC.getRockets().size(), missionSummary1.rockets().size());
        // Mission: Vertical Landing 1
        MissionSummary missionSummary2 = summary.removeFirst();
        assertEquals(missionB.getName(), missionSummary2.name());
        assertEquals(missionB.getStatus(), missionSummary2.status());
        assertEquals(missionB.getRockets().size(), missionSummary2.rockets().size());
        // Mission: Luna 2
        MissionSummary missionSummary3 = summary.removeFirst();
        assertEquals(missionA.getName(), missionSummary3.name());
        assertEquals(missionA.getStatus(), missionSummary3.status());
        assertEquals(missionA.getRockets().size(), missionSummary3.rockets().size());
    }

    @Test
    @DisplayName("Should generate summary with correct sorting by mission name in descending alphabetical order")
    void shouldGenerateSummaryWithCorrectSortingByMissionNameInDescendingAlphabeticalOrder() {
        // Given
        Rocket rocketA1 = buildRocket("Dragon 1", RocketStatus.ON_GROUND);
        Mission missionA = buildMission("Vertical Landing 1", MissionStatus.SCHEDULED, List.of(rocketA1));
        Rocket rocketB1 = buildRocket("Dragon 2", RocketStatus.IN_SPACE);
        Mission missionB = buildMission("Luna 2", MissionStatus.IN_PROGRESS, List.of(rocketB1));
        Rocket rocketC1 = buildRocket("Red Dragon 1", RocketStatus.ON_GROUND);
        Mission missionC = buildMission("Mars 1", MissionStatus.ENDED, List.of(rocketC1));
        List<Mission> allMissions = List.of(missionA, missionB, missionC);
        when(missionRepository.findAll()).thenReturn(allMissions);

        // When
        List<MissionSummary> summary = missionSummaryService.generateMissionSummary();

        // Then
        verify(missionRepository).findAll();
        assertEquals(3, summary.size());
        // Mission: Luna 2
        MissionSummary missionSummary1 = summary.removeFirst();
        assertEquals(missionA.getName(), missionSummary1.name());
        assertEquals(missionA.getStatus(), missionSummary1.status());
        assertEquals(missionA.getRockets().size(), missionSummary1.rockets().size());
        // Mission: Mars 1
        MissionSummary missionSummary2 = summary.removeFirst();
        assertEquals(missionC.getName(), missionSummary2.name());
        assertEquals(missionC.getStatus(), missionSummary2.status());
        assertEquals(missionC.getRockets().size(), missionSummary2.rockets().size());
        // Mission: Vertical Landing 1
        MissionSummary missionSummary3 = summary.removeFirst();
        assertEquals(missionB.getName(), missionSummary3.name());
        assertEquals(missionB.getStatus(), missionSummary3.status());
        assertEquals(missionB.getRockets().size(), missionSummary3.rockets().size());
    }

    private Mission buildMission(String name, MissionStatus status, List<Rocket> rockets) {
        Mission mission = new Mission();
        mission.setId(UUID.randomUUID().toString());
        mission.setName(name);
        mission.setStatus(status);
        mission.addRockets(rockets);
        return mission;
    }

    private Rocket buildRocket(String name, RocketStatus status) {
        Rocket rocket = new Rocket();
        rocket.setId(UUID.randomUUID().toString());
        rocket.setName(name);
        rocket.setStatus(status);
        return rocket;
    }
}
