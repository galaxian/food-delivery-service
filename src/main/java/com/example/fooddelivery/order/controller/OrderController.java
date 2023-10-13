package com.example.fooddelivery.order.controller;

import com.example.fooddelivery.common.interceptor.Authenticated;
import com.example.fooddelivery.common.resolver.OwnerIdentifier;
import com.example.fooddelivery.order.dto.CreateOrderReqDto;
import com.example.fooddelivery.order.dto.GetAllOrderByPhoneReqDto;
import com.example.fooddelivery.order.dto.MenuQuantityReqDto;
import com.example.fooddelivery.order.dto.OrderDetailResDto;
import com.example.fooddelivery.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDetailResDto> findOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findOrder(id));
    }

    @Authenticated
    @GetMapping("/restaurants/{restaurantId}/orders")
    public ResponseEntity<List<OrderDetailResDto>> findAllOrder(@OwnerIdentifier String identifier,
        @PathVariable Long restaurantId) {
        return ResponseEntity.ok(orderService.findAllOrder(identifier, restaurantId));
    }

    @GetMapping("/orders/phone")
    public ResponseEntity<List<OrderDetailResDto>> findAllOrderByPhoneNumber(@RequestBody @Valid GetAllOrderByPhoneReqDto reqDto) {
        return ResponseEntity.ok(orderService.findAllOrderByPhoneNumber(reqDto));
    }

    @Authenticated
    @PostMapping("/restaurants/{restaurantsId}/orders")
    public ResponseEntity<Void> createOrder(@OwnerIdentifier String identifier,
        @RequestBody @Valid CreateOrderReqDto reqDto,
        @PathVariable Long restaurantsId) {
        Long id = orderService.createOrder(identifier, reqDto, restaurantsId);
        return ResponseEntity.created(URI.create("/api/v1/orders/" + id)).build();
    }

    @Authenticated
    @PutMapping("/restaurants/{restaurantsId}/orders/{orderId}")
    public ResponseEntity<Void> updateOrder(@OwnerIdentifier String identifier,
        @PathVariable Long restaurantsId,
        @RequestBody @Valid List<MenuQuantityReqDto> reqDto,
        @PathVariable Long orderId) {
        orderService.updateOrder(identifier, restaurantsId, reqDto, orderId);
        return ResponseEntity.ok().build();
    }

    @Authenticated
    @DeleteMapping("/restaurants/{restaurantsId}/orders/{orderId}")
    public ResponseEntity<Void> deleteOrder(@OwnerIdentifier String identifier,
        @PathVariable Long restaurantsId,
        @PathVariable Long orderId) {
        orderService.deleteOrder(identifier, restaurantsId, orderId);
        return ResponseEntity.ok().build();
    }
}
