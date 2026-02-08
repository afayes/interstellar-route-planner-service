package com.hyperspacetunnelingcorp.routeplanner.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.hyperspacetunnelingcorp.routeplanner.model.CheapestTransport;
import com.hyperspacetunnelingcorp.routeplanner.model.Transport;

class TransportCostServiceTest {

    private final TransportCostService transportCostService = new TransportCostService();

    // boundary test - hstc is less when distance is 33.32 (less than 33.33)
    @Test
    void calculateCheapestTransport_whenHstcIsLess_shouldReturnHstc() {
        CheapestTransport calculateCheapestTransport = transportCostService.calculateCheapestTransport(33.32, 3, 1);
        assertEquals(new CheapestTransport(Transport.HSTC_TRANSPORT, BigDecimal.valueOf(14.99)), calculateCheapestTransport);

    }

    // boundary test - personal is less when distance is 33.34 (greater than 33.33)
    @Test
    void calculateCheapestTransport_whenPersonalIsLess_shouldReturnPersonal() {
        CheapestTransport calculateCheapestTransport = transportCostService.calculateCheapestTransport(33.34, 3, 1);
        assertEquals(new CheapestTransport(Transport.PERSONAL_TRANSPORT, BigDecimal.valueOf(15.00).setScale(2)), calculateCheapestTransport);

    }

    @Test
    void calculateCheapestTransport_whenPassengerCountIs5_shouldReturnHSTC() {
        CheapestTransport calculateCheapestTransport = transportCostService.calculateCheapestTransport(1, 5, 1);
        assertEquals(new CheapestTransport(Transport.HSTC_TRANSPORT, BigDecimal.valueOf(0.45)), calculateCheapestTransport);
    }

    @Test
    void calculateCheapestTransport_whenParkingIs0_shouldReturnHSTC() {
        CheapestTransport calculateCheapestTransport = transportCostService.calculateCheapestTransport(1, 1, 0);
        assertEquals(Transport.HSTC_TRANSPORT, calculateCheapestTransport.transport());
    }

    @Test
    void calculateCheapestTransport_whenDistanceIsNegative_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> transportCostService.calculateCheapestTransport(-1, 5, 1));
    }

    @Test
    void calculateCheapestTransport_whenPassengerCountIsLessThan1_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> transportCostService.calculateCheapestTransport(1, 0, 1));
    }

    @Test
    void calculateCheapestTransport_whenPassengerCountGreaterThan5_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> transportCostService.calculateCheapestTransport(1, 6, 1));
    }

    @Test
    void calculateCheapestTransport_whenParkingIsLessThan0_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> transportCostService.calculateCheapestTransport(1, 1, -1));
    }
}
