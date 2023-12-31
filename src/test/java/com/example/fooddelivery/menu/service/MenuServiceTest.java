package com.example.fooddelivery.menu.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.fooddelivery.common.exception.BadRequestException;
import com.example.fooddelivery.common.exception.NotFoundException;
import com.example.fooddelivery.food.domain.Food;
import com.example.fooddelivery.food.repository.FoodRepository;
import com.example.fooddelivery.menu.domain.Menu;
import com.example.fooddelivery.menu.domain.MenuStatus;
import com.example.fooddelivery.menu.dto.CreateMenuReqDto;
import com.example.fooddelivery.menu.dto.FoodQuantityReqDto;
import com.example.fooddelivery.menu.dto.AdminMenuDetailResDto;
import com.example.fooddelivery.menu.dto.AdminMenuResDto;
import com.example.fooddelivery.menu.dto.MenuResDto;
import com.example.fooddelivery.menu.repository.MenuRepository;
import com.example.fooddelivery.menufood.domain.MenuFood;
import com.example.fooddelivery.menufood.repository.MenuFoodRepository;
import com.example.fooddelivery.owner.domain.Owner;
import com.example.fooddelivery.restaurant.domain.Restaurant;
import com.example.fooddelivery.restaurant.repository.RestaurantRepository;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	private static final Owner OWNER = new Owner(1L, "주인", "비밀번호", "salt");
	private static final Restaurant RESTAURANT = new Restaurant(1L, "치킨집", 10000,
		3000, OWNER, null, null, null);

	@Mock
	private MenuRepository menuRepository;

	@Mock
	private FoodRepository foodRepository;

	@Mock
	private MenuFoodRepository menuFoodRepository;

	@Mock
	private RestaurantRepository restaurantRepository;

	@InjectMocks
	private MenuService menuService;

	@Test
	void 메뉴_생성_성공() {
		//given
		FoodQuantityReqDto foodReq1 = new FoodQuantityReqDto(1L, 1);
		FoodQuantityReqDto foodReq2 = new FoodQuantityReqDto(2L, 1);
		List<FoodQuantityReqDto> foodReqList = new ArrayList<>();
		foodReqList.add(foodReq1);
		foodReqList.add(foodReq2);
		CreateMenuReqDto menuReq = new CreateMenuReqDto("양념치킨", 20000, "특급소스로 만든 치킨", foodReqList);

		Long restaurantId = 1L;

		given(restaurantRepository.findById(any()))
			.willReturn(Optional.of(RESTAURANT));
		given(menuRepository.save(any()))
			.willReturn(new Menu(1L,menuReq.getName(), menuReq.getPrice(), menuReq.getDescribe()
				,true, MenuStatus.SALE, RESTAURANT));
		given(foodRepository.findById(any()))
			.willReturn(Optional.of(new Food(1L, "치킨", 20000, RESTAURANT)))
			.willReturn(Optional.of(new Food(2L, "콜라", 3300, RESTAURANT)));
		given(restaurantRepository.findById(any()))
			.willReturn(Optional.of(RESTAURANT));

		//when
		//then
		assertDoesNotThrow(() -> menuService.createMenu("주인", menuReq, restaurantId));

	}

	@Test
	void 식당_NotFound_메뉴_생성_실패() {
		//given
		FoodQuantityReqDto foodReq1 = new FoodQuantityReqDto(1L, 1);
		FoodQuantityReqDto foodReq2 = new FoodQuantityReqDto(2L, 1);
		List<FoodQuantityReqDto> foodReqList = new ArrayList<>();
		foodReqList.add(foodReq1);
		foodReqList.add(foodReq2);
		CreateMenuReqDto menuReq = new CreateMenuReqDto("양념치킨", 20000, "특급소스로 만든 치킨", foodReqList);

		Long restaurantId = 1L;

		given(restaurantRepository.findById(any()))
			.willReturn(Optional.empty());
		given(restaurantRepository.findById(any()))
			.willReturn(Optional.of(RESTAURANT));

		//when
		//then
		assertThatThrownBy(() -> menuService.createMenu("주인", menuReq, restaurantId))
			.isInstanceOf(NotFoundException.class);

	}

	@Test
	void 음식_NotFound_메뉴_생성_실패() {
		//given
		FoodQuantityReqDto foodReq1 = new FoodQuantityReqDto(1L, 1);
		FoodQuantityReqDto foodReq2 = new FoodQuantityReqDto(2L, 1);
		List<FoodQuantityReqDto> foodReqList = new ArrayList<>();
		foodReqList.add(foodReq1);
		foodReqList.add(foodReq2);
		CreateMenuReqDto menuReq = new CreateMenuReqDto("양념치킨", 20000, "특급소스로 만든 치킨", foodReqList);

		Long restaurantId = 1L;

		given(restaurantRepository.findById(any()))
			.willReturn(Optional.of(RESTAURANT));
		given(menuRepository.save(any()))
			.willReturn(new Menu(1L,menuReq.getName(), menuReq.getPrice(), menuReq.getDescribe()
				,true, MenuStatus.SALE, RESTAURANT));
		given(foodRepository.findById(any()))
			.willReturn(Optional.empty());
		given(restaurantRepository.findById(any()))
			.willReturn(Optional.of(RESTAURANT));

		//when
		//then
		assertThatThrownBy(() -> menuService.createMenu("주인", menuReq, restaurantId))
			.isInstanceOf(NotFoundException.class);
	}

	@Test
	void 가격조건미달_메뉴_생성_실패() {
		//given
		FoodQuantityReqDto foodReq1 = new FoodQuantityReqDto(1L, 1);
		FoodQuantityReqDto foodReq2 = new FoodQuantityReqDto(2L, 1);
		List<FoodQuantityReqDto> foodReqList = new ArrayList<>();
		foodReqList.add(foodReq1);
		foodReqList.add(foodReq2);
		CreateMenuReqDto menuReq = new CreateMenuReqDto("양념치킨", 40000, "특급소스로 만든 치킨", foodReqList);

		Long restaurantId = 1L;

		given(restaurantRepository.findById(any()))
			.willReturn(Optional.of(RESTAURANT));
		given(menuRepository.save(any()))
			.willReturn(new Menu(1L,menuReq.getName(), menuReq.getPrice(), menuReq.getDescribe()
				,true, MenuStatus.SALE, RESTAURANT));
		given(foodRepository.findById(any()))
			.willReturn(Optional.of(new Food(1L, "치킨", 20000, RESTAURANT)))
			.willReturn(Optional.of(new Food(2L, "콜라", 3300, RESTAURANT)));
		given(restaurantRepository.findById(any()))
			.willReturn(Optional.of(RESTAURANT));

		//when
		//then
		assertThatThrownBy(() -> menuService.createMenu("주인", menuReq, restaurantId))
			.isInstanceOf(BadRequestException.class);
	}

	@Test
	void 메뉴_단일_조회_성공() {
		//given
		Long menuId = 1L;
		Long restaurantId = 1L;

		Menu menu = new Menu(menuId, "양념치킨", 20000,
			"특급소스로 만든 치킨", true, MenuStatus.SALE, RESTAURANT);
		Food food1 = new Food(1L, "치킨", 20000, RESTAURANT);
		Food food2 = new Food(2L, "콜라", 3000, RESTAURANT);

		List<MenuFood> menuFoodList = new ArrayList<>();
		menuFoodList.add(new MenuFood(1L,1, menu, food1));
		menuFoodList.add(new MenuFood(2L, 2, menu, food2));

		given(menuRepository.findById(any()))
			.willReturn(Optional.of(menu));
		given(menuFoodRepository.findByMenuId(any()))
			.willReturn(menuFoodList);
		given(restaurantRepository.findById(any()))
			.willReturn(Optional.of(RESTAURANT));

		//when
		AdminMenuDetailResDto result = menuService.adminFindMenu("주인", restaurantId, menuId);

		//then
		assertThat(result.getId()).isEqualTo(menuId);
		assertThat(result.getName()).isEqualTo(menu.getName());
		assertThat(result.getDescribe()).isEqualTo(menu.getDescribe());
		assertThat(result.getPrice()).isEqualTo(menu.getPrice());

	}

	@Test
	void 메뉴_NotFound_단일_조회_실패() {
		//given
		Long menuId = 1L;
		Long restaurantId = 1L;

		given(menuRepository.findById(any()))
			.willReturn(Optional.empty());
		given(restaurantRepository.findById(any()))
			.willReturn(Optional.of(RESTAURANT));

		//when
		//then
		assertThatThrownBy(() -> menuService.adminFindMenu("주인", restaurantId, menuId))
			.isInstanceOf(NotFoundException.class);

	}

	@Test
	void 식당_메뉴_전체_조회_성공() {
		//given
		List<Menu> menuList = new ArrayList<>();

		Menu menu1 = new Menu(1L, "양념치킨", 20000,
			"특급소스로 만든 치킨", true, MenuStatus.SALE, RESTAURANT);
		Menu menu2 = new Menu(2L, "간장치킨", 20000,
			"특급소스로 만든 치킨", true, MenuStatus.SALE, RESTAURANT);
		menuList.add(menu1);
		menuList.add(menu2);

		given(menuRepository.findAllByRestaurantId(any()))
			.willReturn(menuList);
		given(restaurantRepository.findById(any()))
			.willReturn(Optional.of(RESTAURANT));

		//when
		List<AdminMenuResDto> result = menuService.adminFindAllMenu("주인", RESTAURANT.getId());

		//then
		assertThat(result.get(0).getId()).isEqualTo(menu1.getId());
		assertThat(result.get(1).getId()).isEqualTo(menu2.getId());
		assertThat(result.size()).isEqualTo(menuList.size());

	}

	@Test
	void 메뉴_목록_조회_isDisplayFalse_제외() {
		//given
		List<Menu> menuList = new ArrayList<>();

		Menu menu1 = new Menu(1L, "양념치킨", 20000,
			"특급소스로 만든 치킨", true, MenuStatus.SALE, RESTAURANT);
		Menu menu2 = new Menu(2L, "간장치킨", 20000,
			"특급소스로 만든 치킨", false, MenuStatus.SALE, RESTAURANT);
		menuList.add(menu1);
		menuList.add(menu2);

		given(menuRepository.findAllByRestaurantId(any()))
			.willReturn(menuList);

		//when
		List<MenuResDto> result = menuService.findAllMenu(RESTAURANT.getId());

		//then
		assertThat(result.size()).isEqualTo(1);

	}

	@Test
	void 메뉴_NotFound_수정_실패() {
		//given
		Long menuId = 1L;
		FoodQuantityReqDto foodReq1 = new FoodQuantityReqDto(1L, 1);
		FoodQuantityReqDto foodReq2 = new FoodQuantityReqDto(2L, 1);
		List<FoodQuantityReqDto> foodReqList = new ArrayList<>();
		foodReqList.add(foodReq1);
		foodReqList.add(foodReq2);
		CreateMenuReqDto updateReq = new CreateMenuReqDto("양념치킨", 20000,
			"특급소스로 만든 치킨", foodReqList);

		Long restaurantId = 1L;

		given(menuRepository.findById(any()))
			.willReturn(Optional.empty());
		given(restaurantRepository.findById(any()))
			.willReturn(Optional.of(RESTAURANT));

		//when
		//then
		assertThatThrownBy(() -> menuService.updateMenu("주인", restaurantId, menuId, updateReq))
			.isInstanceOf(NotFoundException.class);
	}

	@Test
	void 가격조건미달_메뉴_수정_실패() {
		//given
		Long menuId = 1L;
		FoodQuantityReqDto foodReq1 = new FoodQuantityReqDto(1L, 1);
		FoodQuantityReqDto foodReq2 = new FoodQuantityReqDto(2L, 1);
		List<FoodQuantityReqDto> foodReqList = new ArrayList<>();
		foodReqList.add(foodReq1);
		foodReqList.add(foodReq2);
		CreateMenuReqDto updateReq = new CreateMenuReqDto("양념치킨", 23300,
			"특급소스로 만든 치킨", foodReqList);

		Long restaurantId = 1L;

		given(menuRepository.findById(any()))
			.willReturn(Optional.of(
				new Menu(1L, updateReq.getName(), updateReq.getPrice(),
					updateReq.getDescribe(), true, MenuStatus.SALE,
					RESTAURANT)));
		given(foodRepository.findById(any()))
			.willReturn(Optional.of(new Food(1L, "치킨", 20000, RESTAURANT)))
			.willReturn(Optional.of(new Food(2L, "콜라", 3300, RESTAURANT)));
		given(restaurantRepository.findById(any()))
			.willReturn(Optional.of(RESTAURANT));

		//when
		//then
		assertThatThrownBy(() -> menuService.updateMenu("주인", restaurantId, menuId, updateReq))
			.isInstanceOf(BadRequestException.class);

	}

}
