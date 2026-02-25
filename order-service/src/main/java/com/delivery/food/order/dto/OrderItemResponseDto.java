package com.delivery.food.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDto {

    private Long id;

    private Long menuItemId;

    private String menuItemName;

    private String description;

    private BigDecimal priceAtOrderTime;

    private Integer quantity;

    private BigDecimal subtotal;
}
