package com.example.fooddelivery.menu.dto;

import com.example.fooddelivery.menu.domain.Menu;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateMenuReqDto {
    private String name;
    private int price;
    private String describe;
    private List<FoodQuantityReqDto> foodReqList;

    public Menu toEntity() {
        return Menu.createMenu(name, price, describe);
    }
}
