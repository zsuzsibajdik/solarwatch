package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.dto.CityDto;
import com.codecool.solarwatch.service.CityCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityCrudService cityCrudService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<CityDto>> getAll() {
        return ResponseEntity.ok(cityCrudService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<CityDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cityCrudService.findById(id));
    }
}

