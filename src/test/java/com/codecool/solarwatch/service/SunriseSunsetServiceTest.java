package com.codecool.solarwatch.service;

import com.codecool.solarwatch.dto.SunriseSunsetApiResponse;
import com.codecool.solarwatch.dto.SunriseSunsetResults;
import com.codecool.solarwatch.dto.SunriseSunsetTimes;
import com.codecool.solarwatch.exception.ExternalServiceException;
import com.codecool.solarwatch.service.impl.SunriseSunsetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SunriseSunsetServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private SunriseSunsetServiceImpl sunriseSunsetService;

    private static final String SUNRISE_API_URL = "https://api.sunrise-sunset.org/json";

    @BeforeEach
    void setUp() {
        sunriseSunsetService = new SunriseSunsetServiceImpl(webClient, SUNRISE_API_URL);
    }

    @Test
    void getSunriseSunset_whenValidResponse_returnsCorrectTimes() {
        SunriseSunsetResults results = new SunriseSunsetResults(
                "2026-04-05T01:23:00+00:00",
                "2026-04-05T13:45:00+00:00"
        );
        SunriseSunsetApiResponse apiResponse = new SunriseSunsetApiResponse(results, "OK");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(SunriseSunsetApiResponse.class))
                .thenReturn(Mono.just(apiResponse));

        SunriseSunsetTimes result = sunriseSunsetService.getSunriseSunset(
                47.4979, 19.0402, LocalDate.of(2026, 4, 5)
        );

        assertEquals("01:23", result.sunrise());
        assertEquals("13:45", result.sunset());
    }

    @Test
    void getSunriseSunset_whenApiReturnsNull_throwsExternalServiceException() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(SunriseSunsetApiResponse.class))
                .thenReturn(Mono.empty());

        assertThrows(ExternalServiceException.class, () ->
                sunriseSunsetService.getSunriseSunset(47.4979, 19.0402, LocalDate.of(2026, 4, 5))
        );
    }

    @Test
    void getSunriseSunset_whenApiReturnsNotOkStatus_throwsExternalServiceException() {
        SunriseSunsetResults results = new SunriseSunsetResults(
                "2026-04-05T01:23:00+00:00",
                "2026-04-05T13:45:00+00:00"
        );
        SunriseSunsetApiResponse apiResponse = new SunriseSunsetApiResponse(results, "ERROR");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(SunriseSunsetApiResponse.class))
                .thenReturn(Mono.just(apiResponse));

        assertThrows(ExternalServiceException.class, () ->
                sunriseSunsetService.getSunriseSunset(47.4979, 19.0402, LocalDate.of(2026, 4, 5))
        );
    }

    @Test
    void getSunriseSunset_whenWebClientThrowsException_throwsExternalServiceException() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(SunriseSunsetApiResponse.class))
                .thenThrow(new RuntimeException("Connection refused"));

        assertThrows(ExternalServiceException.class, () ->
                sunriseSunsetService.getSunriseSunset(47.4979, 19.0402, LocalDate.of(2026, 4, 5))
        );
    }
}