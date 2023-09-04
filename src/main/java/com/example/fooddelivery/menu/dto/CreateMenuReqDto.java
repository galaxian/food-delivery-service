package com.example.fooddelivery.menu.dto;

import com.example.fooddelivery.menu.domain.Menu;
import com.example.fooddelivery.restaurant.domain.Restaurant;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateMenuReqDto {
    private String name;
    private int price;
    private String describe;
    private List<FoodQuantityReqDto> foodReqList;

    public Menu toEntity(Restaurant restaurant) {
        return Menu.createMenu(name, price, describe, restaurant);
    }
}
