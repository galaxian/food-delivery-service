package com.example.fooddelivery.restaurant.dto;

import com.example.fooddelivery.menu.dto.MenuResDto;
import com.example.fooddelivery.restaurant.domain.Restaurant;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class RestaurantDetailResDto {
    private Long id;
    private String name;
    private int minPrice;
    private int deliveryFee;
    private List<MenuResDto> menuList;

    public RestaurantDetailResDto(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.minPrice = restaurant.getMinPrice();
        this.deliveryFee = restaurant.getDeliveryFee();
        this.menuList = Optional.ofNullable(restaurant.getMenus()).orElseGet(Collections::emptyList).stream().map(MenuResDto::new).collect(Collectors.toList());
    }
}
