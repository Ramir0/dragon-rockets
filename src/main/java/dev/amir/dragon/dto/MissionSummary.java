package dev.amir.dragon.dto;

import dev.amir.dragon.model.MissionStatus;

import java.util.List;

/**
 * Represents a concise summary of a mission
 */
public record MissionSummary(String name, MissionStatus status, List<RocketSummary> rockets) {
}
