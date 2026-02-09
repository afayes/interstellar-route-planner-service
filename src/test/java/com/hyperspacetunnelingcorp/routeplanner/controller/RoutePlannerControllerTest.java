package com.hyperspacetunnelingcorp.routeplanner.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyperspacetunnelingcorp.routeplanner.dto.GateConnectionResponse;
import com.hyperspacetunnelingcorp.routeplanner.dto.GateResponse;
import com.hyperspacetunnelingcorp.routeplanner.exception.GateNotFoundException;
import com.hyperspacetunnelingcorp.routeplanner.exception.RouteNotFoundException;
import com.hyperspacetunnelingcorp.routeplanner.mapper.GateMapper;
import com.hyperspacetunnelingcorp.routeplanner.model.CheapestRoute;
import com.hyperspacetunnelingcorp.routeplanner.model.CheapestTransport;
import com.hyperspacetunnelingcorp.routeplanner.model.Gate;
import com.hyperspacetunnelingcorp.routeplanner.model.GateConnection;
import com.hyperspacetunnelingcorp.routeplanner.model.Transport;
import com.hyperspacetunnelingcorp.routeplanner.service.GateService;
import com.hyperspacetunnelingcorp.routeplanner.service.RouteService;
import com.hyperspacetunnelingcorp.routeplanner.service.TransportCostService;

