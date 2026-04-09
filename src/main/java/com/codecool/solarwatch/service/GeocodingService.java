package com.codecool.solarwatch.service;

import com.codecool.solarwatch.dto.Coordinates;
import com.codecool.solarwatch.dto.GeocodingResponse;

public interface GeocodingService {
    Coordinates getCoordinatesForCity(String city);

    GeocodingResponse getGeocodingForCity(String city);
}
