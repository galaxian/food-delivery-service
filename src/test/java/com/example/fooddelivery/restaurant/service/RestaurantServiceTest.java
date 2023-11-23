package com.example.fooddelivery.restaurant.service;

import com.example.fooddelivery.common.exception.DuplicateException;
import com.example.fooddelivery.common.exception.NotFoundException;
import com.example.fooddelivery.owner.domain.Owner;
import com.example.fooddelivery.owner.repository.OwnerRepository;
import com.example.fooddelivery.restaurant.domain.Address;
import com.example.fooddelivery.restaurant.domain.Restaurant;
import com.example.fooddelivery.restaurant.dto.CreateRestaurantReqDto;
import com.example.fooddelivery.restaurant.dto.RestaurantDetailResDto;
import com.example.fooddelivery.restaurant.dto.RestaurantResDto;
import com.example.fooddelivery.restaurant.dto.UpdateRestaurantReqDto;
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
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    private static final Owner OWNER = new Owner(1L, "주인", "비밀번호", "salt");
    private static final Address ADDRESS = new Address("서울특별시", "서초구", "선릉", "상세주소");

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @Test
    void 정상적으로_식당_생성() {
        //given
        CreateRestaurantReqDto createRestaurantReqDto = new CreateRestaurantReqDto("ㅁㅁ식당",
            10000, 3000, "서울특별시", "서초구", "선릉", "상세주소");

        given(restaurantRepository.save(any())).willReturn(new Restaurant(1L, createRestaurantReqDto.getName(),
            createRestaurantReqDto.getMinPrice(), createRestaurantReqDto.getDeliveryFee(), ADDRESS, OWNER,
            null, null, null));
        given(ownerRepository.findByIdentifier(any()))
            .willReturn(Optional.of(OWNER));

        //when
        Long result = restaurantService.createRestaurant("주인", createRestaurantReqDto);

        //then
        assertThat(result).isEqualTo(1L);
    }

    @Test
    void 식당_이름_중복_시_에러() {
        //given
        CreateRestaurantReqDto createRestaurantReqDto = new CreateRestaurantReqDto("중복식당",
            10000, 3000, "서울특별시", "서초구", "선릉", "상세주소");

        given(restaurantRepository.findByName(any())).willReturn(Optional.of(new Restaurant(1L, "중복식당",
            10000, 3000,
            ADDRESS, OWNER, null, null, null)));
        given(ownerRepository.findByIdentifier(any()))
            .willReturn(Optional.of(OWNER));

        //when, then
        assertThatThrownBy(() -> restaurantService.createRestaurant("주인", createRestaurantReqDto))
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
            ADDRESS, OWNER, null, null, null)));

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

    @Test
    void 식당_전체조회_성공() {
        //given
        Long restaurantId1 = 1L;
        String name1 = "1번식당";
        int minPrice1 = 10000;
        int deliveryFee1 = 3000;

        Long restaurantId2 = 2L;
        String name2 = "2번식당";
        int minPrice2 = 10000;
        int deliveryFee2 = 3000;

        List<Restaurant> restaurantList = new ArrayList<>();
        restaurantList.add(new Restaurant(restaurantId1, name1, minPrice1, deliveryFee1, ADDRESS, OWNER,
            null, null, null));
        restaurantList.add(new Restaurant(restaurantId2, name2, minPrice2, deliveryFee2, ADDRESS, OWNER,
            null, null, null));

        given(restaurantRepository.findAll()).willReturn(restaurantList);

        //when
        List<RestaurantResDto> result = restaurantService.findAllRestaurant();

        //then
        assertThat(result.get(0).getId()).isEqualTo(restaurantId1);
        assertThat(result.get(1).getId()).isEqualTo(restaurantId2);
        assertThatThrownBy(() -> result.get(result.size())).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void 식당이_존재하지않아_정보_수정_실패() {
        //given
        Long restaurantId = 1L;

        String updateName = "수정식당";
        int updateMinPrice = 5000;
        int updateDeliveryFee = 1000;
        UpdateRestaurantReqDto reqDto = new UpdateRestaurantReqDto(updateName, updateMinPrice, updateDeliveryFee);

        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> restaurantService.updateRestaurant("주인", reqDto, restaurantId))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 식당_검색_성공() {
        //given
        String keyword = "치킨";
        String state = "서울특별시";
        String city = "서초구";

        Long restaurantId1 = 1L;
        String name1 = "맛있는 치킨집";
        int minPrice1 = 10000;
        int deliveryFee1 = 3000;

        Long restaurantId2 = 2L;
        String name2 = "치킨명가";
        int minPrice2 = 10000;
        int deliveryFee2 = 3000;

        List<Restaurant> restaurantList = new ArrayList<>();
        restaurantList.add(new Restaurant(restaurantId1, name1, minPrice1, deliveryFee1, ADDRESS, OWNER,
            null, null, null));
        restaurantList.add(new Restaurant(restaurantId2, name2, minPrice2, deliveryFee2, ADDRESS, OWNER,
            null, null, null));

        given(restaurantRepository.findAllByKeywordAndAddress(any(), any(), any()))
            .willReturn(restaurantList);

        //when
        List<RestaurantResDto> result = restaurantService.filterRestaurant(keyword, state, city);

        //then
        assertThat(result.get(0).getId()).isEqualTo(restaurantId1);
        assertThat(result.get(1).getId()).isEqualTo(restaurantId2);
        assertThat(result.get(0).getName()).contains(name1);
        assertThat(result.get(0).getAddress()).contains(state);
        assertThat(result.get(0).getAddress()).contains(city);
        assertThat(result.get(1).getName()).contains(name2);
        assertThat(result.get(1).getAddress()).contains(state);
        assertThat(result.get(1).getAddress()).contains(city);
        assertThatThrownBy(() -> result.get(result.size())).isInstanceOf(IndexOutOfBoundsException.class);
    }
}
