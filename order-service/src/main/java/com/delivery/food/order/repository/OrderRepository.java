package com.delivery.food.order.repository;

import com.delivery.food.order.domain.Order;
import com.delivery.food.order.domain.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * Repository for {@link Order} entity operations.
 *
 * <p>Provides methods for querying orders by customer, restaurant, status, time range, and delivery city.
 * All list operations return paginated results to support large datasets.
 * </p>
 *
 * <p><strong>Production notes:</strong></p>
 * <ul>
 *     <li>Ensure indexes on customer_id, restaurant_id, status, created_at for performance</li>
 *     <li>For complex filtering (e.g. multiple statuses, price range), consider Specification or QueryDSL later</li>
 *     <li>Most queries should be read-only transactions (@Transactional(readOnly = true))</li>
 * </ul>
 *
 * @see JpaRepository
 * @see Order
 * @see OrderStatus
 */

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Retrieves paginated orders placed by a specific customer.
     *
     * @param customerId the ID of the customer
     * @param pageable   pagination and sorting parameters (e.g. sort by createdAt desc)
     * @return paginated list of orders for the customer
     */
    Page<Order> findAllByCustomerId(Long customerId, Pageable pageable);

    /**
     * Retrieves paginated orders belonging to a specific restaurant.
     *
     * @param restaurantId the ID of the restaurant
     * @param pageable     pagination and sorting parameters
     * @return paginated list of orders for the restaurant
     */
    Page<Order> findAllByRestaurantId(Long restaurantId, Pageable pageable);

    /**
     * Retrieves paginated orders filtered by a specific status.
     *
     * @param status   the order status to filter by
     * @param pageable pagination and sorting parameters
     * @return paginated list of orders in the given status
     */
    Page<Order> findAllByStatus(OrderStatus status, Pageable pageable);

    /**
     * Retrieves paginated orders for a restaurant filtered by status.
     * Useful for restaurant dashboards (e.g. "new orders", "preparing").
     *
     * @param restaurantId the restaurant ID
     * @param status       the status to filter by
     * @param pageable     pagination and sorting parameters
     * @return paginated list of matching orders
     */
    Page<Order> findAllByStatusAndRestaurantId(OrderStatus status, Long restaurantId, Pageable pageable);

    /**
     * Retrieves paginated orders for a customer filtered by status.
     * Useful for customer order history (e.g. "active", "completed").
     *
     * @param customerId the customer ID
     * @param status     the status to filter by
     * @param pageable   pagination and sorting parameters
     * @return paginated list of matching orders
     */
    Page<Order> findAllByStatusAndCustomerId(OrderStatus status, Long customerId, Pageable pageable);

    /**
     * Retrieves paginated orders created within a specific time range.
     * Useful for reports, analytics, or "recent orders".
     *
     * @param createdAtAfter  start of the time range (inclusive)
     * @param createdAtBefore end of the time range (exclusive)
     * @param pageable        pagination and sorting parameters
     * @return paginated list of orders in the time range
     */
    Page<Order> findAllByCreatedAtBetween(LocalDateTime createdAtAfter, LocalDateTime createdAtBefore, Pageable pageable);

    /**
     * Retrieves paginated orders where the delivery city contains the given string (case-insensitive).
     * Useful for admin searches or analytics by region.
     *
     * @param city     partial city name (e.g. "Lviv" matches "Lviv", "Lvivska")
     * @param pageable pagination and sorting parameters
     * @return paginated list of orders matching the city
     */
    @Query("SELECT o FROM Order o WHERE LOWER(o.deliveryAddress.city) LIKE LOWER(CONCAT('%', :city, '%'))")
    Page<Order> findAllByDeliveryAddressCityContainingIgnoreCase(String city, Pageable pageable);
}
