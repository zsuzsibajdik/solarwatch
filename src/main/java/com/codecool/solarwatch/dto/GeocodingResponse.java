package com.codecool.solarwatch.dto;

public record GeocodingResponse(
    String name,
    double lat,
    double lon,
    String state,
    String country
) { }
