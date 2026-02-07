package com.hyperspacetunnelingcorp.routeplanner.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

@Getter
public enum Transport {

    PERSONAL_TRANSPORT("Personal Transport", BigDecimal.valueOf(0.30), BigDecimal.valueOf(5)),
    HSTC_TRANSPORT("HSTC Transport", BigDecimal.valueOf(0.45), BigDecimal.valueOf(0));

    @JsonValue
    private final String name;
    private final BigDecimal costPerAU;
    private final BigDecimal shipStorageCostPerDay;

    Transport(String name, BigDecimal costPerAU, BigDecimal shipStorageCostPerDay) {
        this.name = name;
        this.costPerAU = costPerAU;
        this.shipStorageCostPerDay = shipStorageCostPerDay;
    }
}
