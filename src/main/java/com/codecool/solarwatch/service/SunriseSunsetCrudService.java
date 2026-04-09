package com.codecool.solarwatch.service;

import com.codecool.solarwatch.dto.SunriseSunsetDto;
import com.codecool.solarwatch.dto.SunriseSunsetUpsertRequest;

import java.util.List;

public interface SunriseSunsetCrudService {

    List<SunriseSunsetDto> findAll();

    SunriseSunsetDto findById(Long id);

    SunriseSunsetDto create(SunriseSunsetUpsertRequest request);

    SunriseSunsetDto update(Long id, SunriseSunsetUpsertRequest request);

    void delete(Long id);
}

