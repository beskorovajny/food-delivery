package com.delivery.food.order.domain;

public enum OrderStatus {
    NEW,                    // just created
    CONFIRMED,              // restaurant accepted
    PREPARING,
    READY_FOR_PICKUP,
    IN_DELIVERY,
    DELIVERED,
    CANCELLED,
    REJECTED                // restaurant rejected
}
