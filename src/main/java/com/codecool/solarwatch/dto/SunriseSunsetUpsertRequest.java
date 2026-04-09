package com.codecool.solarwatch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SunriseSunsetUpsertRequest(
        @NotNull Long cityId,
        @NotNull LocalDate date,
        @NotBlank String sunrise,
        @NotBlank String sunset
) {
}

