package com.hyperspacetunnelingcorp.routeplanner.exception;

public class RouteNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "No route found between %s and %s";

    public RouteNotFoundException(String source, String destination) {
        super(ERROR_MESSAGE.formatted(source, destination));
    }
}
