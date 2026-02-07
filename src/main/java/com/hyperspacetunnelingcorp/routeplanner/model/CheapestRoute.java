package com.hyperspacetunnelingcorp.routeplanner.model;

import java.math.BigDecimal;
import java.util.List;

public record CheapestRoute(List<String> route, int totalHu, BigDecimal costPerPassenger) {

}
