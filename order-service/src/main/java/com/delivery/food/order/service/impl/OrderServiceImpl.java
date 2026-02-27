package com.delivery.food.order.service.impl;

import com.delivery.food.common.exception.EntityNotFoundException;
import com.delivery.food.common.exception.InvalidOperationException;
import com.delivery.food.order.domain.Order;
import com.delivery.food.order.domain.OrderStatus;
import com.delivery.food.order.dto.OrderCreateDto;
import com.delivery.food.order.dto.OrderResponseDto;
import com.delivery.food.order.dto.OrderStatusUpdateDto;
import com.delivery.food.order.mapper.OrderMapper;
import com.delivery.food.order.repository.OrderRepository;
import com.delivery.food.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;


    @Transactional
    @Override
    public OrderResponseDto createOrder(OrderCreateDto dto) {
        log.debug("Creating order for customer Id: {}", dto.getCustomerId());

        Order order = orderMapper.toEntity(dto);
        order.setStatus(OrderStatus.NEW);

        /*order.getItems().forEach(item -> {
            if (item.getSubtotal() == null) {
                item.calculateSubtotal(); // force calculation
            }
        });*/

        // Mock prices/names (temporary!)
        Order finalOrder = order;
        order.getItems().forEach(item -> {
            item.setMenuItemName("Mock Item " + item.getMenuItemId());
            item.setPriceAtOrderTime(BigDecimal.valueOf(100.00)); // mock
            item.setSubtotal(item.getPriceAtOrderTime().multiply(BigDecimal.valueOf(item.getQuantity())));
            item.setOrder(finalOrder);
        });

        order.calculateTotal();
        order.setCreatedAt(LocalDateTime.now());

        order = orderRepository.save(order);

        log.info("Order created: id={}, customer id={}", order.getId(), order.getCustomerId());
        return orderMapper.toResponseDto(order);
    }

    @Transactional(readOnly = true)
    @Override
    public OrderResponseDto getOrderById(Long orderId) {
        log.debug("Fetching order with id={}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        return orderMapper.toResponseDto(order);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OrderResponseDto> getOrdersByCustomer(Long customerId, Pageable pageable) {
        log.debug("Fetching orders for customer with id={}", customerId);

        return orderRepository.findAllByCustomerId(customerId, pageable)
                .map(orderMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OrderResponseDto> getOrdersByRestaurant(Long restaurantId, Pageable pageable) {
        log.debug("Fetching orders by restaurant with id={}", restaurantId);

        return orderRepository.findAllByRestaurantId(restaurantId, pageable)
                .map(orderMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OrderResponseDto> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        log.debug("Fetching all orders with status={}", status);

        return orderRepository.findAllByStatus(status, pageable)
                .map(orderMapper::toResponseDto);
    }

    @Transactional
    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatusUpdateDto statusUpdateDto) {
        log.debug("Updating status for order id: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        // Validate transition (add more rules later)
        if (!isValidTransition(order.getStatus(), statusUpdateDto.getNewStatus())) {
            throw new InvalidOperationException("Invalid status transition from "
                    + order.getStatus()
                    + " to " + statusUpdateDto.getNewStatus(),
                    HttpStatus.BAD_REQUEST);
        }

        order.setStatus(statusUpdateDto.getNewStatus());
        order = orderRepository.save(order);

        log.info("Status for order id={} updated (new status = {})", order.getId(), statusUpdateDto.getNewStatus());
        return orderMapper.toResponseDto(order);
    }

    @Transactional
    @Override
    public void cancelOrder(Long orderId) {
        log.debug("Cancelling order with id: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        // Business validation: only allow cancel in early stages
        if (!canBeCancelled(order.getStatus())) {
            throw new InvalidOperationException(
                    "Cannot cancel order in status: " + order.getStatus(),
                    HttpStatus.BAD_REQUEST);
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        log.info("Order cancelled: id={}", orderId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OrderResponseDto> getActiveOrdersByCustomer(Long customerId, Pageable pageable) {
        log.debug("Fetching active orders for customer with id={}", customerId);

        return orderRepository.findAllByCustomerId(customerId, pageable)
                .map(orderMapper::toResponseDto);
    }

    private boolean isValidTransition(OrderStatus current, OrderStatus next) {
        return switch (current) {
            case NEW -> next == OrderStatus.CONFIRMED || next == OrderStatus.CANCELLED;
            case CONFIRMED -> next == OrderStatus.PREPARING || next == OrderStatus.CANCELLED;
            case PREPARING -> next == OrderStatus.READY_FOR_PICKUP;
            case READY_FOR_PICKUP -> next == OrderStatus.IN_DELIVERY;
            case IN_DELIVERY -> next == OrderStatus.DELIVERED;
            case DELIVERED, CANCELLED, REJECTED -> false; // terminal states
        };
    }

    private boolean canBeCancelled(OrderStatus status) {
        return switch (status) {
            case NEW, CONFIRMED -> true;
            default -> false; // cannot cancel PREPARING or later
        };
    }

   /* private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedAccessException("User not authenticated");
        }
        // Assuming UserDetails has id as principal or custom claim
        return Long.valueOf(auth.getName()); // or auth.getPrincipal() as custom UserDetails
    }*/
}
