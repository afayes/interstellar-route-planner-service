package com.hyperspacetunnelingcorp.routeplanner.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyperspacetunnelingcorp.routeplanner.exception.GateNotFoundException;
import com.hyperspacetunnelingcorp.routeplanner.model.Gate;
import com.hyperspacetunnelingcorp.routeplanner.repository.GateRepository;

@Service
public class GateService {

    private final GateRepository gateRepository;

    public GateService(GateRepository gateRepository) {
        this.gateRepository = gateRepository;
    }

    public List<Gate> getGates() {
        return gateRepository.findAllWithConnections();
    }

    public Gate getGate(final String gateCode) {
        if (gateCode == null || gateCode.isBlank()) {
            throw new IllegalArgumentException("Gate code cannot be null or empty");
        }

        return gateRepository
            .findById(gateCode)
            .orElseThrow(() -> new GateNotFoundException(gateCode));
    }
}
