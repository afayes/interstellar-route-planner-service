package com.hyperspacetunnelingcorp.routeplanner.service;

import com.hyperspacetunnelingcorp.routeplanner.exception.GateNotFoundException;
import com.hyperspacetunnelingcorp.routeplanner.model.Gate;
import com.hyperspacetunnelingcorp.routeplanner.model.GateConnection;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.hyperspacetunnelingcorp.routeplanner.repository.GateRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

class GateServiceTest {

    @Mock
    private final GateRepository gateRepository = mock(GateRepository.class);

    private final GateService gateService = new GateService(gateRepository);

    @Test
    void getGates_whenRepositoryReturnsGates_thenReturnGates() {
        List<Gate> gatesExpected = List.of(new Gate("A", "a", List.of(new GateConnection(null, new Gate("B", "b", List.of()), 1))));
        when(gateRepository.findAllWithConnections()).thenReturn(gatesExpected);

        List<Gate> gatesActual = gateService.getGates();
    
        assertEquals(gatesExpected, gatesActual, "Gates do not match");
        verify(gateRepository).findAllWithConnections();
        verifyNoMoreInteractions(gateRepository);
    }

    @Test
    void getGate_whenRepositoryReturnsGate_thenReturnGate() {
        Gate gateExpected = new Gate("A", "a", List.of(new GateConnection(null, new Gate("B", "b", List.of()), 1)));
        when(gateRepository.findById("A")).thenReturn(Optional.of(gateExpected));

        Gate gateActual = gateService.getGate("A");

        assertEquals(gateExpected, gateActual, "Gate does not match");
        verify(gateRepository).findById("A");
        verifyNoMoreInteractions(gateRepository);
    }

    @Test
    void getGate_whenGateDoesNotExist_thenThrowGateNotFoundException() {
        when(gateRepository.findById("A")).thenReturn(Optional.empty());

        assertThrows(GateNotFoundException.class, () -> gateService.getGate("A"));
        verify(gateRepository).findById("A");
        verifyNoMoreInteractions(gateRepository);
    }

    @Test
    void getGate_whenGateIsNull_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> gateService.getGate(null));

        verifyNoMoreInteractions(gateRepository);
        
    }

    @Test
    void getGate_whenGateIsEmpty_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> gateService.getGate(""));

        verifyNoMoreInteractions(gateRepository);
    }
}
