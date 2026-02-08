package com.hyperspacetunnelingcorp.routeplanner.service;

import com.hyperspacetunnelingcorp.routeplanner.exception.GateNotFoundException;
import com.hyperspacetunnelingcorp.routeplanner.exception.RouteNotFoundException;
import com.hyperspacetunnelingcorp.routeplanner.model.CheapestRoute;
import com.hyperspacetunnelingcorp.routeplanner.model.Gate;
import com.hyperspacetunnelingcorp.routeplanner.model.GateConnection;
import com.hyperspacetunnelingcorp.routeplanner.repository.GateRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

class RouteServiceTest {

    private final GateRepository gateRepository = mock(GateRepository.class);
    private final RouteService routeService = new RouteService(gateRepository);

    @Test
    void getCheapestRoute_whenRouteExists_shouldReturnRoute() {
        List<Gate> gates = List.of(
                    new Gate("A", "a",
                            List.of(
                                new GateConnection(null, new Gate("B", "b", null), 1),
                                new GateConnection(null, new Gate("C", "c", null), 3)
                            )
                        ),
                    new Gate("B", "b",
                        List.of(
                            new GateConnection(null, new Gate("C", "c", null), 5)
                        )
                    ),
                    new Gate("C", "c", List.of())
                );

        when(gateRepository.findAllWithConnections()).thenReturn(gates);

        routeService.buildGraph();

        assertEquals(new CheapestRoute(List.of("A", "C"), 3, BigDecimal.valueOf(0.30).setScale(2)), routeService.getCheapestRoute("A", "C"));
        verify(gateRepository).findAllWithConnections();
        verifyNoMoreInteractions(gateRepository);
    }

    @Test
    void getCheapestRoute_whenRouteDoesNotExist_shouldThrowRouteNotFoundException() {
        List<Gate> gates = List.of(
                    new Gate("A", "a",
                            List.of(
                                new GateConnection(null, new Gate("B", "b", null), 1)
                            )
                        ),
                    new Gate("B", "b",
                        List.of()
                    )
                );

        when(gateRepository.findAllWithConnections()).thenReturn(gates);

        routeService.buildGraph();

        assertThrows(RouteNotFoundException.class, () -> routeService.getCheapestRoute("B", "A"));
        verify(gateRepository).findAllWithConnections();
        verifyNoMoreInteractions(gateRepository);
    }

    @Test
    void getCheapestRoute_whenSourceAndDestIsSame_shouldRetrurnSinglePathRoute() {
        List<Gate> gates = List.of(new Gate("A", "a", List.of()));

        when(gateRepository.findAllWithConnections()).thenReturn(gates);

        routeService.buildGraph();

        assertEquals(new CheapestRoute(List.of("A"), 0, BigDecimal.valueOf(0.00).setScale(2)), routeService.getCheapestRoute("A", "A"));
        verify(gateRepository).findAllWithConnections();
        verifyNoMoreInteractions(gateRepository);
    }      
    
    @Test
    void getCheapestRoute_whenSourceDoesNotExist_shouldThrowGatewayNotFoundException() {
        List<Gate> gates = List.of(
                    new Gate("A", "a",
                            List.of(
                                new GateConnection(null, new Gate("B", "b", null), 1)
                            )
                        ),
                    new Gate("B", "b",
                        List.of()
                    )
                );

        when(gateRepository.findAllWithConnections()).thenReturn(gates);

        routeService.buildGraph();

        assertThrows(GateNotFoundException.class, () -> routeService.getCheapestRoute("C", "A"));
        verify(gateRepository).findAllWithConnections();
        verifyNoMoreInteractions(gateRepository);
    }

    @Test
    void getCheapestRoute_whenDestDoesNotExist_shouldThrowGatewayNotFoundException() {
        List<Gate> gates = List.of(
                    new Gate("A", "a",
                            List.of(
                                new GateConnection(null, new Gate("B", "b", null), 1)
                            )
                        ),
                    new Gate("B", "b",
                        List.of()
                    )
                );

        when(gateRepository.findAllWithConnections()).thenReturn(gates);

        routeService.buildGraph();

        assertThrows(GateNotFoundException.class, () -> routeService.getCheapestRoute("A", "C"));
        verify(gateRepository).findAllWithConnections();
        verifyNoMoreInteractions(gateRepository);
    }

    @Test
    void getCheapestRoute_whenSoureceGateIsNull_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> routeService.getCheapestRoute(null, "B"));
    }

    @Test
    void getCheapestRoute_whenSourceGateIsBlank_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> routeService.getCheapestRoute("", "B"));
    }

    @Test
    void getCheapestRoute_whenDesteGateIsNull_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> routeService.getCheapestRoute("A", null));
    }

    @Test
    void getCheapestRoute_whenDesteGateIsBlank_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> routeService.getCheapestRoute("A", ""));
    }
}
