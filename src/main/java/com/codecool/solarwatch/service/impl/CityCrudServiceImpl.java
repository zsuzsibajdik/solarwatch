package com.codecool.solarwatch.service.impl;

import com.codecool.solarwatch.dto.CityDto;
import com.codecool.solarwatch.dto.CityUpsertRequest;
import com.codecool.solarwatch.entity.City;
import com.codecool.solarwatch.exception.ResourceNotFoundException;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.service.CityCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CityCrudServiceImpl implements CityCrudService {

    private final CityRepository cityRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CityDto> findAll() {
        return cityRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CityDto findById(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found: id=" + id));
        return toDto(city);
    }

    @Override
    public CityDto create(CityUpsertRequest request) {
        City toSave = City.builder()
                .name(request.name())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .state(request.state())
                .country(request.country())
                .build();

        return toDto(cityRepository.save(toSave));
    }

    @Override
    public CityDto update(Long id, CityUpsertRequest request) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found: id=" + id));

        city.setName(request.name());
        city.setLatitude(request.latitude());
        city.setLongitude(request.longitude());
        city.setState(request.state());
        city.setCountry(request.country());

        return toDto(cityRepository.save(city));
    }

    @Override
    public void delete(Long id) {
        if (!cityRepository.existsById(id)) {
            throw new ResourceNotFoundException("City not found: id=" + id);
        }
        cityRepository.deleteById(id);
    }

    private CityDto toDto(City city) {
        return new CityDto(
                city.getId(),
                city.getName(),
                city.getLatitude(),
                city.getLongitude(),
                city.getState(),
                city.getCountry()
        );
    }
}

