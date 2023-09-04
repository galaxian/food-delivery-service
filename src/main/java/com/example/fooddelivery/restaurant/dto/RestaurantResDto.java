package com.example.fooddelivery.restaurant.dto;

import com.example.fooddelivery.restaurant.domain.Restaurant;
import lombok.Getter;

@Getter
public class RestaurantResDto {
    private Long id;
    private String name;
    private int minPrice;
    private int deliveryFee;

    public RestaurantResDto(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.minPrice = restaurant.getMinPrice();
        this.deliveryFee = restaurant.getDeliveryFee();
    }
}