@WebMvcTest(RoutePlannerController.class)
class RoutePlannerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransportCostService transportCostService;
    
    @MockitoBean
    private GateService gateService;

    @MockitoBean
    private RouteService routeService;

    @MockitoBean
    private GateMapper gateMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void resetMocks() {
        Mockito.reset(routeService, transportCostService, gateService, gateMapper);
    }

    @Test
    void getCheapestTransport_whenValidRequest_thenReturnCheapestTransport() throws UnsupportedEncodingException, Exception {
        when(transportCostService.calculateCheapestTransport(1, 1, 1))
        .thenReturn(new CheapestTransport(Transport.HSTC_TRANSPORT, BigDecimal.valueOf(0.45)));

        String json = mockMvc.perform(get("/transport/1?passengers=1&parking=1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CheapestTransport response = objectMapper.readValue(json, CheapestTransport.class);

        assertEquals(new CheapestTransport(Transport.HSTC_TRANSPORT, BigDecimal.valueOf(0.45)), response,
                "Response does not match");

        verify(transportCostService).calculateCheapestTransport(1, 1, 1);
        verifyNoMoreInteractions(transportCostService);
    }

    @Test
    void getCheapestTransport_whenServiceThrowsIllegalArgumentException_thenReturnBadRequest() throws UnsupportedEncodingException, Exception {
        when(transportCostService.calculateCheapestTransport(-1, 1, 1))
        .thenThrow(new IllegalArgumentException("Distance must be greater than 0"));

        mockMvc.perform(get("/transport/-1?passengers=1&parking=1"))
                .andExpect(status().isBadRequest());

        verify(transportCostService).calculateCheapestTransport(-1, 1, 1);
        verifyNoMoreInteractions(transportCostService);
    }

    @Test
    void getCheapestTransport_whenDistanceIsInvalid_thenReturnBadRequest() throws UnsupportedEncodingException, Exception {
        mockMvc.perform(get("/transport/invalid?passengers=1&parking=1"))
                .andExpect(status().isBadRequest());

        verifyNoMoreInteractions(transportCostService);
    }

    @Test
    void getCheapestTransport_whenPassengersIsInvalid_thenReturnBadRequest() throws UnsupportedEncodingException, Exception {
        mockMvc.perform(get("/transport/invalid?passengers=invalid&parking=1"))
                .andExpect(status().isBadRequest());

        verifyNoMoreInteractions(transportCostService);
    }

    @Test
    void getCheapestTransport_whenPassengersIsMissing_thenReturnBadRequest() throws UnsupportedEncodingException, Exception {
        mockMvc.perform(get("/transport/invalid?&parking=1"))
                .andExpect(status().isBadRequest());

        verifyNoMoreInteractions(transportCostService);
    }

    @Test
    void getCheapestTransport_whenPassengersIsEmpty_thenReturnBadRequest() throws UnsupportedEncodingException, Exception {
        mockMvc.perform(get("/transport/invalid?passengers=&parking=1"))
                .andExpect(status().isBadRequest());

        verifyNoMoreInteractions(transportCostService);
    }

    // ----
    @Test
    void getCheapestTransport_whenParkingIsInvalid_thenReturnBadRequest() throws UnsupportedEncodingException, Exception {
        mockMvc.perform(get("/transport/invalid?passengers=1&parking=invalid"))
                .andExpect(status().isBadRequest());

        verifyNoMoreInteractions(transportCostService);
    }

    @Test
    void getCheapestTransport_whenParkingIsMissing_thenReturnBadRequest() throws UnsupportedEncodingException, Exception {
        mockMvc.perform(get("/transport/invalid?passengers=1&"))
                .andExpect(status().isBadRequest());

        verifyNoMoreInteractions(transportCostService);
    }

    @Test
    void getCheapestTransport_whenParkingIsEmpty_thenReturnBadRequest() throws UnsupportedEncodingException, Exception {
        mockMvc.perform(get("/transport/invalid?passengers=1&parking="))
                .andExpect(status().isBadRequest());

        verifyNoMoreInteractions(transportCostService);
    }

    @Test
    void getGates_whenServiceReturnsGates_thenReturnGates() throws UnsupportedEncodingException, Exception {
        Gate gateA = new Gate("A", "a", List.of(new GateConnection(null, new Gate("B", "b", null), 1)));
        Gate gateB = new Gate("B", "b", List.of());
        
        List<Gate> gates = List.of(gateA, gateB);

        when(gateService.getGates()).thenReturn(gates);

        GateResponse gateResponseA = new GateResponse("A", "a", List.of(new GateConnectionResponse("B", "b", 1)));
        GateResponse gateResponseB = new GateResponse("B", "b", List.of());
        
        List<GateResponse> gateResponsesExpected = List.of(gateResponseA, gateResponseB);

        when(gateMapper.toGateResponse(gateA)).thenReturn(gateResponseA);
        when(gateMapper.toGateResponse(gateB)).thenReturn(gateResponseB);


        String json = mockMvc.perform(get("/gates"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
                
        List<GateResponse> response = objectMapper.readValue(json, new TypeReference<List<GateResponse>>() {});


        assertEquals(gateResponsesExpected, response, "Gate Response does not match");

        verify(gateService).getGates();
        verify(gateMapper).toGateResponse(gateA);
        verify(gateMapper).toGateResponse(gateB);
        verifyNoMoreInteractions(gateService, gateMapper);
    }

    @Test
    void getGate_whenServiceReturnsGate_thenReturnGate() throws UnsupportedEncodingException, Exception {
        Gate gate = new Gate("A", "a", List.of(new GateConnection(null, new Gate("B", "b", null), 1)));

        when(gateService.getGate("A")).thenReturn(gate);

        GateResponse gateResponseExpected = new GateResponse("A", "a", List.of(new GateConnectionResponse("B", "b", 1)));

        when(gateMapper.toGateResponse(gate)).thenReturn(gateResponseExpected);

        String json = mockMvc.perform(get("/gates/A"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
                
        GateResponse response = objectMapper.readValue(json, GateResponse.class);

        assertEquals(gateResponseExpected, response, "Gate Response does not match");

        verify(gateService).getGate("A");
        verify(gateMapper).toGateResponse(gate);
        verifyNoMoreInteractions(gateService, gateMapper);
    }

    @Test
    void getGate_whenGateParamisMissing_thenReturnNotFound() throws UnsupportedEncodingException, Exception {
        mockMvc.perform(get("/gates/"))
                .andExpect(status().isNotFound());

        verifyNoMoreInteractions(gateService, gateMapper);
    }

    @Test
    void getGate_whenServiceThrowsGateNotFoundException_thenReturnNotFound() throws UnsupportedEncodingException, Exception {
        when(gateService.getGate("a")).thenThrow(new GateNotFoundException("a"));

        mockMvc.perform(get("/gates/a"))
                .andExpect(status().isNotFound());

        verify(gateService).getGate("a");
        verifyNoMoreInteractions(gateService, gateMapper);
    }

    @Test
    void getGate_whenServiceThrowsIllegalArgumentException_thenReturnBadRequest() throws UnsupportedEncodingException, Exception {
        when(gateService.getGate(" ")).thenThrow(new IllegalArgumentException("Gate code cannot be null or empty"));

        mockMvc.perform(get("/gates/ "))
                .andExpect(status().isBadRequest());

        verify(gateService).getGate(" ");
        verifyNoMoreInteractions(gateService, gateMapper);
    }

    @Test
    void getCheapestRoute_whenRouteExists_thenReturnCheapestRoute() throws UnsupportedEncodingException, Exception {
        CheapestRoute cheapestRoute = new CheapestRoute(List.of("A", "B", "C"), 300, BigDecimal.valueOf(30.00));
        when(routeService.getCheapestRoute("A", "C")).thenReturn(cheapestRoute);

        String json = mockMvc.perform(get("/gates/A/to/C"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CheapestRoute response = objectMapper.readValue(json, CheapestRoute.class);

        assertEquals(cheapestRoute, response,"Cheapest route does not match");

       verify(routeService).getCheapestRoute("A", "C");
       verifyNoMoreInteractions(routeService);
    }

    @Test
    void getCheapestRoute_whenServiceThrowsIllegalArgumentException_thenReturnBadRequest() throws UnsupportedEncodingException, Exception {
        when(routeService.getCheapestRoute(" ", "C")).thenThrow(new IllegalArgumentException("Gate code cannot be null or empty"));
       
        mockMvc.perform(get("/gates/ /to/C")).andExpect(status().isBadRequest());

        verify(routeService).getCheapestRoute(" ", "C");
        verifyNoMoreInteractions(routeService);
    }

    @Test
    void getCheapestRoute_whenServiceThrowsGateNotFoundException_thenReturnNotFound() throws UnsupportedEncodingException, Exception {
        when(routeService.getCheapestRoute("A", "C")).thenThrow(new GateNotFoundException("A"));
       
        mockMvc.perform(get("/gates/A/to/C")).andExpect(status().isNotFound());

        verify(routeService).getCheapestRoute("A", "C");
        verifyNoMoreInteractions(routeService);
    }

    @Test
    void getCheapestRoute_whenServiceThrowsRouteNotFoundException_thenReturnNotFound() throws UnsupportedEncodingException, Exception {
        when(routeService.getCheapestRoute("A", "C")).thenThrow(new RouteNotFoundException("A", "C"));
       
        mockMvc.perform(get("/gates/A/to/C")).andExpect(status().isNotFound());

        verify(routeService).getCheapestRoute("A", "C");
        verifyNoMoreInteractions(routeService);
    }
}
