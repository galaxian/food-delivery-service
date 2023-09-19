package com.example.fooddelivery.food.controller;

import com.example.fooddelivery.food.dto.FoodRequestDto;
import com.example.fooddelivery.food.service.FoodService;
import com.example.fooddelivery.food.dto.FoodResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class FoodController {
    private final FoodService foodService;

    @Autowired
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping("/foods/{id}")
    public ResponseEntity<FoodResponseDto> getFood(@PathVariable Long id) {
        return ResponseEntity.ok().body(foodService.getFood(id));
    }

    @GetMapping("/restaurants/{restaurantId}/foods")
    public ResponseEntity<List<FoodResponseDto>> getAllFood(@PathVariable Long restaurantId) {
        return ResponseEntity.ok().body(foodService.getAllFood(restaurantId));
    }

    @PostMapping("/restaurants/{restaurantId}/foods")
    public ResponseEntity<Void> createFood(@RequestBody @Valid FoodRequestDto requestDto, @PathVariable Long restaurantId) {
        Long id = foodService.createFood(requestDto, restaurantId);
        return ResponseEntity.created(URI.create("/api/v1/foods/" + id)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateFood(@PathVariable Long id, @RequestBody @Valid FoodRequestDto requestDto) {
        foodService.updateFood(id, requestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("foods/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
        return ResponseEntity.ok().build();
    }
}
