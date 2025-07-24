package dev.amir.dragon.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for Mission entity.
 */
@DisplayName("Mission Entity Tests")
class MissionTest {
    @Test
    @DisplayName("Should create mission with empty fields")
    void shouldCreateMissionWithEmptyFields() {
        // When
        Mission mission = new Mission();

        // Then
        assertNull(mission.getId());
        assertNull(mission.getName());
        assertNull(mission.getStatus());
        assertTrue(mission.getRockets().isEmpty());
    }

    @Test
    @DisplayName("Should assign valid ID")
    void shouldAssignValidId() {
        // Given
        String missionId = "Mission ID";
        Mission mission = new Mission();

        // When
        mission.setId(missionId);

        // Then
        assertEquals(missionId, mission.getId());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should throw exception when ID is blank")
    void shouldThrowExceptionWhenIdIsBlank(String input) {
        // Given
        Mission mission = new Mission();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mission.setId(input));
        assertEquals("Invalid Mission ID", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when ID exists")
    void shouldThrowExceptionWhenIdExists() {
        // Given
        Mission mission = new Mission();
        String oldId = "Existing Mission ID";
        mission.setId(oldId);
        String newId = "New Mission ID";

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mission.setId(newId));
        assertEquals("Mission ID is already assigned", exception.getMessage());
    }

    @Test
    @DisplayName("Should assign valid Name")
    void shouldAssignValidName() {
        // Given
        String missionName = "Mission Name";
        Mission mission = new Mission();

        // When
        mission.setName(missionName);

        // Then
        assertEquals(missionName, mission.getName());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should throw exception when Name is blank")
    void shouldThrowExceptionWhenNameIsBlank(String input) {
        // Given
        Mission mission = new Mission();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mission.setName(input));
        assertEquals("Invalid Mission Name", exception.getMessage());
    }

    @Test
    @DisplayName("Should assign valid Status")
    void shouldAssignValidStatus() {
        // Given
        MissionStatus missionStatus = MissionStatus.SCHEDULED;
        Mission mission = new Mission();

        // When
        mission.setStatus(missionStatus);

        // Then
        assertEquals(missionStatus, mission.getStatus());
    }

    @Test
    @DisplayName("Should throw exception when Status is null")
    void shouldThrowExceptionWhenStatusIsNull() {
        // Given
        Mission mission = new Mission();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mission.setStatus(null));
        assertEquals("Mission Status can't be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should add valid Rockets")
    void shouldAddValidRockets() {
        // Given
        Collection<Rocket> rockets = List.of(new Rocket());
        Mission mission = new Mission();

        // When
        mission.addRockets(rockets);

        // Then
        assertEquals(rockets, mission.getRockets());
    }

    @Test
    @DisplayName("Should throw exception when Rockets collection is null")
    void shouldThrowExceptionWhenRocketsCollectionIsNull() {
        // Given
        Mission mission = new Mission();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mission.addRockets(null));
        assertEquals("Rockets collection can't be null", exception.getMessage());
    }
}
