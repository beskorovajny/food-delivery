package com.delivery.food.restaurant.service;

import com.delivery.food.restaurant.domain.MenuCategory;
import com.delivery.food.restaurant.domain.MenuItem;
import com.delivery.food.restaurant.domain.Restaurant;
import com.delivery.food.restaurant.dto.RestaurantCreateDto;
import com.delivery.food.restaurant.dto.RestaurantResponseDto;
import com.delivery.food.restaurant.dto.RestaurantUpdateDto;
import com.delivery.food.restaurant.dto.menu.category.MenuCategoryCreateDto;
import com.delivery.food.restaurant.dto.menu.category.MenuCategoryResponseDto;
import com.delivery.food.restaurant.dto.menu.category.MenuCategoryUpdateDto;
import com.delivery.food.restaurant.dto.menu.item.MenuItemCreateDto;
import com.delivery.food.restaurant.dto.menu.item.MenuItemResponseDto;
import com.delivery.food.restaurant.dto.menu.item.MenuItemUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for managing the restaurant bounded context.
 *
 * <p>This service coordinates operations across {@link Restaurant}, {@link MenuCategory},
 * and {@link MenuItem} entities. It enforces business rules such as:
 * <ul>
 *     <li>Restaurant/category/item names uniqueness within their scope</li>
 *     <li>Availability checks for menu items</li>
 *     <li>Consistency between parent-child relationships</li>
 * </ul>
 * </p>
 *
 * <p>All write operations should be wrapped in {@code @Transactional} in the implementation.
 * Read operations use {@code @Transactional(readOnly = true)} for performance optimization.
 * </p>
 *
 * <p><strong>Production considerations:</strong></p>
 * <ul>
 *     <li>Use caching (e.g. {@code @Cacheable}) for frequently read data (restaurant menus)</li>
 *     <li>Implement rate limiting on public endpoints</li>
 *     <li>Secure write operations with role-based authorization (e.g. restaurant owner or admin)</li>
 *     <li>Log important business events (creation, deletion, status changes)</li>
 *     <li>For large menus, consider adding search/filter capabilities via Specification or QueryDSL</li>
 * </ul>
 *
 * @see Restaurant
 * @see MenuCategory
 * @see MenuItem
 */
public interface RestaurantService {
    // Restaurant operations
    /**
     * Creates a new restaurant.
     *
     * <p>Validates input data, checks for name/address conflicts if required by business rules,
     * and persists the new restaurant.
     * </p>
     *
     * @param dto the data to create the restaurant from
     * @return the created restaurant with generated ID
     * @throws jakarta.validation.ConstraintViolationException if validation fails
     * @throws IllegalArgumentException if business rules are violated (e.g. duplicate name)
     */
    RestaurantResponseDto createRestaurant(RestaurantCreateDto dto);

    /**
     * Retrieves a restaurant by its ID.
     *
     * @param id the restaurant identifier
     * @return detailed restaurant information
     * @throws jakarta.persistence.EntityNotFoundException if restaurant not found
     */
    RestaurantResponseDto getRestaurantById(Long id);

    /**
     * Retrieves a paginated list of all restaurants.
     *
     * <p>Supports sorting and pagination via {@link Pageable}.
     * In production, consider adding filters (city, cuisine, rating, open now) via query parameters.
     * </p>
     *
     * @param pageable pagination and sorting parameters
     * @return paginated list of restaurant summaries
     */
    Page<RestaurantResponseDto> getAllRestaurants(Pageable pageable);

    /**
     * Updates an existing restaurant with partial data.
     *
     * <p>Only non-null fields from the DTO are applied.
     * Name/address changes may trigger additional business validation.
     * </p>
     *
     * @param id  the restaurant identifier
     * @param dto partial update data
     * @return updated restaurant details
     * @throws jakarta.persistence.EntityNotFoundException if restaurant not found
     */
    RestaurantResponseDto updateRestaurant(Long id, RestaurantUpdateDto dto);

