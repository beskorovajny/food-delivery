package com.delivery.food.restaurant.service.impl;

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
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        Restaurant restaurant = restaurantMapper.toEntity(dto);
        restaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toResponseDto(restaurant);
    }

    @Transactional(readOnly = true)
    @Override
    public RestaurantResponseDto getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + id));
        return restaurantMapper.toResponseDto(restaurant);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<RestaurantResponseDto> getAllRestaurants(Pageable pageable) {
        return restaurantRepository.findAll(pageable)
                .map(restaurantMapper::toResponseDto);
    }

    @Transactional
    @Override
    public RestaurantResponseDto updateRestaurant(Long id, RestaurantUpdateDto dto) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + id));

        restaurantMapper.updateFromDto(dto, restaurant);

        Restaurant updated = restaurantRepository.save(restaurant);
        return restaurantMapper.toResponseDto(updated);
    }

    @Transactional
    @Override
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + id));
        // Soft delete (preferred in production)
        restaurant.setActive(false);
        restaurantRepository.save(restaurant);

        // Alternative: hard delete
        // restaurantRepository.delete(restaurant);
    }

    @Override
    public MenuCategoryResponseDto createCategory(Long restaurantId, MenuCategoryCreateDto dto) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + restaurantId));

        MenuCategory category = restaurantMapper.toEntity(dto);
        category.setRestaurant(restaurant);

        category = menuCategoryRepository.save(category);
        return restaurantMapper.toResponseDto(category);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MenuCategoryResponseDto> getCategoriesByRestaurant(Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new EntityNotFoundException("Restaurant not found with id: " + restaurantId);
        }

        return menuCategoryRepository.findAllByRestaurant(restaurantId)
                .stream()
                .map(restaurantMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public MenuCategoryResponseDto updateMenuCategory(Long menuCategoryId, MenuCategoryUpdateDto dto) {
        MenuCategory category = menuCategoryRepository.findById(menuCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Menu category not found with id: " + menuCategoryId));

        restaurantMapper.updateFromDto(dto, category);

        category = menuCategoryRepository.save(category);
        return restaurantMapper.toResponseDto(category);
    }

    @Transactional
    @Override
    public void deleteCategory(Long categoryId) {
        MenuCategory category = menuCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Menu category not found with id: " + categoryId));

        menuCategoryRepository.delete(category);
    }

    @Transactional
    @Override
    public MenuItemResponseDto createMenuItem(Long restaurantId, Long categoryId, MenuItemCreateDto dto) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + restaurantId));

        MenuCategory category = menuCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Menu category not found with id: " + categoryId));

        if (!category.getRestaurant().getId().equals(restaurantId)) {
            throw new IllegalArgumentException("Category does not belong to this restaurant");
        }

        MenuItem item = restaurantMapper.toEntity(dto);
        item.setRestaurant(restaurant);
        item.setCategory(category);

        item = menuItemRepository.save(item);
        return restaurantMapper.toResponseDto(item);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MenuItemResponseDto> getMenuItemsByRestaurant(Long restaurantId, Pageable pageable) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new EntityNotFoundException("Restaurant not found with id: " + restaurantId);
        }

        return menuItemRepository.findAllByRestaurantAndAvailableTrue(
                        restaurantRepository.getReferenceById(restaurantId), pageable)
                .map(restaurantMapper::toResponseDto);
    }

    @Transactional
    @Override
    public MenuItemResponseDto updateMenuItem(Long itemId, MenuItemUpdateDto dto) {
        MenuItem item = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Menu item not found with id: " + itemId));

        restaurantMapper.updateFromDto(dto, item);

        item = menuItemRepository.save(item);
        return restaurantMapper.toResponseDto(item);
    }

    @Transactional
    @Override
    public void deleteMenuItem(Long itemId) {
        MenuItem item = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Menu item not found with id: " + itemId));

        // Soft delete (preferred)
        item.setAvailable(false);
        menuItemRepository.save(item);

        // Alternative: hard delete
        // menuItemRepository.delete(item);
    }
}
