package com.codecool.solarwatch.service.impl;

import com.codecool.solarwatch.dto.*;
import com.codecool.solarwatch.entity.City;
import com.codecool.solarwatch.entity.SunriseSunset;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SunriseSunsetRepository;
import com.codecool.solarwatch.service.GeocodingService;
import com.codecool.solarwatch.service.SolarWatchService;
import com.codecool.solarwatch.service.SunriseSunsetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class SolarWatchServiceImpl implements SolarWatchService {

    private final GeocodingService geocodingService;
    private final SunriseSunsetService sunriseSunsetService;
    private final CityRepository cityRepository;
    private final SunriseSunsetRepository sunriseSunsetRepository;

    public SolarWatchServiceImpl(
            GeocodingService geocodingService,
            SunriseSunsetService sunriseSunsetService,
            CityRepository cityRepository,
            SunriseSunsetRepository sunriseSunsetRepository
    ) {
        this.geocodingService = geocodingService;
        this.sunriseSunsetService = sunriseSunsetService;
        this.cityRepository = cityRepository;
        this.sunriseSunsetRepository = sunriseSunsetRepository;
    }

    @Override
    @Transactional
    public SolarWatchResponse getSunriseSunset(
            String city,
            LocalDate date,
            TimeZoneType timezone
    ) {
        City cityEntity = findOrCreateCity(city);
        SunriseSunset sunriseSunset = findOrCreateSunriseSunset(cityEntity, date);

        return new SolarWatchResponse(
                cityEntity.getName(),
                date.toString(),
                sunriseSunset.getSunrise(),
                sunriseSunset.getSunset(),
                timezone
        );
    }

    private City findOrCreateCity(String cityName) {
        return cityRepository.findFirstByNameIgnoreCase(cityName)
                .orElseGet(() -> {
                    GeocodingResponse geo = geocodingService.getGeocodingForCity(cityName);

                    City cityToSave = City.builder()
                            .name(geo.name())
                            .latitude(geo.lat())
                            .longitude(geo.lon())
                            .state(geo.state())
                            .country(geo.country())
                            .build();

                    return cityRepository.save(cityToSave);
                });
    }

    private SunriseSunset findOrCreateSunriseSunset(City city, LocalDate date) {
        return sunriseSunsetRepository.findByCityAndDate(city, date)
                .orElseGet(() -> {
                    SunriseSunsetTimes times = sunriseSunsetService.getSunriseSunset(
                            city.getLatitude(),
                            city.getLongitude(),
                            date
                    );

                    SunriseSunset entity = SunriseSunset.builder()
                            .city(city)
                            .date(date)
                            .sunrise(times.sunrise())
                            .sunset(times.sunset())
                            .build();

                    return sunriseSunsetRepository.save(entity);
                });
    }
}
