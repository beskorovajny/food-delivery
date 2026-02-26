package com.delivery.food.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

/**
 * DTO for partially updating an existing order (customer can change delivery details).
 *
 * <p>Only address is allowed to be updated by customer.
 * Status changes must use dedicated endpoint.
 * </p>
 */

public class OrderPatchDto {

    @Valid
    private DeliveryAddressDto deliveryAddress;

}
