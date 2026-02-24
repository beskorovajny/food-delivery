package com.delivery.food.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponseDto {

    private Long id;
    private String name;
    private String address;
    private String city;
    private String description;
    private BigDecimal averageRating;
    private Integer reviewCount;
    private boolean active;
    private LocalTime openingTime;
    private LocalTime closingTime;
}
