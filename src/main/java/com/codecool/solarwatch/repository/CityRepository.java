package com.codecool.solarwatch.repository;

import com.codecool.solarwatch.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findFirstByNameIgnoreCase(String name);

    Optional<City> findFirstByNameIgnoreCaseAndStateIgnoreCaseAndCountryIgnoreCase(
            String name,
            String state,
            String country
    );
}

