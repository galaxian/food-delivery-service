package com.example.fooddelivery.food.dto;

import com.example.fooddelivery.food.domain.Food;
import lombok.Getter;

@Getter
public class FoodRequestDto {
    private String name;
    private int price;

    public Food toEntity() {
        return Food.createFood(name, price);
    }
}
