package com.delivery.food.order.controller;

import com.delivery.food.order.domain.OrderStatus;
import com.delivery.food.order.dto.OrderCreateDto;
import com.delivery.food.order.dto.OrderResponseDto;
import com.delivery.food.order.dto.OrderStatusUpdateDto;
import com.delivery.food.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Create a new order (customer only).
     */
    @PostMapping
   /* @PreAuthorize("hasRole('CUSTOMER')")*/
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody @Valid OrderCreateDto dto) {
        OrderResponseDto response = orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get order by ID (customer owns it or admin/restaurant can see).
     */
    @GetMapping("/{orderId}")
   /* @PreAuthorize("hasAnyRole('CUSTOMER', 'RESTAURANT_OWNER', 'ADMIN')")*/
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    /**
     * Get paginated orders for current customer.
     */
    @GetMapping("/my-orders")
   /* @PreAuthorize("hasRole('CUSTOMER')")*/
    public ResponseEntity<Page<OrderResponseDto>> getMyOrders(Pageable pageable) {
        // Get current user ID from SecurityContext (implement getCurrentUserId() in service)
        Long customerId = 1L;/*orderService.getCurrentUserId();*/
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId, pageable));
    }

    /**
     * Get paginated orders for a restaurant (restaurant owner or admin).
     */
    @GetMapping("/restaurant/{restaurantId}")
    /*@PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'ADMIN')")*/
    public ResponseEntity<Page<OrderResponseDto>> getOrdersByRestaurant(
            @PathVariable Long restaurantId, Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrdersByRestaurant(restaurantId, pageable));
    }

    /**
     * Get paginated orders by status (admin or analytics use).
     */
    @GetMapping("/status/{status}")
    /*@PreAuthorize("hasRole('ADMIN')")*/
    public ResponseEntity<Page<OrderResponseDto>> getOrdersByStatus(
            @PathVariable OrderStatus status, Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status, pageable));
    }

    /**
     * Update order status (restaurant/delivery/admin).
     */
    @PatchMapping("/{orderId}/status")
   /* @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'DELIVERY', 'ADMIN')")*/
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody @Valid OrderStatusUpdateDto dto) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, dto));
    }

    /**
     * Cancel order (customer only, early stages).
     */
    @PatchMapping("/{orderId}/cancel")
    /*@PreAuthorize("hasRole('CUSTOMER')")*/
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get active (non-final) orders for current customer.
     */
    @GetMapping("/my-active-orders")
    /*@PreAuthorize("hasRole('CUSTOMER')")*/
    public ResponseEntity<Page<OrderResponseDto>> getMyActiveOrders(Pageable pageable) {
        Long customerId = 1L;/* orderService.getCurrentUserId();*/
        return ResponseEntity.ok(orderService.getActiveOrdersByCustomer(customerId, pageable));
    }
}
