package com.hyperspacetunnelingcorp.routeplanner.model;

import java.math.BigDecimal;

public record CheapestTransport(Transport transport, BigDecimal cost) {

}
