package com.example.fooddelivery.order.dto;

import com.example.fooddelivery.order.domain.Order;
import com.example.fooddelivery.order.domain.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
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
}
