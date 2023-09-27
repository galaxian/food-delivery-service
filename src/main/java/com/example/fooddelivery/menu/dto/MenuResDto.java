package com.example.fooddelivery.menu.dto;

import com.example.fooddelivery.menu.domain.Menu;
import com.example.fooddelivery.menu.domain.MenuStatus;
import lombok.Getter;

@Getter
public class MenuResDto {
    private Long id;
    private String name;
    private int price;
    private String describe;
    private boolean isDisplay;
    private MenuStatus status;

    public MenuResDto(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.describe = menu.getDescribe();
        this.isDisplay = menu.isDisplay();
        this.status = menu.getMenuStatus();
    }
}
