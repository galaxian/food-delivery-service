package com.example.fooddelivery.food.dto;

import com.example.fooddelivery.food.domain.Food;
import com.example.fooddelivery.restaurant.domain.Restaurant;
import lombok.Getter;

@Getter
public class FoodRequestDto {
    private String name;
    private int price;

    public Food toEntity(Restaurant restaurant) {
        return Food.createFood(name, price, restaurant);
    }
}
