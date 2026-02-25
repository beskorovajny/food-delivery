package com.delivery.food.order.service;

import com.delivery.food.common.exception.EntityNotFoundException;
import com.delivery.food.common.exception.InvalidOperationException;
import com.delivery.food.order.domain.Order;
import com.delivery.food.order.domain.OrderStatus;
import com.delivery.food.order.dto.OrderCreateDto;
import com.delivery.food.order.dto.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service for managing orders in the food delivery system.
 *
 * <p>This service handles order creation, retrieval, status updates, and cancellations.
 * It enforces business rules such as:
 * <ul>
 *     <li>Order must contain at least one item</li>
 *     <li>Items must be available from the restaurant</li>
 *     <li>Status transitions must be valid (e.g. cannot cancel delivered order)</li>
 * </ul>
 * </p>
 *
 * <p>All write operations should be transactional.
 * Read operations use readOnly=true for performance.
 * </p>
 *
 * @see Order
 * @see OrderCreateDto
 * @see OrderResponseDto
 */
public interface OrderService {

    /**
     * Creates a new order from the provided data.
     *
     * <p>Validates:
     * <ul>
     *     <li>Customer exists</li>
     *     <li>Restaurant exists</li>
     *     <li>All menu items exist and are available</li>
     *     <li>Total price calculation</li>
     * </ul>
     * Sets initial status to {@link OrderStatus#NEW}.
     * </p>
     *
     * @param dto the order creation data (customerId, restaurantId, items, address)
     * @return created order details
     * @throws EntityNotFoundException if customer or restaurant not found
     * @throws InvalidOperationException if items are unavailable or invalid
     */
    OrderResponseDto createOrder(OrderCreateDto dto);

    /**
     * Retrieves detailed information about a specific order.
     *
     * @param orderId the order identifier
     * @return order details including items and address
     * @throws EntityNotFoundException if order not found
     */
    OrderResponseDto getOrderById(Long orderId);

    /**
     * Retrieves paginated list of orders for a specific customer.
     *
     * @param customerId the customer identifier
     * @param pageable   pagination and sorting parameters (default: createdAt desc)
     * @return paginated list of orders
     */
    Page<OrderResponseDto> getOrdersByCustomer(Long customerId, Pageable pageable);

    /**
     * Retrieves paginated list of orders for a specific restaurant.
     *
     * @param restaurantId the restaurant identifier
     * @param pageable     pagination and sorting parameters
     * @return paginated list of orders
     */
    Page<OrderResponseDto> getOrdersByRestaurant(Long restaurantId, Pageable pageable);

    /**
     * Retrieves paginated list of orders filtered by status.
     *
     * @param status   the order status to filter by
     * @param pageable pagination and sorting parameters
     * @return paginated list of matching orders
     */
    Page<OrderResponseDto> getOrdersByStatus(OrderStatus status, Pageable pageable);

    /**
     * Updates the status of an existing order.
     *
     * <p>Validates state transitions (e.g. cannot go back from DELIVERED to PREPARING).
     * Can be called by restaurant (CONFIRMED → PREPARING → READY_FOR_PICKUP)
     * or delivery service (IN_DELIVERY → DELIVERED).
     * </p>
     *
     * @param orderId the order identifier
     * @param newStatus the new status to set
     * @return updated order details
     * @throws EntityNotFoundException if order not found
     * @throws InvalidOperationException if status transition is invalid
     */
    OrderResponseDto updateOrderStatus(Long orderId, OrderStatus newStatus);

    /**
     * Cancels an order (customer or restaurant initiated).
     *
     * <p>Only allowed for orders in NEW or CONFIRMED status.
     * Sets status to CANCELLED and may trigger refund logic.
     * </p>
     *
     * @param orderId the order identifier
     * @param reason optional reason for cancellation
     * @throws EntityNotFoundException if order not found
     * @throws InvalidOperationException if order cannot be cancelled (already delivered, etc.)
     */
    void cancelOrder(Long orderId, String reason);

    /**
     * Retrieves paginated list of active (not delivered/cancelled) orders for a customer.
     *
     * @param customerId the customer identifier
     * @param pageable   pagination and sorting parameters
     * @return paginated list of active orders
     */
    Page<OrderResponseDto> getActiveOrdersByCustomer(Long customerId, Pageable pageable);
}
