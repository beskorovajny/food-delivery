package com.delivery.food.restaurant.mapper;

import com.delivery.food.restaurant.domain.MenuCategory;
import com.delivery.food.restaurant.domain.MenuItem;
import com.delivery.food.restaurant.domain.Restaurant;
import com.delivery.food.restaurant.dto.RestaurantCreateDto;
import com.delivery.food.restaurant.dto.RestaurantResponseDto;
import com.delivery.food.restaurant.dto.RestaurantSummaryDto;
import com.delivery.food.restaurant.dto.RestaurantUpdateDto;
import com.delivery.food.restaurant.dto.menu.category.MenuCategoryCreateDto;
import com.delivery.food.restaurant.dto.menu.category.MenuCategoryResponseDto;
import com.delivery.food.restaurant.dto.menu.category.MenuCategoryUpdateDto;
import com.delivery.food.restaurant.dto.menu.item.MenuItemCreateDto;
import com.delivery.food.restaurant.dto.menu.item.MenuItemResponseDto;
import com.delivery.food.restaurant.dto.menu.item.MenuItemUpdateDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface RestaurantMapper {

    // Restaurant
    Restaurant toEntity(RestaurantCreateDto dto);

    @Mapping(target = "id", ignore = true)
    Restaurant updateFromDto(RestaurantUpdateDto dto, @MappingTarget Restaurant entity);

    RestaurantResponseDto toResponseDto(Restaurant restaurant);

    RestaurantSummaryDto toSummaryDto(Restaurant restaurant);

    // MenuCategory
    MenuCategory toEntity(MenuCategoryCreateDto dto);

    MenuCategory updateFromDto(MenuCategoryUpdateDto dto, @MappingTarget MenuCategory entity);

    MenuCategoryResponseDto toResponseDto(MenuCategory category);

    // MenuItem
    MenuItem toEntity(MenuItemCreateDto dto);

    MenuItem updateFromDto(MenuItemUpdateDto dto, @MappingTarget MenuItem entity);

    MenuItemResponseDto toResponseDto(MenuItem item);

    // Bulk / list mappings (optional but useful)
    List<MenuCategoryResponseDto> toCategoryResponseList(List<MenuCategory> categories);

    List<MenuItemResponseDto> toMenuItemResponseList(List<MenuItem> items);
}
