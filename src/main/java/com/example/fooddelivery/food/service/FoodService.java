package com.example.fooddelivery.food.service;

import com.example.fooddelivery.common.exception.NotFoundException;
import com.example.fooddelivery.food.dto.FoodRequestDto;
import com.example.fooddelivery.food.repository.FoodRepository;
import com.example.fooddelivery.food.domain.Food;
import com.example.fooddelivery.food.dto.FoodResponseDto;
import com.example.fooddelivery.restaurant.domain.Restaurant;
import com.example.fooddelivery.restaurant.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodService {
    private final FoodRepository foodRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public FoodService(FoodRepository foodRepository, RestaurantRepository restaurantRepository) {
        this.foodRepository = foodRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public FoodResponseDto getFood(Long id) {
        Food food = foodRepository.findById(id).orElseThrow(
                () -> new NotFoundException("음식이 존재하지 않습니다.")
        );
        return new FoodResponseDto(food);
    }

    @Transactional
    public List<FoodResponseDto> getAllFood() {
        List<Food> foodList = foodRepository.findAll();

        return foodList.stream().map(FoodResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public Long createFood(FoodRequestDto requestDto, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new NotFoundException("식당을 찾을 수 없습니다.")
        );
        Food food = requestDto.toEntity(restaurant);
        return foodRepository.save(food).getId();
    }

    @Transactional
    public void updateFood(Long id, FoodRequestDto requestDto) {
        Food food = foodRepository.findById(id).orElseThrow(
                () -> new NotFoundException("음식이 존재하지 않습니다.")
        );

        food.updateFood(requestDto.getName(), requestDto.getPrice());
    }

    @Transactional
    public void deleteFood(Long id) {
        Food food = foodRepository.findById(id).orElseThrow(
                () -> new NotFoundException("음식이 존재하지 않습니다.")
        );

        foodRepository.delete(food);
    }
}
