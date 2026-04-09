package com.codecool.solarwatch.dto;

public record SolarWatchResponse(
        String city,
        String date,
        String sunrise,
        String sunset,
        TimeZoneType timezone
) { }
