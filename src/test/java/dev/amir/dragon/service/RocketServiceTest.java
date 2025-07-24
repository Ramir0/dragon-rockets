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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
}
