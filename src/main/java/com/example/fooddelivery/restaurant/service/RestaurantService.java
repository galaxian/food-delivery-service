package com.example.fooddelivery.restaurant.service;

import com.example.fooddelivery.common.exception.DuplicateException;
import com.example.fooddelivery.common.exception.NotFoundException;
import com.example.fooddelivery.owner.domain.Owner;
import com.example.fooddelivery.owner.repository.OwnerRepository;
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
    private final OwnerRepository ownerRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository,
        OwnerRepository ownerRepository) {
        this.restaurantRepository = restaurantRepository;
        this.ownerRepository = ownerRepository;
    }

    @Transactional
    public Long createRestaurant(String identifier, CreateRestaurantReqDto reqDto) {
        Owner owner = findOwnerByIdentifier(identifier);
        validateRestaurant(reqDto);
        Restaurant restaurant = convertToRestaurant(reqDto, owner);
        Restaurant saveRestaurant = restaurantRepository.save(restaurant);
        return saveRestaurant.getId();
    }

    private void validateRestaurant(CreateRestaurantReqDto reqDto) {
        if (isDuplicateName(reqDto.getName())) {
            throw new DuplicateException("동일한 식당 이름이 존재합니다.");
        }
    }

    private boolean isDuplicateName(String name) {
        return restaurantRepository.findByName(name).isPresent();
    }

    private Restaurant convertToRestaurant(CreateRestaurantReqDto reqDto, Owner owner) {
        return new Restaurant(reqDto.getName(), reqDto.getMinPrice(), reqDto.getDeliveryFee(), owner);
    }

    private Owner findOwnerByIdentifier(String identifier) {
        return ownerRepository.findByIdentifier(identifier).orElseThrow(
            () -> new NotFoundException("사용자를 찾을 수 없습니다.")
        );
    }

    @Transactional(readOnly = true)
    public RestaurantDetailResDto findRestaurant(Long restaurantId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        return new RestaurantDetailResDto(restaurant);
    }

    private Restaurant findRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId).orElseThrow(
            () -> new NotFoundException("식당을 찾을 수 없습니다.")
        );
    }

    @Transactional
    public List<RestaurantResDto> findAllRestaurant() {
        List<Restaurant> restaurantList = findAllRestaurants();
        return makeRestaurantResDtoList(restaurantList);
    }

    private List<Restaurant> findAllRestaurants() {
        return restaurantRepository.findAll();
    }

    private List<RestaurantResDto> makeRestaurantResDtoList(List<Restaurant> restaurantList) {
        return restaurantList.stream()
            .map(RestaurantResDto::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public void updateRestaurant(UpdateRestaurantReqDto reqDto, Long restaurantId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        updateRestaurant(restaurant, reqDto);
    }

    private void updateRestaurant(Restaurant restaurant, UpdateRestaurantReqDto reqDto) {
        restaurant.update(reqDto.getName(), reqDto.getMinPrice(), reqDto.getDeliveryFee());
    }

    @Transactional
    public void deleteRestaurant(Long restaurantId) {
        restaurantRepository.deleteById(restaurantId);
    }
}
