package com.example.fooddelivery.order.dto;

import com.example.fooddelivery.order.domain.Order;
import com.example.fooddelivery.order.domain.OrderStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetailResDto {
    private Long id;
    private int totalPrice;
    private LocalDateTime orderTime;
    private OrderStatus orderStatus;
    private List<OrderMenuResDto> orderMenuList;

    public OrderDetailResDto(Order order, int totalPrice, List<OrderMenuResDto> orderMenuList) {
        this.id = order.getId();
        this.totalPrice = totalPrice;
        this.orderTime = order.getOrderTime();
        this.orderStatus = order.getOrderStatus();
        this.orderMenuList = orderMenuList;
    }

    public OrderDetailResDto(Long id, int totalPrice, LocalDateTime orderTime,
        OrderStatus orderStatus, List<OrderMenuResDto> orderMenuList) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.orderTime = orderTime;
        this.orderStatus = orderStatus;
        this.orderMenuList = orderMenuList;
    }
}
