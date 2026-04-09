package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.dto.CityDto;
import com.codecool.solarwatch.dto.CityUpsertRequest;
import com.codecool.solarwatch.service.CityCrudService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/cities")
@RequiredArgsConstructor
public class AdminCityController {

    private final CityCrudService cityCrudService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CityDto> create(@Valid @RequestBody CityUpsertRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cityCrudService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CityDto> update(@PathVariable Long id, @Valid @RequestBody CityUpsertRequest request) {
        return ResponseEntity.ok(cityCrudService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cityCrudService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

