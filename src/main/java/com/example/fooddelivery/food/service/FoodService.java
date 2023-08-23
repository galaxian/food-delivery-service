package com.example.fooddelivery.food.service;

import com.example.fooddelivery.food.dto.FoodRequestDto;
import com.example.fooddelivery.food.repository.FoodRepository;
import com.example.fooddelivery.food.domain.Food;
import com.example.fooddelivery.food.dto.FoodResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public List<FoodResponseDto> getAllFood() {
        List<Food> foodList = foodRepository.findAll();

        return foodList.stream().map(FoodResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public Long createFood(FoodRequestDto requestDto) {
        Food food = requestDto.toEntity();
        return foodRepository.save(food).getId();
    }

    @Transactional
    public void updateFood(Long id, FoodRequestDto requestDto) {
        Food food = foodRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("음식이 존재하지 않습니다.")
        );

        food.updateFood(requestDto.getName(), requestDto.getPrice());
    }

    @Transactional
    public void deleteFood(Long id) {
        Food food = foodRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("음식이 존재하지 않습니다.")
        );

        foodRepository.delete(food);
    }
}
