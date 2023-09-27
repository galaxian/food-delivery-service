package com.example.fooddelivery.order.domain;

import lombok.Getter;

@Getter
public enum OrderStatus {
    WAITING,
    ACCEPT,
    DELIVERING,
    DELIVERED,
    COMPLETE
}
