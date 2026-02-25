package com.delivery.food.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryAddress {

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String house;

    private String apartment;

    @Column(nullable = false)
    private String city;

    private String comment;
}
