package dev.amir.dragon.dto;

import dev.amir.dragon.model.RocketStatus;

/**
 * Represents a concise summary of a rocket
 */
public record RocketSummary(String name, RocketStatus status) {
}
