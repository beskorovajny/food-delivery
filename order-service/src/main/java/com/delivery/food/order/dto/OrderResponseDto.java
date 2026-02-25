package com.delivery.food.order.dto;

import com.delivery.food.order.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for returning detailed order information to the client.
 *
 * <p>Includes all order data: status, items, totals, timestamps.
 * Used for GET /orders/{id} and list responses.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {

    private Long id;

    private Long customerId;

    private Long restaurantId;

    private String restaurantName;

    private DeliveryAddressDto deliveryAddress;

    private OrderStatus status;

    private List<OrderItemResponseDto> items;

    private BigDecimal totalPrice;

    private BigDecimal deliveryFee;

    private BigDecimal serviceFee;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
