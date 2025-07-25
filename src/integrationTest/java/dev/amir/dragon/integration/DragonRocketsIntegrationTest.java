package dev.amir.dragon.integration;

import dev.amir.dragon.dto.MissionSummary;
import dev.amir.dragon.dto.RocketSummary;
import dev.amir.dragon.model.Mission;
import dev.amir.dragon.model.MissionStatus;
import dev.amir.dragon.model.Rocket;
import dev.amir.dragon.model.RocketStatus;
import dev.amir.dragon.repository.MissionInMemoryRepository;
import dev.amir.dragon.repository.MissionRepository;
import dev.amir.dragon.repository.RocketInMemoryRepository;
import dev.amir.dragon.repository.RocketRepository;
import dev.amir.dragon.service.MissionService;
import dev.amir.dragon.service.MissionSummaryService;
import dev.amir.dragon.service.RocketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Dragon Rockets Integration Test")
public class DragonRocketsIntegrationTest {
    private RocketService rocketService;
    private MissionService missionService;
    private MissionSummaryService missionSummaryService;

    @BeforeEach
    void setUp() {
        MissionRepository missionRepository = new MissionInMemoryRepository();
        RocketRepository rocketRepository = new RocketInMemoryRepository();

        rocketService = new RocketService(rocketRepository);
        missionService = new MissionService(missionRepository, rocketRepository);
        missionSummaryService = new MissionSummaryService(missionRepository);
    }

