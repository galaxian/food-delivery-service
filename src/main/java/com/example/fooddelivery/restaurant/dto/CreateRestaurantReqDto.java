package com.example.fooddelivery.restaurant.dto;

import lombok.Getter;

@Getter
public class CreateRestaurantReqDto {
    private String name;
    private int minPrice;
    private int deliveryFee;
    private String state;
    private String city;
    private String street;
    private String etcAddress;

    public CreateRestaurantReqDto(String name, int minPrice, int deliveryFee, String state, String city,
        String street, String etcAddress) {
        this.name = name;
        this.minPrice = minPrice;
        this.deliveryFee = deliveryFee;
        this.state = state;
        this.city = city;
        this.street = street;
        this.etcAddress = etcAddress;
    }
}
