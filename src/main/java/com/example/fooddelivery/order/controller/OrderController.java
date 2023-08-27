package com.example.fooddelivery.order.controller;

import com.example.fooddelivery.order.dto.CreateOrderReqDto;
import com.example.fooddelivery.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderReqDto reqDto) {
        Long id = orderService.createOrder(reqDto);
        return ResponseEntity.created(URI.create("/api/v1/orders/" + id)).build();
    }
}
