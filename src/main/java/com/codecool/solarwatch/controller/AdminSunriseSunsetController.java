package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.dto.SunriseSunsetDto;
import com.codecool.solarwatch.dto.SunriseSunsetUpsertRequest;
import com.codecool.solarwatch.service.SunriseSunsetCrudService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/sunrise-sunsets")
@RequiredArgsConstructor
public class AdminSunriseSunsetController {

    private final SunriseSunsetCrudService sunriseSunsetCrudService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SunriseSunsetDto> create(@Valid @RequestBody SunriseSunsetUpsertRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sunriseSunsetCrudService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SunriseSunsetDto> update(
            @PathVariable Long id,
            @Valid @RequestBody SunriseSunsetUpsertRequest request
    ) {
        return ResponseEntity.ok(sunriseSunsetCrudService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sunriseSunsetCrudService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

