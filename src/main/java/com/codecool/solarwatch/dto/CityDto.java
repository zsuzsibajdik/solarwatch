package com.codecool.solarwatch.dto;

public record CityDto(
        Long id,
        String name,
        double latitude,
        double longitude,
        String state,
        String country
) {
}

