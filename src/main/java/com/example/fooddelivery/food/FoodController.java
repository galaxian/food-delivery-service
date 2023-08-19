package com.example.fooddelivery.food;

import com.example.fooddelivery.food.dto.FoodResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
}
