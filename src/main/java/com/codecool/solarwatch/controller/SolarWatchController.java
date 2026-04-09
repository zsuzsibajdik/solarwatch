package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.dto.SolarWatchResponse;
import com.codecool.solarwatch.dto.TimeZoneType;
import com.codecool.solarwatch.service.SolarWatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class SolarWatchController {

    private static final Logger log = LoggerFactory.getLogger(SolarWatchController.class);

    private final SolarWatchService solarWatchService;

    public SolarWatchController(SolarWatchService solarWatchService) {
        this.solarWatchService = solarWatchService;
    }

    @GetMapping("/solar-watch")
    public ResponseEntity<SolarWatchResponse> getSolarWatch(
            @RequestParam String city,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate date,
            @RequestParam(required = false, defaultValue = "LOCAL")TimeZoneType timezone
    ) {
        log.info("Incoming request: city={}, date={}, timezone={}", city, date, timezone);

        SolarWatchResponse response = solarWatchService.getSunriseSunset(city, date, timezone);
        return ResponseEntity.ok(response);
    }

}
