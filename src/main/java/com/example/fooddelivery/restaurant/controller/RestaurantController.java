package com.example.fooddelivery.restaurant.controller;

import com.example.fooddelivery.restaurant.dto.CreateRestaurantReqDto;
import com.example.fooddelivery.restaurant.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping("")
    public ResponseEntity<Long> createRestaurant(@RequestBody CreateRestaurantReqDto reqDto) {
        Long id = restaurantService.createRestaurant(reqDto);
        return ResponseEntity.created(URI.create("/api/v1/restaurants" + id)).body(id);
    }
}
