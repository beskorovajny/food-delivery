package com.delivery.food.order.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long customerId;           // foreign key to user-service (no @ManyToOne)

    @Column(nullable = false)
    private Long restaurantId;         // foreign key to restaurant-service

    @Column(nullable = false)
    private String restaurantName;     // denormalized copy (for read performance)

    @Embedded
    private DeliveryAddress deliveryAddress;  // value object (see below)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.NEW;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    private BigDecimal deliveryFee;

    private BigDecimal serviceFee;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    // Helper methods
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void calculateTotal() {
        totalPrice = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(deliveryFee != null ? deliveryFee : BigDecimal.ZERO)
                .add(serviceFee != null ? serviceFee : BigDecimal.ZERO);
    }
}
