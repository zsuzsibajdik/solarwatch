package com.codecool.solarwatch.dto;

public record SunriseSunsetApiResponse(
        SunriseSunsetResults results,
        String status
) { }
