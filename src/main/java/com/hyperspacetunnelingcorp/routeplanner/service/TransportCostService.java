package com.hyperspacetunnelingcorp.routeplanner.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.hyperspacetunnelingcorp.routeplanner.model.CheapestTransport;
import com.hyperspacetunnelingcorp.routeplanner.model.Transport;

@Service
public class TransportCostService {

    private static final int HSTC_MAX_PASSENGERS = 5;

    public CheapestTransport calculateCheapestTransport(double distance, int passengers, int parkingDays) {
        if (distance < 1) {
            throw new IllegalArgumentException("Distance cannot be less than 1");
        }

        if (passengers < 1 || passengers > HSTC_MAX_PASSENGERS) {
            throw new IllegalArgumentException("Passengers must be between 1 and 5");
        }

        if (parkingDays < 0) {
            throw new IllegalArgumentException("Parking cannot be less than 0");
        }

        // if there are 5 passengers or days of parking is 0 then use HSTC
        if (passengers == HSTC_MAX_PASSENGERS || parkingDays == 0) {
            Transport transport = Transport.HSTC_TRANSPORT;
            BigDecimal cost = calculateHSTCTransportCost(distance);
            return new CheapestTransport(transport, cost.setScale(2, RoundingMode.HALF_UP));
        }

        BigDecimal personalCost = calculatePersonalTransportCost(distance, parkingDays);
        BigDecimal hstcCost = calculateHSTCTransportCost(distance);

        if (personalCost.compareTo(hstcCost) < 0) {
            return new CheapestTransport(Transport.PERSONAL_TRANSPORT, personalCost.setScale(2, RoundingMode.HALF_UP));
        }

        return new CheapestTransport(Transport.HSTC_TRANSPORT, hstcCost.setScale(2, RoundingMode.HALF_UP));
    }

    private BigDecimal calculatePersonalTransportCost(double distance, int parking) {
        return Transport.PERSONAL_TRANSPORT.getCostPerAU()
                .multiply(BigDecimal.valueOf(distance))
                .add(Transport.PERSONAL_TRANSPORT.getShipStorageCostPerDay().multiply(BigDecimal.valueOf(parking)));
    }

    private BigDecimal calculateHSTCTransportCost(double distance) {
        return Transport.HSTC_TRANSPORT.getCostPerAU().multiply(BigDecimal.valueOf(distance));
    }    
}
