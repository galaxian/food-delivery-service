package com.example.fooddelivery.order.dto;

import lombok.Getter;

@Getter
public class MenuQuantityReqDto {
    private Long id;
    private int quantity;
    private int price;

    public MenuQuantityReqDto(Long id, int quantity, int price) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
    }
}
