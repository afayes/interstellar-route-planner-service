package com.hyperspacetunnelingcorp.routeplanner.controller;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hyperspacetunnelingcorp.routeplanner.dto.GateResponse;
import com.hyperspacetunnelingcorp.routeplanner.mapper.GateMapper;
import com.hyperspacetunnelingcorp.routeplanner.model.CheapestTransport;
import com.hyperspacetunnelingcorp.routeplanner.model.Gate;
import com.hyperspacetunnelingcorp.routeplanner.service.GateService;
import com.hyperspacetunnelingcorp.routeplanner.service.TransportCostService;

@RestController
public class RoutePlannerController {

    private final TransportCostService transportCostService;
    private final GateService gateService;
    private final GateMapper gateMapper;


    public RoutePlannerController(TransportCostService transportCostService, GateService gateService, GateMapper gateMapper) {
        this.transportCostService = transportCostService;
        this.gateService = gateService;
        this.gateMapper = gateMapper;
    }

    @GetMapping("/transport/{distance}")
    public CheapestTransport getCheapestTransport(
        @PathVariable double distance, 
        @RequestParam int passengers, 
        @RequestParam int parking) {    
        return transportCostService.calculateCheapestTransport(distance, passengers, parking);
    } 
    
    @GetMapping("/gates")
    public Collection<GateResponse> getGates() {
        List<Gate> gates = gateService.getGates();
        return gates.stream().map(gateMapper::toGateResponse).collect(Collectors.toList());
    }

    @GetMapping("/gates/{gateCode}")
    public GateResponse getGate(@PathVariable String gateCode) {
        Gate gate = gateService.getGate(gateCode);
        return gateMapper.toGateResponse(gate);
    }
} 
