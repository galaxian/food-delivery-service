package com.example.fooddelivery.order.dto;

import com.example.fooddelivery.ordermenu.domain.OrderMenu;
import lombok.Getter;

@Getter
public class OrderMenuResDto {
    private Long id;
    private String name;
    private int price;
    private int quantity;

    public OrderMenuResDto(OrderMenu orderMenu) {
        this.id = orderMenu.getMenu().getId();
        this.name = orderMenu.getMenu().getName();
        this.price = orderMenu.getMenu().getPrice();
        this.quantity = orderMenu.getQuantity();
    }
}
