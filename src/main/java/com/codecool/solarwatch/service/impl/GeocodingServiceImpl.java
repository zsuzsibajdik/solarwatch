package com.codecool.solarwatch.service.impl;

import com.codecool.solarwatch.dto.Coordinates;
import com.codecool.solarwatch.dto.GeocodingResponse;
import com.codecool.solarwatch.exception.CityNotFoundException;
import com.codecool.solarwatch.exception.ExternalServiceException;
import com.codecool.solarwatch.service.GeocodingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Objects;

@Service
public class GeocodingServiceImpl implements GeocodingService {

    private final WebClient webClient;
    private final String apiKey;
    private final String geocodingUrl;

    public GeocodingServiceImpl(
            WebClient webClient,
            @Value("${openweather.api.key}") String apiKey,
            @Value("${openweather.geocoding.url}") String geocodingUrl
    ) {
        this.webClient = webClient;
        this.apiKey = apiKey;
        this.geocodingUrl = geocodingUrl;
    }

    @Override
    public Coordinates getCoordinatesForCity(String city) {
        GeocodingResponse geo = getGeocodingForCity(city);
        return new Coordinates(geo.lat(), geo.lon());
    }

    @Override
    public GeocodingResponse getGeocodingForCity(String city) {
        try {
            GeocodingResponse[] response = webClient
                    .get()
                    .uri(geocodingUrl + "?q={city}&limit=1&appid={apiKey}", city, apiKey)
                    .retrieve()
                    .bodyToMono(GeocodingResponse[].class)
                    .block();

            if (response == null || response.length == 0) {
                throw new CityNotFoundException(city);
            }

            return Objects.requireNonNull(response)[0];
        } catch (WebClientResponseException ex) {
            throw new ExternalServiceException("Geocoding API call failed", ex);
        } catch (RuntimeException ex) {
            throw new ExternalServiceException("Geocoding API call failed", ex);
        }
    }
}