    @Test
    @DisplayName("Should demonstrate complete system workflow with example scenario")
    void shouldDemonstrateCompleteSystemWorkflowWithExampleScenario() {
        // Mission: Mars
        missionService.create("Mars");

        // Mission: Luna1
        Mission missionLuna1 = missionService.create("Luna1");
        Rocket rocketDragon1 = rocketService.create("Dragon 1");
        rocketDragon1.setStatus(RocketStatus.IN_SPACE);
        rocketService.changeStatus(rocketDragon1.getId(), rocketDragon1.getStatus());
        missionService.assignRocketToMission(missionLuna1.getId(), rocketDragon1.getId());
        Rocket rocketDragon2 = rocketService.create("Dragon 2");
        rocketDragon2.setStatus(RocketStatus.IN_REPAIR);
        rocketService.changeStatus(rocketDragon2.getId(), rocketDragon2.getStatus());
        missionService.assignRocketToMission(missionLuna1.getId(), rocketDragon2.getId());

        // Mission: Double Landing
        Mission missionDoubleLanding = missionService.create("Double Landing");
        missionDoubleLanding.setStatus(MissionStatus.ENDED);
        missionService.changeStatus(missionDoubleLanding.getId(), missionDoubleLanding.getStatus());

        // Mission: Transit
        Mission missionTransit = missionService.create("Transit");
        Rocket rocketRedDragon = rocketService.create("Red Dragon");
        missionService.assignRocketToMission(missionTransit.getId(), rocketRedDragon.getId());
        Rocket rocketDragonXl = rocketService.create("Dragon XL");
        rocketDragonXl.setStatus(RocketStatus.IN_SPACE);
        rocketService.changeStatus(rocketDragonXl.getId(), rocketDragonXl.getStatus());
        missionService.assignRocketToMission(missionTransit.getId(), rocketDragonXl.getId());
        Rocket rocketFalconHeavy = rocketService.create("Falcon Heavy");
        rocketFalconHeavy.setStatus(RocketStatus.IN_SPACE);
        rocketService.changeStatus(rocketFalconHeavy.getId(), rocketFalconHeavy.getStatus());
        missionService.assignRocketToMission(missionTransit.getId(), rocketFalconHeavy.getId());

        // Mission: Luna2
        missionService.create("Luna2");

        // Mission: Vertical Landing
        Mission missionVerticalLanding = missionService.create("Vertical Landing");
        missionVerticalLanding.setStatus(MissionStatus.ENDED);
        missionService.changeStatus(missionVerticalLanding.getId(), missionVerticalLanding.getStatus());

        // No Mission Rockets
        rocketService.create("Dragon 3");

        // Generate Mission Summary
        List<MissionSummary> summary = missionSummaryService.generateMissionSummary();

        // Extract Summary objects to validate
        MissionSummary summaryTransit = summary.getFirst();
        int totalRocketsInTransit = summaryTransit.rockets().size();
        RocketSummary summaryRedDragon = summaryTransit.rockets().getFirst();
        RocketSummary summaryDragonXl = summaryTransit.rockets().get(1);
        RocketSummary summaryFalconHeavy = summaryTransit.rockets().get(2);
        MissionSummary summaryLuna1 = summary.get(1);
        int totalRocketsInLuna1 = summaryLuna1.rockets().size();
        RocketSummary summaryDragon1 = summaryLuna1.rockets().getFirst();
        RocketSummary summaryDragon2 = summaryLuna1.rockets().get(1);
        MissionSummary summaryVerticalLanding = summary.get(2);
        int totalRocketsInVerticalLanding = summaryVerticalLanding.rockets().size();
        MissionSummary summaryMars = summary.get(3);
        int totalRocketsInMars = summaryMars.rockets().size();
        MissionSummary summaryLuna2 = summary.get(4);
        int totalRocketsInLuna2 = summaryLuna2.rockets().size();
        MissionSummary summaryDoubleLanding = summary.get(5);
        int totalRocketsInDoubleLanding = summaryDoubleLanding.rockets().size();

        assertAll(
                "Mission Summary",
                () -> assertEquals("Transit", summaryTransit.name()),
                () -> assertEquals(MissionStatus.IN_PROGRESS, summaryTransit.status()),
                () -> assertEquals(3, totalRocketsInTransit),
                () -> assertEquals("Red Dragon", summaryRedDragon.name()),
                () -> assertEquals(RocketStatus.ON_GROUND, summaryRedDragon.status()),
                () -> assertEquals("Dragon XL", summaryDragonXl.name()),
                () -> assertEquals(RocketStatus.IN_SPACE, summaryDragonXl.status()),
                () -> assertEquals("Falcon Heavy", summaryFalconHeavy.name()),
                () -> assertEquals(RocketStatus.IN_SPACE, summaryFalconHeavy.status()),

                () -> assertEquals("Luna1", summaryLuna1.name()),
                () -> assertEquals(MissionStatus.PENDING, summaryLuna1.status()),
                () -> assertEquals(2, totalRocketsInLuna1),
                () -> assertEquals("Dragon 1", summaryDragon1.name()),
                () -> assertEquals(RocketStatus.IN_SPACE, summaryDragon1.status()),
                () -> assertEquals("Dragon 2", summaryDragon2.name()),
                () -> assertEquals(RocketStatus.IN_REPAIR, summaryDragon2.status()),

                () -> assertEquals("Vertical Landing", summaryVerticalLanding.name()),
                () -> assertEquals(MissionStatus.ENDED, summaryVerticalLanding.status()),
                () -> assertEquals(0, totalRocketsInVerticalLanding),

                () -> assertEquals("Mars", summaryMars.name()),
                () -> assertEquals(MissionStatus.SCHEDULED, summaryMars.status()),
                () -> assertEquals(0, totalRocketsInMars),

                () -> assertEquals("Luna2", summaryLuna2.name()),
                () -> assertEquals(MissionStatus.SCHEDULED, summaryLuna2.status()),
                () -> assertEquals(0, totalRocketsInLuna2),

                () -> assertEquals("Double Landing", summaryDoubleLanding.name()),
                () -> assertEquals(MissionStatus.ENDED, summaryDoubleLanding.status()),
                () -> assertEquals(0, totalRocketsInDoubleLanding)
        );
    }
}
