package com.delivery.food.restaurant.service.impl;

import com.delivery.food.common.exception.DuplicateEntityException;
import com.delivery.food.common.exception.EntityNotFoundException;
import com.delivery.food.common.exception.InvalidOperationException;
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
import com.delivery.food.restaurant.mapper.RestaurantMapper;
import com.delivery.food.restaurant.repository.MenuCategoryRepository;
import com.delivery.food.restaurant.repository.MenuItemRepository;
import com.delivery.food.restaurant.repository.RestaurantRepository;
import com.delivery.food.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final RestaurantMapper restaurantMapper;

    @Transactional
    @Override
    public RestaurantResponseDto createRestaurant(RestaurantCreateDto dto) {
        log.debug("Creating restaurant: {}", dto.getName());

        if (restaurantRepository.findByName(dto.getName()).isPresent()) {
            throw new DuplicateEntityException("Restaurant with name '" + dto.getName() + "' already exists");
        }
        Restaurant restaurant = restaurantMapper.toEntity(dto);
        restaurant = restaurantRepository.save(restaurant);

        log.info("Restaurant created: id={}, name={}", restaurant.getId(), restaurant.getName());
        return restaurantMapper.toResponseDto(restaurant);
    }

    @Transactional(readOnly = true)
    @Override
    public RestaurantResponseDto getRestaurantById(Long id) {
        log.debug("Fetching restaurant by id: {}", id);

        Restaurant restaurant = restaurantRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + id));

        return restaurantMapper.toResponseDto(restaurant);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<RestaurantResponseDto> getAllRestaurants(Pageable pageable) {
        log.debug("Fetching all restaurants: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());

        return restaurantRepository.findAll(pageable)
                .map(restaurantMapper::toResponseDto);
    }

    @Transactional
    @Override
    public RestaurantResponseDto updateRestaurant(Long id, RestaurantUpdateDto dto) {
        log.debug("Updating restaurant id: {}", id);

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + id));

        restaurantMapper.updateFromDto(dto, restaurant);
        Restaurant updated = restaurantRepository.save(restaurant);

        log.info("Restaurant updated: id={}", restaurant.getId());
        return restaurantMapper.toResponseDto(updated);
    }

    @Transactional
    @Override
    public void deleteRestaurant(Long id) {
        log.debug("Deleting restaurant id: {}", id);

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + id));
        // Soft delete (preferred in production)
        restaurant.setActive(false);
        restaurantRepository.save(restaurant);

        log.info("Restaurant deactivated: id={}", id);
        // Alternative: hard delete
        // restaurantRepository.delete(restaurant);
    }

    @Override
    public MenuCategoryResponseDto createCategory(Long restaurantId, MenuCategoryCreateDto dto) {
        log.debug("Creating category for restaurant id: {}, name: {}", restaurantId, dto.getName());

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + restaurantId));

        menuCategoryRepository.findByNameAndRestaurantId(dto.getName(), restaurant.getId())
                .ifPresent(existing -> {
                    throw new DuplicateEntityException("Category with name '"
                            + dto.getName() + "' already exists in this restaurant");
                });

        MenuCategory category = restaurantMapper.toEntity(dto);
        category.setRestaurant(restaurant);
        category = menuCategoryRepository.save(category);

        log.info("Category created: id={}, name={}", category.getId(), category.getName());
        return restaurantMapper.toResponseDto(category);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MenuCategoryResponseDto> getCategoriesByRestaurant(Long restaurantId) {
        log.debug("Fetching categories for restaurant id: {}", restaurantId);

        return menuCategoryRepository.findAllByRestaurantId(restaurantId)
                .stream()
                .map(restaurantMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public MenuCategoryResponseDto updateMenuCategory(Long menuCategoryId, MenuCategoryUpdateDto dto) {
        log.debug("Updating menu category id: {}", menuCategoryId);

        MenuCategory category = menuCategoryRepository.findById(menuCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Menu category not found with id: " + menuCategoryId));

        restaurantMapper.updateFromDto(dto, category);
        category = menuCategoryRepository.save(category);

        log.info("Menu category updated: id={}", category.getId());
        return restaurantMapper.toResponseDto(category);
    }

    @Transactional
    @Override
    public void deleteCategory(Long categoryId) {
        log.debug("Deleting menu category id: {}", categoryId);

        MenuCategory category = menuCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Menu category not found with id: " + categoryId));

        menuCategoryRepository.delete(category);
        log.info("Menu category deleted: id={}", categoryId);
    }

    @Transactional
    @Override
    public MenuItemResponseDto createMenuItem(Long restaurantId, Long categoryId, MenuItemCreateDto dto) {
        log.debug("Creating menu item for restaurant id: {}, category id: {}, name: {}",
                restaurantId, categoryId, dto.getName());

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + restaurantId));

        MenuCategory category = menuCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Menu category not found with id: " + categoryId));

        if (!category.getRestaurant().getId().equals(restaurantId)) {
            throw new InvalidOperationException("Category does not belong to this restaurant", HttpStatus.BAD_REQUEST);
        }

        // Check for duplicate item name in this restaurant
        menuItemRepository.findByNameAndRestaurantId(dto.getName(), restaurant.getId())
                .ifPresent(existing -> {
                    throw new DuplicateEntityException("Menu item with name '"
                            + dto.getName() + "' already exists in this restaurant");
                });

        MenuItem item = restaurantMapper.toEntity(dto);
        item.setRestaurant(restaurant);
        item.setCategory(category);
        item = menuItemRepository.save(item);

        log.info("Menu item created: id={}, name={}", item.getId(), item.getName());
        return restaurantMapper.toResponseDto(item);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MenuItemResponseDto> getMenuItemsByRestaurant(Long restaurantId, Pageable pageable) {
        log.debug("Fetching menu items for restaurant id: {}, page={}, size={}",
                restaurantId, pageable.getPageNumber(), pageable.getPageSize());

        if (!restaurantRepository.existsById(restaurantId)) {
            throw new EntityNotFoundException("Restaurant not found with id: " + restaurantId);
        }

        return menuItemRepository.findAllByRestaurantIdAndAvailableTrue(restaurantId, pageable)
                .map(restaurantMapper::toResponseDto);
    }

    @Transactional
    @Override
    public MenuItemResponseDto updateMenuItem(Long itemId, MenuItemUpdateDto dto) {
        log.debug("Updating menu item id: {}", itemId);

        MenuItem item = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Menu item not found with id: " + itemId));

        restaurantMapper.updateFromDto(dto, item);
        item = menuItemRepository.save(item);

        log.info("Menu item updated: id={}", item.getId());
        return restaurantMapper.toResponseDto(item);
    }

    @Transactional
    @Override
    public void deleteMenuItem(Long itemId) {
        log.debug("Deleting menu item id: {}", itemId);

        MenuItem item = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Menu item not found with id: " + itemId));

        // Soft delete (preferred)
        item.setAvailable(false);
        menuItemRepository.save(item);

        log.info("Menu item deactivated: id={}", itemId);

        // Alternative: hard delete
        // menuItemRepository.delete(item);
    }
}
