package com.delivery.food.order.mapper;

import com.delivery.food.order.domain.DeliveryAddress;
import com.delivery.food.order.domain.Order;
import com.delivery.food.order.domain.OrderItem;
import com.delivery.food.order.dto.*;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OrderMapper {
    // Order
    Order toEntity(OrderCreateDto dto);

    @Mapping(target = "id", ignore = true)
    Order patchFromDto(OrderPatchDto dto, @MappingTarget Order entity);

    @Mapping(target = "id", ignore = true)
    Order updateStatusFromDto(OrderStatusUpdateDto dto, @MappingTarget Order entity);

    OrderResponseDto toResponseDto(Order order);

    // Order item
    OrderItem toEntity(OrderItemCreateDto dto);

    OrderItemResponseDto toResponseDto(OrderItem orderItem);

    // Delivery address (MapStruct will handle them automatically even without explicit methods (field names match).)
    DeliveryAddress toEntity(DeliveryAddressDto dto);

    DeliveryAddressDto toDto(DeliveryAddress deliveryAddress);
}
