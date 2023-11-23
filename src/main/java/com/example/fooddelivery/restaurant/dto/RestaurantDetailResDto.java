package com.example.fooddelivery.restaurant.dto;

import com.example.fooddelivery.menu.dto.AdminMenuResDto;
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
    private String address;
    private List<MenuResDto> menuList;

    public RestaurantDetailResDto(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.minPrice = restaurant.getMinPrice();
        this.deliveryFee = restaurant.getDeliveryFee();
        this.address = restaurant.getRestaurantAddress().toString();
        this.menuList = Optional.ofNullable(restaurant.getMenus()).orElseGet(Collections::emptyList).stream().map(
			MenuResDto::new).collect(Collectors.toList());
    }

    public RestaurantDetailResDto(Long id, String name, int minPrice, int deliveryFee,
        String address, List<MenuResDto> menuList) {
        this.id = id;
        this.name = name;
        this.minPrice = minPrice;
        this.deliveryFee = deliveryFee;
        this.address = address;
        this.menuList = menuList;
    }
}
