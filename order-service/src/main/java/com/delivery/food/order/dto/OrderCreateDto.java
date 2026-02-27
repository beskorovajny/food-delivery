package com.delivery.food.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for creating a new order.
 *
 * <p>Used as input when a customer submits an order from the app.
 * Contains customer info, restaurant, delivery address, and list of items.
 * All fields are required except comment.
 * </p>
 *
 * <p>Example JSON:</p>
 * <pre>
 * {
 *   "customerId": 123,
 *   "restaurantId": 45,
 *   "deliveryAddress": {
 *     "street": "Doroshenka",
 *     "house": "12",
 *     "apartment": "5",
 *     "city": "Lviv",
 *     "comment": "Leave at the door"
 *   },
 *   "items": [
 *     { "menuItemId": 10, "quantity": 2 },
 *     { "menuItemId": 15, "quantity": 1 }
 *   ]
 * }
 * </pre>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDto {

    @NotNull(message = "Customer ID is required")
    @Positive(message = "Customer ID must be positive")
    private Long customerId;

    @NotNull(message = "Restaurant ID is required")
    @Positive(message = "Restaurant ID must be positive")
    private Long restaurantId;

    @NotNull(message = "Restaurant name is required")
    private String restaurantName;

    @NotNull(message = "Delivery address is required")
    @Valid
    private DeliveryAddressDto deliveryAddress;

    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemCreateDto> items;
}
