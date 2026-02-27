package com.delivery.food.restaurant.dto.menu.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuCategoryCreateDto {

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    private Integer displayOrder;
}
