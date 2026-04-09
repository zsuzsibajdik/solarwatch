package com.codecool.solarwatch.dto;

import java.time.LocalDate;

public record SunriseSunsetDto(
        Long id,
        Long cityId,
        String cityName,
        LocalDate date,
        String sunrise,
        String sunset
) {
}

