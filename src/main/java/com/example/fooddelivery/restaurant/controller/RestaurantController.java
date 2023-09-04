package com.example.fooddelivery.restaurant.controller;

import com.example.fooddelivery.restaurant.dto.CreateRestaurantReqDto;
import com.example.fooddelivery.restaurant.dto.RestaurantDetailResDto;
import com.example.fooddelivery.restaurant.dto.RestaurantResDto;
import com.example.fooddelivery.restaurant.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("{restaurantId}")
    public ResponseEntity<RestaurantDetailResDto> findRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(restaurantService.findRestaurant(restaurantId));
    }

    @GetMapping("")
    public ResponseEntity<List<RestaurantResDto>> findAllRestaurant() {
        return ResponseEntity.ok(restaurantService.findAllRestaurant());
    }

    @PostMapping("")
    public ResponseEntity<Void> createRestaurant(@RequestBody CreateRestaurantReqDto reqDto) {
        Long id = restaurantService.createRestaurant(reqDto);
        return ResponseEntity.created(URI.create("/api/v1/restaurants" + id)).build();
    }
}
