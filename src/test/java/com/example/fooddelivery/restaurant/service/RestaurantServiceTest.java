package com.example.fooddelivery.restaurant.service;

import com.example.fooddelivery.common.exception.DuplicateException;
import com.example.fooddelivery.common.exception.NotFoundException;
import com.example.fooddelivery.restaurant.domain.Restaurant;
import com.example.fooddelivery.restaurant.dto.CreateRestaurantReqDto;
import com.example.fooddelivery.restaurant.dto.RestaurantDetailResDto;
import com.example.fooddelivery.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @Test
    void 정상적으로_식당_생성() {
        //given
        CreateRestaurantReqDto createRestaurantReqDto = new CreateRestaurantReqDto("ㅁㅁ식당",
            10000, 3000);

        given(restaurantRepository.save(any())).willReturn(new Restaurant(1L, createRestaurantReqDto.getName(),
            createRestaurantReqDto.getMinPrice(), createRestaurantReqDto.getDeliveryFee(),
            null, null, null));

        //when
        Long result = restaurantService.createRestaurant(createRestaurantReqDto);

        //then
        assertThat(result).isEqualTo(1L);
    }

    @Test
    void 식당_이름_중복_시_에러() {
        //given
        CreateRestaurantReqDto createRestaurantReqDto = new CreateRestaurantReqDto("중복식당",
            10000, 3000);

        given(restaurantRepository.findByName(any())).willReturn(Optional.of(new Restaurant(1L, "중복식당",
            10000, 3000,
            null, null, null)));

        //when, then
        assertThatThrownBy(() -> restaurantService.createRestaurant(createRestaurantReqDto))
            .isInstanceOf(DuplicateException.class);
    }

    @Test
    void 식당_단일_조회_성공() {
        //given
        Long restaurantId = 1L;
        String name = "중복식당";
        int minPrice = 10000;
        int deliveryFee = 3000;

        given(restaurantRepository.findById(any())).willReturn(Optional.of(new Restaurant(restaurantId, name,
            minPrice, deliveryFee,
            null, null, null)));

        //when
        RestaurantDetailResDto result = restaurantService.findRestaurant(restaurantId);

        //then
        assertThat(result.getId()).isEqualTo(restaurantId);
        assertThat(result.getMinPrice()).isEqualTo(minPrice);
        assertThat(result.getDeliveryFee()).isEqualTo(deliveryFee);
        assertThat(result.getMenuList()).isEqualTo(Collections.EMPTY_LIST);
        assertThat(result.getName()).isEqualTo(name);
    }

    @Test
    void 식당_DB없음_단일조회_실패() {
        //given
        Long restaurantId = 1L;

        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> restaurantService.findRestaurant(restaurantId))
            .isInstanceOf(NotFoundException.class);
    }

}
