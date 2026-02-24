package com.delivery.food.restaurant.repository;

import com.delivery.food.restaurant.domain.MenuCategory;
import com.delivery.food.restaurant.domain.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link MenuCategory} entity operations.
 *
 * <p>Provides methods to query menu categories, typically filtered by restaurant.
 * Categories are usually small in number per restaurant (5–20), so most queries
 * are not paginated by default, but pagination is available for consistency.
 * </p>
 *
 * <p><strong>Production notes:</strong></p>
 * <ul>
 *     <li>Ensure index on restaurant_id + display_order for fast restaurant menu loading</li>
 *     <li>For very large menus, consider caching categories per restaurant (Redis / Caffeine)</li>
 *     <li>Use {@link org.springframework.data.jpa.repository.JpaSpecificationExecutor} if complex filters needed later</li>
 * </ul>
 *
 * @see JpaRepository
 * @see Restaurant
 * @see MenuCategory
 */
public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {
    /**
     * Finds all categories belonging to a specific restaurant.
     *
     * <p>Typically used to load the full menu structure for a restaurant.
     * Results are ordered by {@code displayOrder} (if set) or insertion order.
     * </p>
     *
     * @param restaurantId the restaurant identifier whose categories to retrieve
     * @return list of categories (usually small, no pagination needed in most cases)
     */
    List<MenuCategory> findAllByRestaurant(Long restaurantId);

    /**
     * Finds a category by name within a specific restaurant.
     *
     * <p>Useful for uniqueness checks during category creation.
     * Category names are usually unique per restaurant.
     * </p>
     *
     * @param name       the category name (case-sensitive)
     * @param restaurantId the restaurant identifier
     * @return Optional containing the category if found
     */
    Optional<MenuCategory> findByNameAndRestaurant(String name, Long restaurantId);

    /**
     * Retrieves paginated categories for a restaurant.
     *
     * <p>Useful when displaying categories in admin UI or when supporting very large menus.
     * Results are sorted according to the provided {@link Pageable}.
     * </p>
     *
     * @param restaurant the restaurant to filter by
     * @param pageable   pagination and sorting parameters
     * @return Page of categories
     */
    Page<MenuCategory> findAllByRestaurant(Restaurant restaurant, Pageable pageable);
}
