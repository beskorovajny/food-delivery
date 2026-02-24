package com.delivery.food.restaurant.dto.menu.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuCategoryResponseDto {

    private Long id;
    private String name;
    private Integer displayOrder;
    private Long restaurantId;
}
