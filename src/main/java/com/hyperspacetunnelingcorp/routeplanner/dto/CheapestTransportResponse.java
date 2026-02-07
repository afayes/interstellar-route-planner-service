package com.hyperspacetunnelingcorp.routeplanner.dto;

import java.math.BigDecimal;

import com.hyperspacetunnelingcorp.routeplanner.model.Transport;

public record CheapestTransportResponse(Transport transport, BigDecimal cost) {

}
