package com.example.fooddelivery.order.dto;

import lombok.Getter;

@Getter
public class MenuQuantityReqDto {
    private Long id;
    private int quantity;

    public MenuQuantityReqDto(Long id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }
}
