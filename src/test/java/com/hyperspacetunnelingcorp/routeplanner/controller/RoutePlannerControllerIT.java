package com.hyperspacetunnelingcorp.routeplanner.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyperspacetunnelingcorp.routeplanner.dto.GateConnectionResponse;
import com.hyperspacetunnelingcorp.routeplanner.dto.GateResponse;
import com.hyperspacetunnelingcorp.routeplanner.model.CheapestRoute;
import com.hyperspacetunnelingcorp.routeplanner.model.CheapestTransport;
import com.hyperspacetunnelingcorp.routeplanner.model.Transport;

/**
 * High-level integration tests
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RoutePlannerControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getCheapestTransport_whenHSTCIsCheaper_thenReturnHSTC() throws Exception {
        String json = mockMvc.perform(get("/transport/1?passengers=1&parking=1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CheapestTransport response = objectMapper.readValue(json, CheapestTransport.class);

        assertEquals(new CheapestTransport(Transport.HSTC_TRANSPORT, BigDecimal.valueOf(0.45)), response,
                "Response does not match");
    }

    @Test
    void getCheapestTransport_whenPersonalIsCheaper_thenReturnPersonal() throws Exception {
        String json = mockMvc.perform(get("/transport/34?passengers=1&parking=1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CheapestTransport response = objectMapper.readValue(json, CheapestTransport.class);

        assertEquals(new CheapestTransport(Transport.PERSONAL_TRANSPORT, BigDecimal.valueOf(15.20).setScale(2)), response,
                "Response does not match");
    }

    @Test // parking parameter is missing
    void getCheapestTransport_whenParameterIsMissing_thenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/transport/34?passengers=1"))
                .andExpect(status().isBadRequest());
    }

    @Test // passenger parameter value -1 is invalid
    void getCheapestTransport_whenParameterIsInvalid_thenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/transport/1?passengers=-1&parking=1"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void getGates_whenGatesExist_thenReturnGates() throws Exception {
        String json = mockMvc.perform(get("/gates"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Collection<GateResponse> response = objectMapper.readValue(json, new TypeReference<List<GateResponse>>() {
        });

        assertEquals(13, response.size(), "Gates count does not match");
    }

    @Test
    void getGate_whenGateExists_thenReturnGate() throws Exception {
        String json = mockMvc.perform(get("/gates/SOL"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        GateResponse response = objectMapper.readValue(json, GateResponse.class);
        
        assertEquals("SOL", response.id(), "Gate code does not match");
        assertEquals("Sol", response.name(), "Gate name does not match");
        assertEquals(5, response.connections().size(), "Connections count does not match");
    }

    @Test
    void getGate_whenGateDoesNotExist_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/gates/INVALID"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCheapestRoute_whenRouteExists_thenReturnRoute() throws Exception {
        String json = mockMvc.perform(get("/gates/SOL/to/SIR"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CheapestRoute response = objectMapper.readValue(json, CheapestRoute.class);

        assertEquals(new CheapestRoute(List.of("SOL", "SIR"), 100, BigDecimal.valueOf(10.00).setScale(2, RoundingMode.UNNECESSARY)), response, "Cheapest route does not match");
    }

    @Test
    void getCheapestRoute_whenGateDoesNotExist_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/gates/INVALID/to/CAS"))
                .andExpect(status().isNotFound());
    }

    @Test // dest is not specified
    void getCheapestRoute_whenGateNotSpecified_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/gates/INVALID/to/"))
                .andExpect(status().isNotFound());
    }
}
