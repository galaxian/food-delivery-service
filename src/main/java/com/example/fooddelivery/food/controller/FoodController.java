package com.example.fooddelivery.food.controller;

import com.example.fooddelivery.food.dto.FoodRequestDto;
import com.example.fooddelivery.food.service.FoodService;
import com.example.fooddelivery.food.dto.FoodResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class FoodController {
    private final FoodService foodService;

    @Autowired
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping("/api/v1/foods/{id}")
    public ResponseEntity<FoodResponseDto> getFood(@PathVariable Long id) {
        return ResponseEntity.ok().body(foodService.getFood(id));
    }

    @PostMapping("/api/v1/foods")
    public ResponseEntity<?> createFood(@RequestBody FoodRequestDto requestDto) {
        Long id = foodService.createFood(requestDto);
        return ResponseEntity.created(URI.create("/api/v1/foods/" + id)).build();
    }
}
