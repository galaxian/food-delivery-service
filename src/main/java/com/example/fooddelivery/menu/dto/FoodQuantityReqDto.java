package com.example.fooddelivery.menu.dto;

import lombok.Getter;

@Getter
public class FoodQuantityReqDto {
    private Long id;
    private int quantity;

    public FoodQuantityReqDto(Long id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }
}
