package com.example.fooddelivery.restaurant.service;

import com.example.fooddelivery.common.exception.DuplicateException;
import com.example.fooddelivery.restaurant.domain.Restaurant;
import com.example.fooddelivery.restaurant.dto.CreateRestaurantReqDto;
import com.example.fooddelivery.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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

}
