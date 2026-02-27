package com.delivery.food.order.dto;

import com.delivery.food.order.domain.OrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating order status (e.g. confirm, prepare, deliver, cancel).
 *
 * <p>Used by restaurant, delivery, or admin.
 * </p>
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdateDto {
    @NotNull
    private OrderStatus newStatus;
}
