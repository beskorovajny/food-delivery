package com.delivery.food.restaurant.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
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
public class RestaurantUpdateDto {

    @Size(min = 3, max = 100)
    private String name;

    @Size(max = 255)
    private String address;

    @Size(max = 100)
    private String city;

    private String description;

    @PositiveOrZero
    @Digits(integer = 1, fraction = 1)
    private BigDecimal averageRating;

    private LocalTime openingTime;
    private LocalTime closingTime;
}
