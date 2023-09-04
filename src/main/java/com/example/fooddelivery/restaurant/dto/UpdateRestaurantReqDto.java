package com.example.fooddelivery.restaurant.dto;

import lombok.Getter;

@Getter
public class UpdateRestaurantReqDto {
    private String name;
    private int minPrice;
    private int deliveryFee;
}
