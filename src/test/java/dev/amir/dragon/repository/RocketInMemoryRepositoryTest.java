package dev.amir.dragon.repository;

import dev.amir.dragon.model.Rocket;
import dev.amir.dragon.model.RocketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for Rocket In Memory Repository.
 */
@DisplayName("Rocket In Memory Repository Tests")
class RocketInMemoryRepositoryTest {
    private RocketRepository rocketRepository;

    @BeforeEach
    void setUp() {
        rocketRepository = new RocketInMemoryRepository();
    }

    @Test
    @DisplayName("Should insert a new rocket")
    void shouldInsertANewRocket() {
        // Given
        Rocket newRocket = new Rocket();
        newRocket.setName("NewRocket");
        newRocket.setStatus(RocketStatus.ON_GROUND);

        // When
        Rocket savedRocket = rocketRepository.save(newRocket);

        // Then
        assertNotNull(savedRocket.getId());
        assertEquals(newRocket.getName(), savedRocket.getName());
        assertEquals(newRocket.getStatus(), savedRocket.getStatus());
    }

    @Test
    @DisplayName("Should update an existing rocket")
    void shouldUpdateAnExistingRocket() {
        // Given
        Rocket newRocket = new Rocket();
        newRocket.setName("NewRocket");
        newRocket.setStatus(RocketStatus.ON_GROUND);
        Rocket savedRocket = rocketRepository.save(newRocket);
        savedRocket.setStatus(RocketStatus.IN_SPACE);

        // When
        Rocket updatedRocket = rocketRepository.save(savedRocket);

        // Then
        assertEquals(savedRocket.getId(), updatedRocket.getId());
        assertEquals(savedRocket.getName(), updatedRocket.getName());
        assertEquals(savedRocket.getStatus(), updatedRocket.getStatus());
    }

    @Test
    @DisplayName("Should throw exception when rocket ID does not exist")
    void shouldThrowExceptionWhenRocketIdDoesNotExist() {
        // Given
        String fakeRocketId = UUID.randomUUID().toString();
        Rocket newRocket = new Rocket();
        newRocket.setId(fakeRocketId);
        newRocket.setName("NewRocket");
        newRocket.setStatus(RocketStatus.ON_GROUND);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> rocketRepository.save(newRocket));
        assertEquals(String.format("Rocket with ID: %s does not exist", fakeRocketId), exception.getMessage());
    }

    @Test
    @DisplayName("Should find a rocket by ID")
    void shouldFindARocketById() {
        // Given
        Rocket newRocket = new Rocket();
        newRocket.setName("NewRocket");
        newRocket.setStatus(RocketStatus.ON_GROUND);
        Rocket savedRocket = rocketRepository.save(newRocket);

        // When
        Optional<Rocket> foundRocket = rocketRepository.findById(savedRocket.getId());

        // Then
        assertTrue(foundRocket.isPresent());
        assertEquals(savedRocket.getId(), foundRocket.get().getId());
    }

    @Test
    @DisplayName("Should return empty when rocket ID does not exist")
    void shouldReturnEmptyWhenRocketIdDoesNotExist() {
        // Given
        Rocket newRocket = new Rocket();
        newRocket.setName("NewRocket");
        newRocket.setStatus(RocketStatus.ON_GROUND);
        rocketRepository.save(newRocket);

        // When
        Optional<Rocket> foundRocket = rocketRepository.findById(UUID.randomUUID().toString());

        // Then
        assertTrue(foundRocket.isEmpty());
    }
}
