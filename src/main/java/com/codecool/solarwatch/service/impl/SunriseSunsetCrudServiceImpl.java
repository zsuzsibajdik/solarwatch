package com.codecool.solarwatch.service.impl;

import com.codecool.solarwatch.dto.SunriseSunsetDto;
import com.codecool.solarwatch.dto.SunriseSunsetUpsertRequest;
import com.codecool.solarwatch.entity.City;
import com.codecool.solarwatch.entity.SunriseSunset;
import com.codecool.solarwatch.exception.ResourceConflictException;
import com.codecool.solarwatch.exception.ResourceNotFoundException;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SunriseSunsetRepository;
import com.codecool.solarwatch.service.SunriseSunsetCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SunriseSunsetCrudServiceImpl implements SunriseSunsetCrudService {

    private final SunriseSunsetRepository sunriseSunsetRepository;
    private final CityRepository cityRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SunriseSunsetDto> findAll() {
        return sunriseSunsetRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SunriseSunsetDto findById(Long id) {
        SunriseSunset entity = sunriseSunsetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sunrise/sunset not found: id=" + id));
        return toDto(entity);
    }

    @Override
    public SunriseSunsetDto create(SunriseSunsetUpsertRequest request) {
        City city = cityRepository.findById(request.cityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found: id=" + request.cityId()));

        SunriseSunset toSave = SunriseSunset.builder()
                .city(city)
                .date(request.date())
                .sunrise(request.sunrise())
                .sunset(request.sunset())
                .build();

        try {
            return toDto(sunriseSunsetRepository.save(toSave));
        } catch (DataIntegrityViolationException ex) {
            throw new ResourceConflictException("Sunrise/sunset already exists for cityId=" + request.cityId() + " and date=" + request.date());
        }
    }

    @Override
    public SunriseSunsetDto update(Long id, SunriseSunsetUpsertRequest request) {
        SunriseSunset entity = sunriseSunsetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sunrise/sunset not found: id=" + id));

        City city = cityRepository.findById(request.cityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found: id=" + request.cityId()));

        entity.setCity(city);
        entity.setDate(request.date());
        entity.setSunrise(request.sunrise());
        entity.setSunset(request.sunset());

        try {
            return toDto(sunriseSunsetRepository.save(entity));
        } catch (DataIntegrityViolationException ex) {
            throw new ResourceConflictException("Sunrise/sunset already exists for cityId=" + request.cityId() + " and date=" + request.date());
        }
    }

    @Override
    public void delete(Long id) {
        if (!sunriseSunsetRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sunrise/sunset not found: id=" + id);
        }
        sunriseSunsetRepository.deleteById(id);
    }

    private SunriseSunsetDto toDto(SunriseSunset entity) {
        // Access city fields in a transactional context to avoid LazyInitialization issues
        City city = entity.getCity();
        return new SunriseSunsetDto(
                entity.getId(),
                city.getId(),
                city.getName(),
                entity.getDate(),
                entity.getSunrise(),
                entity.getSunset()
        );
    }
}

