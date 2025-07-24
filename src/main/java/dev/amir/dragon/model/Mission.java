package dev.amir.dragon.model;

import dev.amir.dragon.validator.InputValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a space mission.
 */
public class Mission {
    private final List<Rocket> rockets;

    private String id;
    private String name;
    private MissionStatus status;

    public Mission() {
        this.rockets = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        InputValidator.requireNull(this.id, "Mission ID is already assigned");
        this.id = InputValidator.requireNonBlank(id, "Invalid Mission ID");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = InputValidator.requireNonBlank(name, "Invalid Mission Name");
    }

    public MissionStatus getStatus() {
        return status;
    }

    public void setStatus(MissionStatus status) {
        InputValidator.requireNonNull(status, "Mission Status can't be null");
        this.status = status;
    }

    public List<Rocket> getRockets() {
        return List.copyOf(rockets);
    }

    public void addRockets(Collection<Rocket> rockets) {
        InputValidator.requireNonNull(rockets, "Rockets collection can't be null");
        this.rockets.addAll(rockets);
    }
}
