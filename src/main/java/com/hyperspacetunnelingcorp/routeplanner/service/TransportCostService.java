package com.hyperspacetunnelingcorp.routeplanner.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.hyperspacetunnelingcorp.routeplanner.dto.CheapestTransportResponse;
import com.hyperspacetunnelingcorp.routeplanner.model.Transport;

@Service
public class TransportCostService {

    private static final int HSTC_MAX_PASSENGERS = 5;

    public CheapestTransportResponse calculateCheapestTransport(double distance, int passengers, int parking) {
        if (distance <= 0) {
            throw new IllegalArgumentException("Distance must be greater than 0");
        }

        if (passengers <= 0 || passengers > HSTC_MAX_PASSENGERS) {
            throw new IllegalArgumentException("Passengers must be between 1 and 5");
        }

        if (parking < 0) {
            throw new IllegalArgumentException("Parking must be greater than or equal to 0");
        }

        if (passengers == HSTC_MAX_PASSENGERS) {
            Transport transport = Transport.HSTC_TRANSPORT;
            BigDecimal cost = transport.getCostPerAU().multiply(BigDecimal.valueOf(distance));
            return new CheapestTransportResponse(transport, cost);
        }

        BigDecimal personalCost = calculatePersonalTransportCost(distance, passengers, parking);
        BigDecimal hstcCost = calculateHSTCTransportCost(distance, passengers);

        if (personalCost.compareTo(hstcCost) < 0) {
            return new CheapestTransportResponse(Transport.PERSONAL_TRANSPORT, personalCost);
        }

        return new CheapestTransportResponse(Transport.HSTC_TRANSPORT, hstcCost);
    }

    private BigDecimal calculatePersonalTransportCost(double distance, int passengers, int parking) {
        return Transport.PERSONAL_TRANSPORT.getCostPerAU().multiply(BigDecimal.valueOf(distance)).add(Transport.PERSONAL_TRANSPORT.getShipStorageCostPerDay().multiply(BigDecimal.valueOf(parking)));
    }

    private BigDecimal calculateHSTCTransportCost(double distance, int passengers) {
        return Transport.HSTC_TRANSPORT.getCostPerAU().multiply(BigDecimal.valueOf(distance));
    }    
}
