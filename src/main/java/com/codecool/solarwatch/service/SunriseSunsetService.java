package com.codecool.solarwatch.service;

import com.codecool.solarwatch.dto.SunriseSunsetTimes;

import java.time.LocalDate;

public interface SunriseSunsetService {

    SunriseSunsetTimes getSunriseSunset(
            double latitude,
            double longitude,
            LocalDate date
    );
}
