package com.example.fooddelivery.restaurant.service;

import com.example.fooddelivery.restaurant.domain.Restaurant;
import com.example.fooddelivery.restaurant.dto.CreateRestaurantReqDto;
import com.example.fooddelivery.restaurant.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public Long createRestaurant(CreateRestaurantReqDto reqDto) {
        Restaurant restaurant = reqDto.toEntity();

        Restaurant saveRestaurant = restaurantRepository.save(restaurant);

        return saveRestaurant.getId();
    }
}
