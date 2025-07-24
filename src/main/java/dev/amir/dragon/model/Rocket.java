package dev.amir.dragon.model;

import dev.amir.dragon.validator.InputValidator;

/**
 * Represents a rocket.
 */
public class Rocket {
    private RocketStatus status;
    private String name;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        InputValidator.requireNull(this.id, "Rocket ID is already assigned");
        this.id = InputValidator.requireNonBlank(id, "Invalid Rocket ID");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = InputValidator.requireNonBlank(name, "Invalid Rocket Name");
    }

    public RocketStatus getStatus() {
        return status;
    }

    public void setStatus(RocketStatus status) {
        InputValidator.requireNonNull(status, "Rocket Status can't be null");
        this.status = status;
    }
}
