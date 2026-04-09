package com.codecool.solarwatch.exception;

public class CityNotFoundException extends RuntimeException {
    public CityNotFoundException(String city) {
        super("City not found: " + city);
    }
}
