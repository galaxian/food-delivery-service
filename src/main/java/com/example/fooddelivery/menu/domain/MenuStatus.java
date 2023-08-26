package com.example.fooddelivery.menu.domain;

import lombok.Getter;

@Getter
public enum MenuStatus {
    SOLD_OUT("sold-out"),
    SALE("sale");

    private final String status;

    MenuStatus(String status) {
        this.status = status;
    }
}
