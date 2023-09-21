package com.example.fooddelivery.restaurant.dto;

import lombok.Getter;

@Getter
public class CreateRestaurantReqDto {
    private String name;
    private int minPrice;
    private int deliveryFee;

    public CreateRestaurantReqDto(String name, int minPrice, int deliveryFee) {
        this.name = name;
        this.minPrice = minPrice;
        this.deliveryFee = deliveryFee;
    }
}
