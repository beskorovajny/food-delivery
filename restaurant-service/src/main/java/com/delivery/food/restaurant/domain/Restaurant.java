package com.delivery.food.restaurant.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    private String description;

    @Column(precision = 5, scale = 2)
    private BigDecimal averageRating;       // 4.7 etc.

    private int reviewCount;

    @Column(nullable = false)
    private boolean active = true;

    private LocalTime openingTime;
    private LocalTime closingTime;
}
