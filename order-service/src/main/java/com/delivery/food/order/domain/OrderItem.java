package com.delivery.food.order.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    public OrderItem(Long menuItemId, String menuItemName, BigDecimal priceAtOrderTime, Integer quantity) {
        this.menuItemId = menuItemId;
        this.menuItemName = menuItemName;
        this.priceAtOrderTime = priceAtOrderTime;
        this.quantity = quantity != null ? quantity : 1;
        if (this.subtotal == null) {
            calculateSubtotal();
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Long menuItemId;           // foreign key to restaurant-service

    @Column(nullable = false)
    private String menuItemName;       // denormalized copy

    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtOrderTime;

    @Column(nullable = false)
    private Integer quantity = 1;

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal;

    @PrePersist
    @PreUpdate
    public void calculateSubtotal() {
        if (priceAtOrderTime != null && quantity != null) {
            this.subtotal = priceAtOrderTime.multiply(BigDecimal.valueOf(quantity));
        } else {
            this.subtotal = BigDecimal.ZERO; // fallback
        }
    }
}
