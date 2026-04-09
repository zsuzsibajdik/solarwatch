package com.codecool.solarwatch.service;

import com.codecool.solarwatch.dto.SolarWatchResponse;
import com.codecool.solarwatch.dto.TimeZoneType;

import java.time.LocalDate;

public interface SolarWatchService {
    SolarWatchResponse getSunriseSunset(String city, LocalDate date, TimeZoneType timezone);
}
