package com.example.fooddelivery.food.controller;

import com.example.fooddelivery.common.interceptor.Authenticated;
import com.example.fooddelivery.common.resolver.OwnerIdentifier;
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

    @Authenticated
    @GetMapping("/restaurants/{restaurantId}/foods/{foodId}")
    public ResponseEntity<FoodResponseDto> getFood(@OwnerIdentifier String identifier,
        @PathVariable Long restaurantId, @PathVariable Long foodId) {
        return ResponseEntity.ok().body(foodService.findFood(identifier, restaurantId, foodId));
    }

    @Authenticated
    @GetMapping("/restaurants/{restaurantId}/foods")
    public ResponseEntity<List<FoodResponseDto>> getAllFood(@OwnerIdentifier String identifier,
        @PathVariable Long restaurantId) {
        return ResponseEntity.ok().body(foodService.findAllFood(identifier, restaurantId));
    }

    @Authenticated
    @PostMapping("/restaurants/{restaurantId}/foods")
    public ResponseEntity<Void> createFood(@OwnerIdentifier String identifier,
        @RequestBody @Valid FoodRequestDto requestDto,
        @PathVariable Long restaurantId)
    {
        Long id = foodService.createFood(identifier, requestDto, restaurantId);
        return ResponseEntity.created(URI.create("/api/v1/foods/" + id)).build();
    }

    @PutMapping("/foods/{id}")
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
