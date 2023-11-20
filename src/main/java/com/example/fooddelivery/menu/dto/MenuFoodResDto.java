package com.example.fooddelivery.menu.dto;

import com.example.fooddelivery.menufood.domain.MenuFood;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuFoodResDto {
    private Long id;
    private String name;
    private int quantity;
    private int price;

    public MenuFoodResDto(MenuFood menuFood) {
        this.id = menuFood.getFood().getId();
        this.name = menuFood.getFood().getName();
        this.quantity = menuFood.getQuantity();
        this.price = menuFood.getFood().getPrice();
    }

    public MenuFoodResDto(Long id, String name, int quantity, int price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
}
