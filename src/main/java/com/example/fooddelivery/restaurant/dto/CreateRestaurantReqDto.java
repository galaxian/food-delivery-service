package com.example.fooddelivery.restaurant.dto;

import com.example.fooddelivery.restaurant.domain.Restaurant;
import lombok.Getter;

@Getter
public class CreateRestaurantReqDto {
    private String name;
    private int minPrice;
    private int deliveryFee;

    public Restaurant toEntity() {
        return Restaurant.createRestaurant(name, minPrice, deliveryFee);
    }

    public CreateRestaurantReqDto(String name, int minPrice, int deliveryFee) {
        this.name = name;
        this.minPrice = minPrice;
        this.deliveryFee = deliveryFee;
    }
}
