package com.example.fooddelivery.food;

import com.example.fooddelivery.food.dto.FoodResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FoodService {
    private final FoodRepository foodRepository;

    @Autowired
    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Transactional
    public FoodResponseDto getFood(Long id) {
        Food food = foodRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("음식이 존재하지 않습니다.")
        );
        return new FoodResponseDto(food);
    }
}
