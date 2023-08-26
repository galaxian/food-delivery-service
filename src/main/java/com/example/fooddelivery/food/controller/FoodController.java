package com.example.fooddelivery.food.controller;

import com.example.fooddelivery.food.dto.FoodRequestDto;
import com.example.fooddelivery.food.service.FoodService;
import com.example.fooddelivery.food.dto.FoodResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/foods")
public class FoodController {
    private final FoodService foodService;

    @Autowired
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodResponseDto> getFood(@PathVariable Long id) {
        return ResponseEntity.ok().body(foodService.getFood(id));
    }

    @GetMapping("")
    public ResponseEntity<List<FoodResponseDto>> getAllFood() {
        return ResponseEntity.ok().body(foodService.getAllFood());
    }

    @PostMapping("")
    public ResponseEntity<?> createFood(@RequestBody FoodRequestDto requestDto) {
        Long id = foodService.createFood(requestDto);
        return ResponseEntity.created(URI.create("/api/v1/foods/" + id)).build();
    }

    @PutMapping("/{id}")
    public void updateFood(@PathVariable Long id, @RequestBody FoodRequestDto requestDto) {
        foodService.updateFood(id, requestDto);
    }

    @DeleteMapping("/api/v1/foods/{id}")
    public void deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
    }
}
