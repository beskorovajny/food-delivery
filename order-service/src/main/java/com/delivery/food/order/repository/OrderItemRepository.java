package com.delivery.food.order.repository;

import com.delivery.food.order.domain.Order;
import com.delivery.food.order.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for {@link OrderItem} entity operations.
 *
 * <p>Provides direct access to order items, typically used for reporting,
 * statistics, or bulk operations. Most business logic should go through
 * the owning {@link Order} aggregate via {@link Order#addItem(OrderItem)}.
 * </p>
 *
 * <p><strong>Production notes:</strong></p>
 * <ul>
 *     <li>Indexes on order_id and menu_item_id recommended</li>
 *     <li>Avoid direct deletes — prefer cascading from Order</li>
 *     <li>For complex reporting consider projections or QueryDSL later</li>
 * </ul>
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Finds all items belonging to a specific order.
     * Usually not needed — use {@code Order.getItems()} instead.
     */
    List<OrderItem> findAllByOrderId(Long orderId);

    /**
     * Finds all order items that reference a specific menu item (across all orders).
     * Useful for analytics ("how many times was this pizza ordered?").
     */
    List<OrderItem> findAllByMenuItemId(Long menuItemId);

    /**
     * Counts how many times a menu item was ordered (simple popularity metric).
     */
    long countByMenuItemId(Long menuItemId);
}
