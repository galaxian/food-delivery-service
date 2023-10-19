package com.example.fooddelivery.menu.dto;

import com.example.fooddelivery.menu.domain.Menu;
import com.example.fooddelivery.menu.domain.MenuStatus;
import lombok.Getter;

@Getter
public class AdminMenuResDto {
    private Long id;
    private String name;
    private int price;
    private boolean isDisplay;
    private MenuStatus status;

    public AdminMenuResDto(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.isDisplay = menu.isDisplay();
        this.status = menu.getMenuStatus();
    }

    public AdminMenuResDto(Long id, String name, int price, boolean isDisplay,
        MenuStatus status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.isDisplay = isDisplay;
        this.status = status;
    }
}
