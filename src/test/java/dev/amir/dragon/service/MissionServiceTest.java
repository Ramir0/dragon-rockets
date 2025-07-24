package dev.amir.dragon.service;

import dev.amir.dragon.model.Mission;
import dev.amir.dragon.model.MissionStatus;
import dev.amir.dragon.repository.MissionRepository;
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
 * Tests for Mission Service.
 */
@DisplayName("Mission Service Tests")
@ExtendWith(MockitoExtension.class)
class MissionServiceTest {

    @Captor
    ArgumentCaptor<Mission> missionCaptor;

    @Mock
    private MissionRepository missionRepository;

    private MissionService missionService;

    @BeforeEach
    void setUp() {
        this.missionService = new MissionService(missionRepository);
    }

    @Test
    @DisplayName("Should create a new mission")
    void shouldCreateANewMission() {
        // Given
        String missionName = "Luna 1";
        Mission expected = new Mission();
        when(missionRepository.save(any())).thenReturn(expected);

        // When
        Mission actual = missionService.create(missionName);

        // Then
        assertEquals(expected, actual);
        verify(missionRepository).save(missionCaptor.capture());
        Mission createdMission = missionCaptor.getValue();
        assertEquals(MissionStatus.SCHEDULED, createdMission.getStatus());
        assertEquals(missionName, createdMission.getName());
    }
}
