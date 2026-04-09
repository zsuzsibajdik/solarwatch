package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.dto.SunriseSunsetDto;
import com.codecool.solarwatch.service.SunriseSunsetCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sunrise-sunsets")
@RequiredArgsConstructor
public class SunriseSunsetController {

    private final SunriseSunsetCrudService sunriseSunsetCrudService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<SunriseSunsetDto>> getAll() {
        return ResponseEntity.ok(sunriseSunsetCrudService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<SunriseSunsetDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(sunriseSunsetCrudService.findById(id));
    }
}

