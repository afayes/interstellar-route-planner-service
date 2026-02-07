package com.hyperspacetunnelingcorp.routeplanner.dto;

import java.util.List;

public record GateResponse(
    String id, 
    String name, 
    List<GateConnectionResponse> connections) {
}
