package com.codecool.solarwatch.service.impl;

import com.codecool.solarwatch.dto.SunriseSunsetApiResponse;
import com.codecool.solarwatch.dto.SunriseSunsetTimes;
import com.codecool.solarwatch.exception.ExternalServiceException;
import com.codecool.solarwatch.service.SunriseSunsetService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;

@Service
public class SunriseSunsetServiceImpl implements SunriseSunsetService {

    private final WebClient webClient;
    private final String sunriseApiUrl;

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm");

    public SunriseSunsetServiceImpl(
            WebClient webClient,
            @Value("${sunrise.api.url}") String sunriseApiUrl
    ) {
        this.webClient = webClient;
        this.sunriseApiUrl = sunriseApiUrl;
    }

    @Override
    public SunriseSunsetTimes getSunriseSunset(
            double latitude,
            double longitude,
            LocalDate date
    ) {
        try {
            SunriseSunsetApiResponse response = webClient
                    .get()
                    .uri(sunriseApiUrl + "?lat=" + latitude + "&lng=" + longitude + "&date=" + date + "&formatted=0&tzid=UTC")
                    .retrieve()
                    .bodyToMono(SunriseSunsetApiResponse.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();

            if (response == null ||
                response.results() == null ||
                !"OK".equalsIgnoreCase(response.status())) {
                throw new ExternalServiceException("Invalid Sunrise/Sunset API response");
            }

            String sunrise = OffsetDateTime
                    .parse(response.results().sunrise())
                    .format(TIME_FORMAT);

            String sunset = OffsetDateTime
                    .parse(response.results().sunset())
                    .format(TIME_FORMAT);

            return new SunriseSunsetTimes(sunrise, sunset);

        } catch (WebClientResponseException ex) {
            throw new ExternalServiceException("Sunrise/Sunset API call failed", ex);
        } catch (RuntimeException ex) {
            throw new ExternalServiceException("Sunrise/Sunset API call failed", ex);
        }
    }
}
