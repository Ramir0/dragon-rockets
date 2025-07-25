package dev.amir.dragon.repository;

import dev.amir.dragon.model.Mission;
import dev.amir.dragon.model.MissionStatus;
import dev.amir.dragon.model.Rocket;
import dev.amir.dragon.model.RocketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MissionInMemoryRepositoryTest {
    private MissionRepository missionRepository;

    @BeforeEach
    void setUp() {
        missionRepository = new MissionInMemoryRepository();
    }

    @Test
    @DisplayName("Should insert a new mission")
    void shouldInsertANewMission() {
        // Given
        Mission newMission = new Mission();
        newMission.setName("NewMission");
        newMission.setStatus(MissionStatus.SCHEDULED);

        // When
        Mission savedMission = missionRepository.save(newMission);

        // Then
        assertNotNull(savedMission.getId());
        assertEquals(newMission.getName(), savedMission.getName());
        assertEquals(newMission.getStatus(), savedMission.getStatus());
        assertTrue(savedMission.getRockets().isEmpty());
    }

    @Test
    @DisplayName("Should update an existing mission")
    void shouldUpdateAnExistingMission() {
        // Given
        Mission newMission = new Mission();
        newMission.setName("NewMission");
        newMission.setStatus(MissionStatus.SCHEDULED);
        Mission savedMission = missionRepository.save(newMission);
        savedMission.setStatus(MissionStatus.ENDED);

        // When
        Mission updatedMission = missionRepository.save(savedMission);

        // Then
        assertEquals(savedMission.getId(), updatedMission.getId());
        assertEquals(savedMission.getName(), updatedMission.getName());
        assertEquals(savedMission.getStatus(), updatedMission.getStatus());
        assertTrue(updatedMission.getRockets().isEmpty());
    }

    @Test
    @DisplayName("Should throw exception when mission ID does not exist")
    void shouldThrowExceptionWhenMissionIdDoesNotExist() {
        // Given
        String fakeMissionId = UUID.randomUUID().toString();
        Mission newMission = new Mission();
        newMission.setId(fakeMissionId);
        newMission.setName("NewMission");
        newMission.setStatus(MissionStatus.SCHEDULED);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> missionRepository.save(newMission));
        assertEquals(String.format("Mission with ID: %s does not exist", fakeMissionId), exception.getMessage());
    }

    @Test
    @DisplayName("Should find a mission by ID")
    void shouldFindAMissionById() {
        // Given
        Mission newMission = new Mission();
        newMission.setName("NewMission");
        newMission.setStatus(MissionStatus.SCHEDULED);
        Mission savedMission = missionRepository.save(newMission);

        // When
        Optional<Mission> foundMission = missionRepository.findById(savedMission.getId());

        // Then
        assertTrue(foundMission.isPresent());
        assertEquals(savedMission.getId(), foundMission.get().getId());
    }

    @Test
    @DisplayName("Should return empty when mission ID does not exist")
    void shouldReturnEmptyWhenMissionIdDoesNotExist() {
        // Given
        Mission newMission = new Mission();
        newMission.setName("NewMission");
        newMission.setStatus(MissionStatus.SCHEDULED);
        missionRepository.save(newMission);

        // When
        Optional<Mission> foundMission = missionRepository.findById(UUID.randomUUID().toString());

        // Then
        assertTrue(foundMission.isEmpty());
    }

    @Test
    @DisplayName("Should find a mission by rocket ID")
    void shouldFindAMissionByRocketId() {
        // Given
        Rocket rocket = new Rocket();
        rocket.setId(UUID.randomUUID().toString());
        rocket.setName("NewRocket");
        rocket.setStatus(RocketStatus.ON_GROUND);
        Mission newMission = new Mission();
        newMission.setName("NewMission");
        newMission.setStatus(MissionStatus.SCHEDULED);
        newMission.addRockets(List.of(rocket));
        Mission savedMission = missionRepository.save(newMission);

        // When
        Optional<Mission> foundMission = missionRepository.findByRocketId(rocket.getId());

        // Then
        assertTrue(foundMission.isPresent());
        assertEquals(savedMission.getId(), foundMission.get().getId());
    }

    @Test
    @DisplayName("Should return empty when rocket ID does not exist")
    void shouldReturnEmptyWhenRocketIdDoesNotExist() {
        // Given
        Mission newMission = new Mission();
        newMission.setName("NewMission");
        newMission.setStatus(MissionStatus.SCHEDULED);
        missionRepository.save(newMission);

        // When
        Optional<Mission> foundMission = missionRepository.findByRocketId(UUID.randomUUID().toString());

        // Then
        assertTrue(foundMission.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list when there is no saved mission")
    void shouldReturnEmptyListWhenThereIsNoSavedMission() {
        // When
        List<Mission> allMissions = missionRepository.findAll();

        // Then
        assertTrue(allMissions.isEmpty());
    }

    @Test
    @DisplayName("Should find all saved mission")
    void shouldFindAllSavedMission() {
        // Given
        Mission mission1 = new Mission();
        mission1.setName("NewMission1");
        mission1.setStatus(MissionStatus.SCHEDULED);
        missionRepository.save(mission1);
        Mission mission2 = new Mission();
        mission2.setName("NewMission2");
        mission2.setStatus(MissionStatus.ENDED);
        missionRepository.save(mission2);

        // When
        List<Mission> allMissions = missionRepository.findAll();

        // Then
        assertEquals(2, allMissions.size());
        assertTrue(allMissions.stream().map(Mission::getName)
                .anyMatch(missionName -> missionName.equals(mission1.getName())));
        assertTrue(allMissions.stream().map(Mission::getName)
                .anyMatch(missionName -> missionName.equals(mission2.getName())));
    }
}
