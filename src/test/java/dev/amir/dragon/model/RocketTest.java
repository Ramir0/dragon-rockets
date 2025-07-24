package dev.amir.dragon.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for Rocket entity.
 */
@DisplayName("Rocket Entity Tests")
class RocketTest {
    @Test
    @DisplayName("Should create rocket with empty fields")
    void shouldCreateRocketWithEmptyFields() {
        // Given
        Rocket rocket = new Rocket();

        // When & Then
        assertNull(rocket.getId());
        assertNull(rocket.getName());
        assertNull(rocket.getStatus());
    }

    @Test
    @DisplayName("Should assign valid ID")
    void shouldAssignValidId() {
        // Given
        String rocketId = "Rocket ID";
        Rocket rocket = new Rocket();

        // When
        rocket.setId(rocketId);

        // Then
        assertEquals(rocketId, rocket.getId());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should throw exception when ID is blank")
    void shouldThrowExceptionWhenIdIsBlank(String input) {
        // Given
        Rocket rocket = new Rocket();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> rocket.setId(input));
        assertEquals("Invalid Rocket ID", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when ID exists")
    void shouldThrowExceptionWhenIdExists() {
        // Given
        String oldId = "Existing Rocket ID";
        Rocket rocket = new Rocket();
        rocket.setId(oldId);
        String newId = "New Rocket ID";

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> rocket.setId(newId));
        assertEquals("Rocket ID is already assigned", exception.getMessage());
    }

    @Test
    @DisplayName("Should assign valid Name")
    void shouldAssignValidName() {
        // Given
        String rocketName = "Rocket Name";
        Rocket rocket = new Rocket();

        // When
        rocket.setName(rocketName);

        // Then
        assertEquals(rocketName, rocket.getName());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should throw exception when Name is blank")
    void shouldThrowExceptionWhenNameIsBlank(String input) {
        // Given
        Rocket rocket = new Rocket();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> rocket.setName(input));
        assertEquals("Invalid Rocket Name", exception.getMessage());
    }

    @Test
    @DisplayName("Should assign valid Status")
    void shouldAssignValidStatus() {
        // Given
        RocketStatus rocketStatus = RocketStatus.ON_GROUND;
        Rocket rocket = new Rocket();

        // When
        rocket.setStatus(rocketStatus);

        // Then
        assertEquals(rocketStatus, rocket.getStatus());
    }

    @Test
    @DisplayName("Should throw exception when Status is null")
    void shouldThrowExceptionWhenStatusIsNull() {
        // Given
        Rocket rocket = new Rocket();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> rocket.setStatus(null));
        assertEquals("Rocket Status can't be null", exception.getMessage());
    }
}
