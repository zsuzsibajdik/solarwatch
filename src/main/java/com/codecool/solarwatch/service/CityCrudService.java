package com.codecool.solarwatch.service;

import com.codecool.solarwatch.dto.CityDto;
import com.codecool.solarwatch.dto.CityUpsertRequest;

import java.util.List;

public interface CityCrudService {
    List<CityDto> findAll();

    CityDto findById(Long id);

    CityDto create(CityUpsertRequest request);

    CityDto update(Long id, CityUpsertRequest request);

    void delete(Long id);
}

