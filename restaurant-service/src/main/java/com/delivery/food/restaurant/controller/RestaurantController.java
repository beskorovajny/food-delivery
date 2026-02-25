package com.delivery.food.restaurant.controller;

import com.delivery.food.restaurant.dto.RestaurantCreateDto;
import com.delivery.food.restaurant.dto.RestaurantResponseDto;
import com.delivery.food.restaurant.dto.RestaurantUpdateDto;
import com.delivery.food.restaurant.dto.menu.category.MenuCategoryCreateDto;
import com.delivery.food.restaurant.dto.menu.category.MenuCategoryResponseDto;
import com.delivery.food.restaurant.dto.menu.category.MenuCategoryUpdateDto;
import com.delivery.food.restaurant.dto.menu.item.MenuItemCreateDto;
import com.delivery.food.restaurant.dto.menu.item.MenuItemResponseDto;
import com.delivery.food.restaurant.dto.menu.item.MenuItemUpdateDto;
import com.delivery.food.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for restaurant-related operations.
 *
 * <p>Exposes endpoints for managing restaurants, their menu categories, and menu items.
 * All write operations require authentication and appropriate roles (e.g. restaurant owner or admin)
 * — implement security with Spring Security later.
 * </p>
 *
 * <p><strong>Base path:</strong> {@code /api/restaurants}</p>
 *
 * @see RestaurantService
 */
@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    // ──────────────────────────────────────────────
    // Restaurant endpoints
    // ──────────────────────────────────────────────

    /**
     * Creates a new restaurant.
     *
     * @param dto data for the new restaurant
     * @return created restaurant details with 201 Created status
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantResponseDto createRestaurant(
            @Valid @RequestBody RestaurantCreateDto dto) {
        return restaurantService.createRestaurant(dto);
    }

    /**
     * Retrieves a restaurant by its ID.
     *
     * @param id restaurant identifier
     * @return restaurant details or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> getRestaurantById(@PathVariable Long id) {
        RestaurantResponseDto dto = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Retrieves a paginated list of all restaurants.
     *
     * <p>Supports sorting and pagination via query parameters:
     * <ul>
     *     <li>{@code page} — page number (0-based)</li>
     *     <li>{@code size} — page size (default 20)</li>
     *     <li>{@code sort} — e.g. {@code name,asc} or {@code averageRating,desc}</li>
     * </ul>
     * </p>
     *
     * @param pageable pagination and sorting parameters
     * @return paginated list of restaurants
     */
    @GetMapping
    public ResponseEntity<Page<RestaurantResponseDto>> getAllRestaurants(Pageable pageable) {
        Page<RestaurantResponseDto> page = restaurantService.getAllRestaurants(pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * Updates an existing restaurant (partial update).
     *
     * @param id  restaurant identifier
     * @param dto partial update data
     * @return updated restaurant details
     */
    @PatchMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> updateRestaurant(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantUpdateDto dto) {
        RestaurantResponseDto updated = restaurantService.updateRestaurant(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes (or deactivates) a restaurant.
     *
     * @param id restaurant identifier
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
    }

    // ──────────────────────────────────────────────
    // Menu Category endpoints
    // ──────────────────────────────────────────────

    /**
     * Creates a new menu category for a specific restaurant.
     *
     * @param restaurantId owning restaurant ID
     * @param dto          category data
     * @return created category
     */
    @PostMapping("/{restaurantId}/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public MenuCategoryResponseDto createCategory(
            @PathVariable Long restaurantId,
            @Valid @RequestBody MenuCategoryCreateDto dto) {
        return restaurantService.createCategory(restaurantId, dto);
    }

    /**
     * Retrieves all menu categories for a restaurant.
     *
     * @param restaurantId restaurant identifier
     * @return list of categories
     */
    @GetMapping("/{restaurantId}/categories")
    public ResponseEntity<List<MenuCategoryResponseDto>> getCategoriesByRestaurant(
            @PathVariable Long restaurantId) {
        List<MenuCategoryResponseDto> categories = restaurantService.getCategoriesByRestaurant(restaurantId);
        return ResponseEntity.ok(categories);
    }

    /**
     * Updates a menu category.
     *
     * @param categoryId category identifier
     * @param dto        partial update data
     * @return updated category
     */
    @PatchMapping("/categories/{categoryId}")
    public ResponseEntity<MenuCategoryResponseDto> updateMenuCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody MenuCategoryUpdateDto dto) {
        MenuCategoryResponseDto updated = restaurantService.updateMenuCategory(categoryId, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes a menu category.
     *
     * @param categoryId category identifier
     */
    @DeleteMapping("/categories/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long categoryId) {
        restaurantService.deleteCategory(categoryId);
    }

    // ──────────────────────────────────────────────
    // Menu Item endpoints
    // ──────────────────────────────────────────────

    /**
     * Creates a new menu item in a category of a restaurant.
     *
     * @param restaurantId owning restaurant ID
     * @param categoryId   target category ID
     * @param dto          item data
     * @return created item
     */
    @PostMapping("/{restaurantId}/categories/{categoryId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemResponseDto createMenuItem(
            @PathVariable Long restaurantId,
            @PathVariable Long categoryId,
            @Valid @RequestBody MenuItemCreateDto dto) {
        return restaurantService.createMenuItem(restaurantId, categoryId, dto);
    }

    /**
     * Retrieves paginated menu items for a restaurant.
     *
     * @param restaurantId restaurant identifier
     * @param pageable     pagination parameters
     * @return paginated list of items
     */
    @GetMapping("/{restaurantId}/items")
    public ResponseEntity<Page<MenuItemResponseDto>> getMenuItemsByRestaurant(
            @PathVariable Long restaurantId,
            Pageable pageable) {
        Page<MenuItemResponseDto> page = restaurantService.getMenuItemsByRestaurant(restaurantId, pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * Updates a menu item.
     *
     * @param itemId item identifier
     * @param dto    partial update data
     * @return updated item
     */
    @PatchMapping("/items/{itemId}")
    public ResponseEntity<MenuItemResponseDto> updateMenuItem(
            @PathVariable Long itemId,
            @Valid @RequestBody MenuItemUpdateDto dto) {
        MenuItemResponseDto updated = restaurantService.updateMenuItem(itemId, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes a menu item.
     *
     * @param itemId item identifier
     */
    @DeleteMapping("/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMenuItem(@PathVariable Long itemId) {
        restaurantService.deleteMenuItem(itemId);
    }
}
