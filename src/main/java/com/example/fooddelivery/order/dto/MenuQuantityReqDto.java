package com.example.fooddelivery.order.dto;

import lombok.Getter;

@Getter
public class MenuQuantityReqDto {
    private Long id;
    private int quantity;
    private int price;
}
