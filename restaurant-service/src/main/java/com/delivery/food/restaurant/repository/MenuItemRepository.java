package com.delivery.food.restaurant.repository;

import com.delivery.food.restaurant.domain.MenuCategory;
import com.delivery.food.restaurant.domain.MenuItem;
import com.delivery.food.restaurant.domain.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link MenuItem} entity operations.
 *
 * <p>Provides methods for querying menu items by restaurant, category, availability, etc.
 * Menu items are the most frequently queried entities in restaurant-service.
 * </p>
 *
 * <p><strong>Production notes:</strong></p>
 * <ul>
 *     <li>Ensure indexes on restaurant_id, category_id, available for fast menu loading</li>
 *     <li>For search (by name/description) consider trigram index or full-text search later</li>
 *     <li>Cache popular menu items per restaurant (e.g. Redis) to reduce DB load</li>
 *     <li>Avoid N+1 queries — use JOIN FETCH or @EntityGraph when fetching with category/restaurant</li>
 * </ul>
 *
 * @see JpaRepository
 * @see Restaurant
 * @see MenuCategory
 * @see MenuItem
 */
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    /**
     * Finds all menu items belonging to a specific restaurant.
     *
     * <p>Used to load the full menu for a restaurant.
     * </p>
     *
     * @param restaurantId the owning restaurant identifier
     * @return list of menu items
     */
    Page<MenuItem> findAllByRestaurantId(Long restaurantId, Pageable pageable);

    /**
     * Finds all menu items in a specific category of a restaurant.
     *
     * <p>Commonly used to display items under one category (e.g. "Pizza").
     * </p>
     *
     * @param category the menu category
     * @return list of menu items in that category
     */
    List<MenuItem> findAllByCategory(MenuCategory category);

    /**
     * Finds available menu items for a restaurant with pagination.
     *
     * <p>Used for customer-facing menu display — only shows items marked as available.
     * Supports sorting by price, name, popularity, etc.
     * </p>
     *
     * @param restaurantId the restaurant identifier
     * @param pageable   pagination and sorting parameters
     * @return Page of available menu items
     */
    Page<MenuItem> findAllByRestaurantIdAndAvailableTrue(Long restaurantId, Pageable pageable);

    /**
     * Finds a menu item by name within a specific restaurant.
     *
     * <p>Useful for uniqueness checks during item creation.
     * Item names are typically unique per restaurant.
     * </p>
     *
     * @param name       the item name (case-sensitive)
     * @param restaurantId the owning restaurant identifier
     * @return Optional containing the item if found
     */
    Optional<MenuItem> findByNameAndRestaurantId(String name, Long restaurantId);
}
