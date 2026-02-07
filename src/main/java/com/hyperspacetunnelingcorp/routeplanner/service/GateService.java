package com.hyperspacetunnelingcorp.routeplanner.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hyperspacetunnelingcorp.routeplanner.dto.GateResponse;
import com.hyperspacetunnelingcorp.routeplanner.exception.GateNotFoundException;
import com.hyperspacetunnelingcorp.routeplanner.mapper.GateMapper;
import com.hyperspacetunnelingcorp.routeplanner.model.Gate;
import com.hyperspacetunnelingcorp.routeplanner.repository.GateRepository;

@Service
public class GateService {

    private final GateRepository gateRepository;

    private final GateMapper gateMapper;

    public GateService(GateRepository gateRepository, GateMapper gateMapper) {
        this.gateRepository = gateRepository;
        this.gateMapper = gateMapper;
    }

    public List<GateResponse> getGates() {
        List<Gate> gates = gateRepository.findAll();
        return gates.stream().map(gateMapper::toGateResponse).collect(Collectors.toList());
    }

    public GateResponse getGate(final String gateCode) {
        if (gateCode == null || gateCode.isEmpty()) {
            throw new IllegalArgumentException("Gate code cannot be null or empty");
        }

        return gateRepository.findById(gateCode)
            .map(gateMapper::toGateResponse)
            .orElseThrow(() -> new GateNotFoundException(gateCode));
    }
}
