package dev.amir.dragon.service;

import dev.amir.dragon.model.Rocket;
import dev.amir.dragon.model.RocketStatus;
import dev.amir.dragon.repository.RocketRepository;

/**
 * Service class to perform rocket operations.
 */
class RocketService {
    private final RocketRepository rocketRepository;

    RocketService(RocketRepository rocketRepository) {
        this.rocketRepository = rocketRepository;
    }

    public Rocket create(String name) {
        Rocket newRocket = new Rocket();
        newRocket.setName(name);
        newRocket.setStatus(RocketStatus.ON_GROUND);
        return rocketRepository.save(newRocket);
    }

    public boolean changeStatus(String rocketId, RocketStatus newStatus) {
        Rocket existingRocket = rocketRepository.getById(rocketId);
        existingRocket.setStatus(newStatus);
        Rocket modifiedRocket = rocketRepository.save(existingRocket);
        return modifiedRocket != null;
    }
}
