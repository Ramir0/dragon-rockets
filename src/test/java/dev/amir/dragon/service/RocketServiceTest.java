package dev.amir.dragon.service;

import dev.amir.dragon.model.Rocket;
import dev.amir.dragon.model.RocketStatus;
import dev.amir.dragon.repository.RocketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for Rocket Service.
 */
@DisplayName("Rocket Service Tests")
@ExtendWith(MockitoExtension.class)
class RocketServiceTest {

    @Captor
    ArgumentCaptor<Rocket> rocketCaptor;

    @Mock
    private RocketRepository rocketRepository;

    private RocketService rocketService;

    @BeforeEach
    void setUp() {
        this.rocketService = new RocketService(rocketRepository);
    }

    @Test
    @DisplayName("Should create a new rocket")
    void shouldCreateANewRocket() {
        // Given
        String rocketName = "Dragon 1";
        Rocket expected = new Rocket();
        when(rocketRepository.save(any())).thenReturn(expected);

        // When
        Rocket actual = rocketService.create(rocketName);

        // Then
        assertEquals(expected, actual);
        verify(rocketRepository).save(rocketCaptor.capture());
        Rocket createdRocket = rocketCaptor.getValue();
        assertEquals(RocketStatus.ON_GROUND, createdRocket.getStatus());
        assertEquals(rocketName, createdRocket.getName());
    }

    @Test
    @DisplayName("Should change the current rocket status")
    void shouldChangeTheCurrentRocketStatus() {
        // Given
        String rocketId = "RocketID";
        RocketStatus newStatus = RocketStatus.IN_SPACE;
        Rocket existingRocket = buildDefaultRocket(rocketId);
        when(rocketRepository.findById(anyString())).thenReturn(Optional.of(existingRocket));
        when(rocketRepository.save(any())).thenReturn(new Rocket());

        // When
        boolean actual = rocketService.changeStatus(rocketId, newStatus);

        // Then
        assertTrue(actual);
        verify(rocketRepository).findById(eq(rocketId));
        verify(rocketRepository).save(rocketCaptor.capture());
        Rocket modifiedRocket = rocketCaptor.getValue();
        assertEquals(newStatus, modifiedRocket.getStatus());
    }

    @Test
    @DisplayName("Should throw exception when rocket does not exist while changing rocket status")
    void shouldThrowExceptionWhenRocketDoesNotExistWhileChangingRocketStatus() {
        // Given
        String rocketId = "RocketID";
        RocketStatus newStatus = RocketStatus.IN_SPACE;
        when(rocketRepository.findById(anyString())).thenReturn(Optional.empty());

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> rocketService.changeStatus(rocketId, newStatus));
        assertEquals("Rocket with ID: RocketID was not found", exception.getMessage());
        verify(rocketRepository).findById(eq(rocketId));
        verify(rocketRepository, never()).save(any());
    }

    private Rocket buildDefaultRocket(String id) {
        Rocket rocket = new Rocket();
        rocket.setId(id);
        rocket.setName("Dragon 1");
        rocket.setStatus(RocketStatus.ON_GROUND);
        return rocket;
    }
}
