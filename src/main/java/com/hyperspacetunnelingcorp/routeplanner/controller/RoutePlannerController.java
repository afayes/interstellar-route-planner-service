package com.hyperspacetunnelingcorp.routeplanner.controller;

import java.util.Collection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hyperspacetunnelingcorp.routeplanner.dto.CheapestTransportResponse;
import com.hyperspacetunnelingcorp.routeplanner.dto.GateResponse;
import com.hyperspacetunnelingcorp.routeplanner.service.GateService;
import com.hyperspacetunnelingcorp.routeplanner.service.TransportCostService;

@RestController
public class RoutePlannerController {

    private final TransportCostService transportCostService;
    private final GateService gateService;

    public RoutePlannerController(TransportCostService transportCostService, GateService gateService) {
        this.transportCostService = transportCostService;
        this.gateService = gateService;
    }

    @GetMapping("/transport/{distance}")
    public CheapestTransportResponse getCheapestTransport(
        @PathVariable double distance, 
        @RequestParam int passengers, 
        @RequestParam int parking) {    
        return transportCostService.calculateCheapestTransport(distance, passengers, parking);
    } 
    
    @GetMapping("/gates")
    public Collection<GateResponse> getGates() {
        return gateService.getGates();
    }

    @GetMapping("/gates/{gateCode}")
    public GateResponse getGate(@PathVariable String gateCode) {
        return gateService.getGate(gateCode);
    }
}