    /**
     * Deletes (or deactivates) a restaurant.
     *
     * <p>Implementation may choose soft-delete (set active=false) or hard-delete.
     * In production, prefer soft-delete to preserve historical data.
     * Cascading deletion of menu categories/items should be handled carefully.
     * </p>
     *
     * @param id the restaurant identifier
     * @throws jakarta.persistence.EntityNotFoundException if restaurant not found
     */
    void deleteRestaurant(Long id);

    // Menu Category operations
    /**
     * Creates a new menu category for a specific restaurant.
     *
     * @param restaurantId the owning restaurant identifier
     * @param dto          the category data
     * @return created category with generated ID
     * @throws jakarta.persistence.EntityNotFoundException if restaurant not found
     * @throws IllegalArgumentException if category name already exists in the restaurant
     */
    MenuCategoryResponseDto createCategory(Long restaurantId, MenuCategoryCreateDto dto);

    /**
     * Retrieves all menu categories for a given restaurant.
     *
     * <p>Categories are typically few (5–20) — no pagination by default.
     * Sorted by display order or creation time.
     * </p>
     *
     * @param restaurantId the restaurant identifier
     * @return list of categories
     * @throws jakarta.persistence.EntityNotFoundException if restaurant not found
     */
    List<MenuCategoryResponseDto> getCategoriesByRestaurant(Long restaurantId);

    /**
     * Updates an existing menu category.
     *
     * @param menuCategoryId the category identifier
     * @param dto            partial update data
     * @return updated category details
     * @throws jakarta.persistence.EntityNotFoundException if category not found
     */
    MenuCategoryResponseDto updateMenuCategory(Long menuCategoryId, MenuCategoryUpdateDto dto);

    /**
     * Deletes a menu category.
     *
     * <p>May cascade to delete items (business decision) or throw if items exist.
     * In production, prefer soft-delete or move items to another category.
     * </p>
     *
     * @param categoryId the category identifier
     * @throws jakarta.persistence.EntityNotFoundException if category not found
     */
    void deleteCategory(Long categoryId);

    // Menu Item operations
    /**
     * Creates a new menu item in a specific category of a restaurant.
     *
     * @param restaurantId the owning restaurant identifier
     * @param categoryId   the target category identifier
     * @param dto          the item data
     * @return created menu item with generated ID
     * @throws jakarta.persistence.EntityNotFoundException if restaurant or category not found
     * @throws IllegalArgumentException if item name already exists in the restaurant
     */
    MenuItemResponseDto createMenuItem(Long restaurantId, Long categoryId, MenuItemCreateDto dto);

    /**
     * Retrieves paginated menu items for a restaurant.
     *
     * <p>Typically used for customer-facing menu display.
     * Can be filtered to available items only in implementation.
     * </p>
     *
     * @param restaurantId the restaurant identifier
     * @param pageable     pagination and sorting parameters
     * @return paginated list of menu items
     * @throws jakarta.persistence.EntityNotFoundException if restaurant not found
     */
    Page<MenuItemResponseDto> getMenuItemsByRestaurant(Long restaurantId, Pageable pageable);

    /**
     * Updates an existing menu item.
     *
     * @param itemId the menu item identifier
     * @param dto    partial update data
     * @return updated menu item details
     * @throws jakarta.persistence.EntityNotFoundException if item not found
     */
    MenuItemResponseDto updateMenuItem(Long itemId, MenuItemUpdateDto dto);

    /**
     * Deletes a menu item.
     *
     * <p>Prefer soft-delete (set available=false) in production to preserve sales history.
     * Hard-delete is acceptable only if no historical data is needed.
     * </p>
     *
     * @param itemId the menu item identifier
     * @throws jakarta.persistence.EntityNotFoundException if item not found
     */
    void deleteMenuItem(Long itemId);
}
