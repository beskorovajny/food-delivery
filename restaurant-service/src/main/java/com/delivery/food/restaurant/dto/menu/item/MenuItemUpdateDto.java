package com.delivery.food.restaurant.dto.menu.item;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemUpdateDto {

    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Positive(message = "Price must be positive")
    @Digits(integer = 10, fraction = 2, message = "Price format invalid (max 10 digits before decimal)")
    private BigDecimal price;

    @Size(max = 500)
    private String imageUrl;

    private Boolean available;
}
