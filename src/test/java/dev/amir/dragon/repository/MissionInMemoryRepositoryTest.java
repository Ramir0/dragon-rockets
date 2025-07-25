package dev.amir.dragon.repository;

import dev.amir.dragon.model.Mission;
import dev.amir.dragon.model.MissionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}