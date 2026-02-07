package com.hyperspacetunnelingcorp.routeplanner.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.hyperspacetunnelingcorp.routeplanner.dto.GateConnectionResponse;
import com.hyperspacetunnelingcorp.routeplanner.dto.GateResponse;
import com.hyperspacetunnelingcorp.routeplanner.model.Gate;
import com.hyperspacetunnelingcorp.routeplanner.model.GateConnection;

@Component
public class GateMapper {

    public GateResponse toGateResponse(Gate gate) {
        List<GateConnectionResponse> connections = gate.getConnections().stream()
            .map(this::toGateConnectionResponse)
            .collect(Collectors.toList());
        
        return new GateResponse(gate.getId(), gate.getName(), connections);
    }

    private GateConnectionResponse toGateConnectionResponse(GateConnection connection) {
        return new GateConnectionResponse(connection.getToGate().getId(), connection.getToGate().getName(), connection.getHu());
    }
}
