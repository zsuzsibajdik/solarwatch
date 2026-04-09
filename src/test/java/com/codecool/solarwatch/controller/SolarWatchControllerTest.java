package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.dto.SolarWatchResponse;
import com.codecool.solarwatch.dto.TimeZoneType;
import com.codecool.solarwatch.exception.CityNotFoundException;
import com.codecool.solarwatch.jwt.JwtUtil;
import com.codecool.solarwatch.service.SolarWatchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = SolarWatchController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class SolarWatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SolarWatchService solarWatchService;

    @MockitoBean
    private JwtUtil jwtUtil;

    private static final LocalDate DATE = LocalDate.of(2026, 4, 5);
    private static final String CITY_NAME = "Budapest";

    @Test
    void getSolarWatch_whenValidRequest_returnsOk() throws Exception {
        SolarWatchResponse mockResponse = new SolarWatchResponse(
                CITY_NAME,
                DATE.toString(),
                "06:00",
                "18:00",
                TimeZoneType.LOCAL
        );

        when(solarWatchService.getSunriseSunset(CITY_NAME, DATE, TimeZoneType.LOCAL))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/api/solar-watch")
                        .param("city", CITY_NAME)
                        .param("date", "2026-04-05")
                        .param("timezone", "LOCAL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value(CITY_NAME))
                .andExpect(jsonPath("$.sunrise").value("06:00"))
                .andExpect(jsonPath("$.sunset").value("18:00"));
    }

    @Test
    void getSolarWatch_whenNoTimezone_usesLocalAsDefault() throws Exception {
        SolarWatchResponse mockResponse = new SolarWatchResponse(
                CITY_NAME,
                DATE.toString(),
                "06:00",
                "18:00",
                TimeZoneType.LOCAL
        );

        when(solarWatchService.getSunriseSunset(CITY_NAME, DATE, TimeZoneType.LOCAL))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/api/solar-watch")
                        .param("city", CITY_NAME)
                        .param("date", "2026-04-05"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value(CITY_NAME));
    }

    @Test
    void getSolarWatch_whenCityNotFound_returnsErrorStatus() throws Exception {
        when(solarWatchService.getSunriseSunset(eq("Ismeretlen"), eq(DATE), any(TimeZoneType.class)))
                .thenThrow(new CityNotFoundException("Ismeretlen"));

        mockMvc.perform(get("/api/solar-watch")
                        .param("city", "Ismeretlen")
                        .param("date", "2026-04-05"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getSolarWatch_whenMissingCityParam_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/solar-watch")
                        .param("date", "2026-04-05"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSolarWatch_whenMissingDateParam_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/solar-watch")
                        .param("city", CITY_NAME))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSolarWatch_whenInvalidDateFormat_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/solar-watch")
                        .param("city", CITY_NAME)
                        .param("date", "nem-datum"))
                .andExpect(status().isBadRequest());
    }
}
