package com.codecool.solarwatch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CityUpsertRequest(
        @NotBlank String name,
        @NotNull Double latitude,
        @NotNull Double longitude,
        String state,
        @NotBlank @Size(min = 2, max = 2) String country
) {
}

