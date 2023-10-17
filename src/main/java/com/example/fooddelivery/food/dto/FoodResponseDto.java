package com.example.fooddelivery.food.dto;

import com.example.fooddelivery.food.domain.Food;
import lombok.Getter;

@Getter
public class FoodResponseDto {
    private Long id;
    private String name;
    private int price;

    public FoodResponseDto(Food entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.price = entity.getPrice();
    }

    public FoodResponseDto(Long id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
