package com.delivery.food.restaurant.dto.menu.item;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemCreateDto {

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    private String description;

    @NotNull
    @Positive
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;

    private String imageUrl;

    private boolean available = true;
}
