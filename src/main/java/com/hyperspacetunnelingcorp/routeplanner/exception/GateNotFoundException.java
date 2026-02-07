package com.hyperspacetunnelingcorp.routeplanner.exception;

public class GateNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Gate not found: %s";

    public GateNotFoundException(String gateCode) {
        super(ERROR_MESSAGE.formatted(gateCode));
    }

}
