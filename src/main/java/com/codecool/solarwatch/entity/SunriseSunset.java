package com.codecool.solarwatch.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "sunrise_sunset",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_sunrise_sunset_city_date",
                columnNames = {"city_id", "date"}
        )
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SunriseSunset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String sunrise;

    @Column(nullable = false)
    private String sunset;
}


