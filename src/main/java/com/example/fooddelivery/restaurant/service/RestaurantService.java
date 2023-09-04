package com.example.fooddelivery.restaurant.service;

import com.example.fooddelivery.common.exception.NotFoundException;
import com.example.fooddelivery.restaurant.domain.Restaurant;
import com.example.fooddelivery.restaurant.dto.CreateRestaurantReqDto;
import com.example.fooddelivery.restaurant.dto.RestaurantDetailResDto;
import com.example.fooddelivery.restaurant.dto.RestaurantResDto;
import com.example.fooddelivery.restaurant.dto.UpdateRestaurantReqDto;
import com.example.fooddelivery.restaurant.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public RestaurantDetailResDto findRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new NotFoundException("식당을 찾을 수 없습니다.")
        );
        return new RestaurantDetailResDto(restaurant);
    }

    @Transactional
    public List<RestaurantResDto> findAllRestaurant() {
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        return restaurantList.stream().map(RestaurantResDto::new).collect(Collectors.toList());
    }

    @Transactional
    public void updateRestaurant(UpdateRestaurantReqDto reqDto, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new NotFoundException("식당이 존재하지 않습니다.")
        );
        restaurant.update(reqDto.getName(), reqDto.getMinPrice(), reqDto.getDeliveryFee());
    }

    @Transactional
    public void deleteRestaurant(Long restaurantId) {
        restaurantRepository.deleteById(restaurantId);
    }
}
