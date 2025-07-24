package dev.amir.dragon.service;

import dev.amir.dragon.model.Mission;
import dev.amir.dragon.model.MissionStatus;
import dev.amir.dragon.model.Rocket;
import dev.amir.dragon.model.RocketStatus;
import dev.amir.dragon.repository.MissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
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

    @Test
    @DisplayName("Should change mission status successfully")
    void shouldChangeMissionStatusSuccessfully() {
        // Given
        String missionId = "Mission ID";
        Mission existingMission = buildDefaultMission(missionId, MissionStatus.SCHEDULED);
        MissionStatus newStatus = MissionStatus.ENDED;
        when(missionRepository.getById(anyString())).thenReturn(existingMission);
        when(missionRepository.save(any())).thenReturn(new Mission());

        // When
        boolean actual = missionService.changeStatus(missionId, newStatus);

        // Then
        assertTrue(actual);
        verify(missionRepository).getById(eq(missionId));
        verify(missionRepository).save(missionCaptor.capture());
        Mission modifiedMission = missionCaptor.getValue();
        assertEquals(newStatus, modifiedMission.getStatus());
    }

    @Test
    @DisplayName("Should throw exception when setting SCHEDULED status with rockets")
    void shouldThrowExceptionWhenSettingScheduledStatusWithRockets() {
        // Given
        String missionId = "Mission ID";
        Mission existingMission = buildDefaultMission(missionId, MissionStatus.PENDING);
        existingMission.addRockets(List.of(buildDefaultRocket()));
        MissionStatus newStatus = MissionStatus.SCHEDULED;
        when(missionRepository.getById(missionId)).thenReturn(existingMission);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> missionService.changeStatus(missionId, newStatus));
        assertEquals("Cannot set mission to SCHEDULED when rockets are assigned", exception.getMessage());
        assertNotEquals(newStatus, existingMission.getStatus());
        verify(missionRepository).getById(eq(missionId));
        verify(missionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when setting PENDING status without rockets")
    void shouldThrowExceptionWhenSettingPendingStatusWithoutRockets() {
        // Given
        String missionId = "Mission ID";
        Mission existingMission = buildDefaultMission(missionId, MissionStatus.SCHEDULED);
        MissionStatus newStatus = MissionStatus.PENDING;
        when(missionRepository.getById(missionId)).thenReturn(existingMission);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> missionService.changeStatus(missionId, newStatus));
        assertEquals("Cannot set mission to PENDING without assigned rockets", exception.getMessage());
        assertNotEquals(newStatus, existingMission.getStatus());
        verify(missionRepository).getById(eq(missionId));
        verify(missionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when setting IN_PROGRESS status without rockets")
    void shouldThrowExceptionWhenSettingInProgressStatusWithoutRockets() {
        // Given
        String missionId = "Mission ID";
        Mission existingMission = buildDefaultMission(missionId, MissionStatus.SCHEDULED);
        MissionStatus newStatus = MissionStatus.IN_PROGRESS;
        when(missionRepository.getById(missionId)).thenReturn(existingMission);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> missionService.changeStatus(missionId, newStatus));
        assertEquals("Cannot set mission to IN_PROGRESS without assigned rockets", exception.getMessage());
        assertNotEquals(newStatus, existingMission.getStatus());
        verify(missionRepository).getById(eq(missionId));
        verify(missionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when setting IN_PROGRESS status with rockets IN_REPAIR")
    void shouldThrowExceptionWhenSettingInProgressStatusWithRocketsInRepair() {
        // Given
        String missionId = "Mission ID";
        Mission existingMission = buildDefaultMission(missionId, MissionStatus.PENDING);
        Rocket existingRocket = buildDefaultRocket();
        existingRocket.setStatus(RocketStatus.IN_REPAIR);
        existingMission.addRockets(List.of(existingRocket));
        MissionStatus newStatus = MissionStatus.IN_PROGRESS;
        when(missionRepository.getById(missionId)).thenReturn(existingMission);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> missionService.changeStatus(missionId, newStatus));
        assertEquals("Cannot set mission to IN_PROGRESS when rockets are in repair", exception.getMessage());
        assertNotEquals(newStatus, existingMission.getStatus());
        verify(missionRepository).getById(eq(missionId));
        verify(missionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when setting ENDED status with rockets")
    void shouldThrowExceptionWhenSettingEndedStatusWithRockets() {
        // Given
        String missionId = "Mission ID";
        Mission existingMission = buildDefaultMission(missionId, MissionStatus.PENDING);
        existingMission.addRockets(List.of(buildDefaultRocket()));
        MissionStatus newStatus = MissionStatus.ENDED;
        when(missionRepository.getById(missionId)).thenReturn(existingMission);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> missionService.changeStatus(missionId, newStatus));
        assertEquals("Cannot set mission to ENDED when rockets are assigned", exception.getMessage());
        assertNotEquals(newStatus, existingMission.getStatus());
        verify(missionRepository).getById(eq(missionId));
        verify(missionRepository, never()).save(any());
    }

    private Mission buildDefaultMission(String id, MissionStatus status) {
        Mission mission = new Mission();
        mission.setId(id);
        mission.setName("Luna 1");
        mission.setStatus(status);
        return mission;
    }

    private Rocket buildDefaultRocket() {
        Rocket rocket = new Rocket();
        rocket.setId("Rocket ID");
        rocket.setName("Dragon 1");
        rocket.setStatus(RocketStatus.ON_GROUND);
        return rocket;
    }
}
