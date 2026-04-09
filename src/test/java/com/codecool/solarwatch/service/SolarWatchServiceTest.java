package com.codecool.solarwatch.service;

import com.codecool.solarwatch.dto.GeocodingResponse;
import com.codecool.solarwatch.dto.SolarWatchResponse;
import com.codecool.solarwatch.dto.SunriseSunsetTimes;
import com.codecool.solarwatch.dto.TimeZoneType;
import com.codecool.solarwatch.entity.City;
import com.codecool.solarwatch.entity.SunriseSunset;
import com.codecool.solarwatch.exception.CityNotFoundException;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SunriseSunsetRepository;
import com.codecool.solarwatch.service.impl.SolarWatchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolarWatchServiceTest {

    @Mock
    private GeocodingService geocodingService;

    @Mock
    private SunriseSunsetService sunriseSunsetService;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private SunriseSunsetRepository sunriseSunsetRepository;

    @InjectMocks
    private SolarWatchServiceImpl solarWatchService;

    private static final LocalDate DATE = LocalDate.of(2026, 4, 5);
    private static final String CITY_NAME = "Budapest";

    private City mockCity;

    @BeforeEach
    void setUp() {
        mockCity = City.builder()
                .name(CITY_NAME)
                .latitude(47.4979)
                .longitude(19.0402)
                .country("HU")
                .state("Budapest")
                .build();
    }

    @Test
    void getSunriseSunset_whenCityAndSunriseSunsetExistInDb_doesNotCallExternalApis() {
        SunriseSunset mockSunriseSunset = SunriseSunset.builder()
                .city(mockCity)
                .date(DATE)
                .sunrise("06:00")
                .sunset("18:00")
                .build();

        when(cityRepository.findFirstByNameIgnoreCase(CITY_NAME)).thenReturn(Optional.of(mockCity));
        when(sunriseSunsetRepository.findByCityAndDate(mockCity, DATE)).thenReturn(Optional.of(mockSunriseSunset));

        SolarWatchResponse result = solarWatchService.getSunriseSunset(CITY_NAME, DATE, TimeZoneType.LOCAL);

        assertEquals(CITY_NAME, result.city());
        assertEquals("06:00", result.sunrise());
        assertEquals("18:00", result.sunset());
        verifyNoInteractions(geocodingService);
        verifyNoInteractions(sunriseSunsetService);
    }

    @Test
    void getSunriseSunset_whenCityNotInDb_callsGeocodingApi() {
        GeocodingResponse geoResponse = new GeocodingResponse(CITY_NAME, 47.4979, 19.0402, "HU", "Budapest");
        SunriseSunset mockSunriseSunset = SunriseSunset.builder()
                .city(mockCity)
                .date(DATE)
                .sunrise("06:00")
                .sunset("18:00")
                .build();

        when(cityRepository.findFirstByNameIgnoreCase(CITY_NAME)).thenReturn(Optional.empty());
        when(geocodingService.getGeocodingForCity(CITY_NAME)).thenReturn(geoResponse);
        when(cityRepository.save(any(City.class))).thenReturn(mockCity);
        when(sunriseSunsetRepository.findByCityAndDate(mockCity, DATE)).thenReturn(Optional.of(mockSunriseSunset));

        SolarWatchResponse result = solarWatchService.getSunriseSunset(CITY_NAME, DATE, TimeZoneType.LOCAL);

        assertEquals(CITY_NAME, result.city());
        verify(geocodingService).getGeocodingForCity(CITY_NAME);
    }

    @Test
    void getSunriseSunset_whenSunriseSunsetNotInDb_callsSunriseSunsetApi() {
        SunriseSunsetTimes times = new SunriseSunsetTimes("06:00", "18:00");
        SunriseSunset savedEntity = SunriseSunset.builder()
                .city(mockCity)
                .date(DATE)
                .sunrise("06:00")
                .sunset("18:00")
                .build();

        when(cityRepository.findFirstByNameIgnoreCase(CITY_NAME)).thenReturn(Optional.of(mockCity));
        when(sunriseSunsetRepository.findByCityAndDate(mockCity, DATE)).thenReturn(Optional.empty());
        when(sunriseSunsetService.getSunriseSunset(mockCity.getLatitude(), mockCity.getLongitude(), DATE)).thenReturn(times);
        when(sunriseSunsetRepository.save(any(SunriseSunset.class))).thenReturn(savedEntity);

        SolarWatchResponse result = solarWatchService.getSunriseSunset(CITY_NAME, DATE, TimeZoneType.LOCAL);

        assertEquals("06:00", result.sunrise());
        assertEquals("18:00", result.sunset());
        verify(sunriseSunsetService).getSunriseSunset(mockCity.getLatitude(), mockCity.getLongitude(), DATE);
    }

    @Test
    void getSunriseSunset_whenCityNotFound_throwsException() {
        when(cityRepository.findFirstByNameIgnoreCase(CITY_NAME)).thenReturn(Optional.empty());
        when(geocodingService.getGeocodingForCity(CITY_NAME)).thenThrow(new CityNotFoundException(CITY_NAME));

        assertThrows(CityNotFoundException.class, () ->
                solarWatchService.getSunriseSunset(CITY_NAME, DATE, TimeZoneType.LOCAL)
        );
    }
}
