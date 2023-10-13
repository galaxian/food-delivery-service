package com.example.fooddelivery.food.service;

import com.example.fooddelivery.common.exception.NotFoundException;
import com.example.fooddelivery.common.exception.UnauthorizedException;
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

    @Transactional(readOnly = true)
    public FoodResponseDto findFood(Long id) {
        Food food = findFoodById(id);
        return new FoodResponseDto(food);
    }

    private Food findFoodById(Long id) {
        return foodRepository.findById(id).orElseThrow(
                () -> new NotFoundException("음식이 존재하지 않습니다.")
        );
    }

    @Transactional(readOnly = true)
    public List<FoodResponseDto> findAllFood(Long restaurantId) {
        List<Food> foodList = findAllFoodsByRestaurantId(restaurantId);
        return makeFoodResponseDtoList(foodList);
    }

    private List<Food> findAllFoodsByRestaurantId(Long restaurantId) {
        return foodRepository.findAllByRestaurantId(restaurantId);
    }

    private List<FoodResponseDto> makeFoodResponseDtoList(List<Food> foodList) {
        return foodList.stream()
            .map(FoodResponseDto::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public Long createFood(String identifier, FoodRequestDto requestDto, Long restaurantId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        validateOwner(identifier, restaurant);
        Food food = convertToFoodEntity(requestDto, restaurant);
        Food saveFood = foodRepository.save(food);
        return saveFood.getId();
    }

    private void validateOwner(String identifier, Restaurant restaurant) {
        if (!restaurant.isOwner(identifier)) {
            throw new UnauthorizedException("식당 주인만 사용할 수 있는 기능입니다.");
        }
    }

    private Restaurant findRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId).orElseThrow(
            () -> new NotFoundException("식당을 찾을 수 없습니다.")
        );
    }

    private Food convertToFoodEntity(FoodRequestDto requestDto, Restaurant restaurant) {
        return Food.createFood(requestDto.getName(), requestDto.getPrice(), restaurant);
    }

    @Transactional
    public void updateFood(Long id, FoodRequestDto requestDto) {
        Food food = findFoodById(id);
        updateFood(food, requestDto);
    }

    private void updateFood(Food food, FoodRequestDto requestDto) {
        food.updateFood(requestDto.getName(), requestDto.getPrice());
    }

    @Transactional
    public void deleteFood(Long id) {
        Food food = findFoodById(id);

        foodRepository.delete(food);
    }
}
