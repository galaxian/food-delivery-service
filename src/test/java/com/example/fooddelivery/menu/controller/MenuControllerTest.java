package com.example.fooddelivery.menu.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.example.fooddelivery.auth.dto.LoginResDto;
import com.example.fooddelivery.common.AbstractRestDocsTest;
import com.example.fooddelivery.common.exception.BadRequestException;
import com.example.fooddelivery.common.exception.NotFoundException;
import com.example.fooddelivery.common.exception.UnauthorizedException;
import com.example.fooddelivery.menu.domain.MenuStatus;
import com.example.fooddelivery.menu.dto.CreateMenuReqDto;
import com.example.fooddelivery.menu.dto.FoodQuantityReqDto;
import com.example.fooddelivery.menu.dto.MenuDetailResDto;
import com.example.fooddelivery.menu.dto.MenuFoodResDto;
import com.example.fooddelivery.menu.dto.MenuResDto;
import com.example.fooddelivery.menu.service.MenuService;
import com.fasterxml.jackson.core.type.TypeReference;

@WebMvcTest(MenuController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class MenuControllerTest extends AbstractRestDocsTest {

	private static final LoginResDto TOKEN_DTO = new LoginResDto("accessToken");
	private static final List<FoodQuantityReqDto> FOOD_REQ_LIST = new ArrayList<>(Arrays.asList(
		new FoodQuantityReqDto(1L, 1), new FoodQuantityReqDto(2L, 1)
	));

	@MockBean
	private MenuService menuService;

	@DisplayName("메뉴 생성 성공")
	@Test
	void successCreateMenu() throws Exception {
		//given
		Long restaurantId = 1L;
		CreateMenuReqDto reqDto = new CreateMenuReqDto("양념치킨", 10000,
			"비법 소스로 만든 양념치킨", FOOD_REQ_LIST);
		given(menuService.createMenu(anyString(), any(CreateMenuReqDto.class), anyLong()))
			.willReturn(1L);

		//when
		ResultActions resultActions = createMenu(reqDto, restaurantId);

		//then
		resultActions
			.andExpect(status().isCreated())
			.andExpect(header().string(LOCATION, "/api/v1/menus/1"))
			.andDo(
				restDocs.document(
					requestFields(
						fieldWithPath("name")
							.type(JsonFieldType.STRING)
							.description("메뉴 이름"),
						fieldWithPath("price")
							.type(JsonFieldType.NUMBER)
							.description("메뉴 가격"),
						fieldWithPath("describe")
							.type(JsonFieldType.STRING)
							.description("메뉴 설명"),
						fieldWithPath("foodReqList")
							.type(JsonFieldType.ARRAY)
							.description("음식 요구 리스트"),
						fieldWithPath("foodReqList[].id")
							.type(JsonFieldType.NUMBER)
							.description("음식 식별자"),
						fieldWithPath("foodReqList[].quantity")
							.type(JsonFieldType.NUMBER)
							.description("음식 갯수")
					),
					responseHeaders(
						headerWithName(LOCATION).description("생성된 메뉴 URL")
					)
				)
			);
	}

	@DisplayName("식당을 찾을 수 없는 경우 메뉴 생성 실패")
	@Test
	void failCreateMenuByNotFoundRestaurant() throws Exception {
		//given
		Long restaurantId = 1L;
		CreateMenuReqDto reqDto = new CreateMenuReqDto("양념치킨", 10000,
			"비법 소스로 만든 양념치킨", FOOD_REQ_LIST);
		given(menuService.createMenu(anyString(), any(CreateMenuReqDto.class), anyLong()))
			.willThrow(new NotFoundException("식당을 찾을 수 없습니다."));

		//when
		ResultActions resultActions = createMenu(reqDto, restaurantId);

		//then
		resultActions
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("식당을 찾을 수 없습니다."));
	}

	@DisplayName("식당 주인이 아닌 경우 메뉴 생성 실패")
	@Test
	void failCreateMenuByUnAuth() throws Exception {
		//given
		Long restaurantId = 1L;
		CreateMenuReqDto reqDto = new CreateMenuReqDto("양념치킨", 10000,
			"비법 소스로 만든 양념치킨", FOOD_REQ_LIST);
		given(menuService.createMenu(anyString(), any(CreateMenuReqDto.class), anyLong()))
			.willThrow(new UnauthorizedException("식당 주인만 사용할 수 있는 기능입니다."));

		//when
		ResultActions resultActions = createMenu(reqDto, restaurantId);

		//then
		resultActions
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.message").value("식당 주인만 사용할 수 있는 기능입니다."));
	}

	@DisplayName("음식이 없는 경우 메뉴 생성 실패")
	@Test
	void failCreateMenuByNotFoundFood() throws Exception {
		//given
		Long restaurantId = 1L;
		CreateMenuReqDto reqDto = new CreateMenuReqDto("양념치킨", 10000,
			"비법 소스로 만든 양념치킨", FOOD_REQ_LIST);
		given(menuService.createMenu(anyString(), any(CreateMenuReqDto.class), anyLong()))
			.willThrow(new NotFoundException("음식을 찾지 못했습니다."));

		//when
		ResultActions resultActions = createMenu(reqDto, restaurantId);

		//then
		resultActions
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("음식을 찾지 못했습니다."));
	}

	@DisplayName("메뉴 가격이 구성 음식 가격의 합보다 큰 경우 메뉴 생성 실패")
	@Test
	void failCreateMenuByPrice() throws Exception {
		//given
		Long restaurantId = 1L;
		CreateMenuReqDto reqDto = new CreateMenuReqDto("양념치킨", 10000,
			"비법 소스로 만든 양념치킨", FOOD_REQ_LIST);
		given(menuService.createMenu(anyString(), any(CreateMenuReqDto.class), anyLong()))
			.willThrow(new BadRequestException("메뉴 가격은 구성된 음식 가격의 합보다 같거나 작아야 합니다."));

		//when
		ResultActions resultActions = createMenu(reqDto, restaurantId);

		//then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("메뉴 가격은 구성된 음식 가격의 합보다 같거나 작아야 합니다."));
	}

	@DisplayName("메뉴 조회 성공")
	@Test
	void successFindMenu() throws Exception {
		//given
		Long menuId = 1L;
		given(menuService.findMenu(anyLong()))
			.willReturn(new MenuDetailResDto(menuId, "양념치킨", 10000, "비법 소스로 만든 양념치킨",
				MenuStatus.SALE, new ArrayList<>(Arrays.asList(
					new MenuFoodResDto(1L, "양념치킨", 9000, 1),
				new MenuFoodResDto(2L, "콜라", 1, 1000)
			)
				)
				)
			);

		//when
		ResultActions resultActions = findMenu(menuId);

		//then
		MvcResult mvcResult = resultActions
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					responseFields(
						fieldWithPath("id")
							.type(JsonFieldType.NUMBER)
							.description("메뉴 식별자"),
						fieldWithPath("name")
							.type(JsonFieldType.STRING)
							.description("메뉴 이름"),
						fieldWithPath("price")
							.type(JsonFieldType.NUMBER)
							.description("메뉴 가격"),
						fieldWithPath("describe")
							.type(JsonFieldType.STRING)
							.description("메뉴 설명"),
						fieldWithPath("status")
							.type(JsonFieldType.VARIES)
							.description("메뉴 상태"),
						fieldWithPath("menuFoodList")
							.type(JsonFieldType.ARRAY)
							.description("메뉴 구성 음식 목록"),
						fieldWithPath("menuFoodList[].id")
							.type(JsonFieldType.NUMBER)
							.description("음식 식별자"),
						fieldWithPath("menuFoodList[].name")
							.type(JsonFieldType.STRING)
							.description("음식 이름"),
						fieldWithPath("menuFoodList[].quantity")
							.type(JsonFieldType.NUMBER)
							.description("음식 갯수"),
						fieldWithPath("menuFoodList[].price")
							.type(JsonFieldType.NUMBER)
							.description("음식 가격")
					)
				)
			)
			.andReturn();

		MenuDetailResDto menuDetailResDto = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(),
			MenuDetailResDto.class
		);

		assertThat(menuDetailResDto.getId()).isEqualTo(menuId);
		assertThat(menuDetailResDto.getName()).isEqualTo("양념치킨");
		assertThat(menuDetailResDto.getPrice()).isEqualTo(10000);
		assertThat(menuDetailResDto.getDescribe()).isEqualTo("비법 소스로 만든 양념치킨");
		assertThat(menuDetailResDto.getStatus()).isEqualTo(MenuStatus.SALE);
		assertThat(menuDetailResDto.getMenuFoodList().get(1).getId()).isEqualTo(2L);
		assertThat(menuDetailResDto.getMenuFoodList().get(1).getName()).isEqualTo("콜라");
		assertThat(menuDetailResDto.getMenuFoodList().get(1).getQuantity()).isEqualTo(1);
		assertThat(menuDetailResDto.getMenuFoodList().get(1).getPrice()).isEqualTo(1000);

	}

	@DisplayName("메뉴가 존재하지 않는 경우 메뉴 조회 실패")
	@Test
	void failFindMenuByNotFoundMenu() throws Exception {
		//given
		Long menuId = 1L;
		given(menuService.findMenu(anyLong()))
			.willThrow(new NotFoundException("메뉴가 존재하지 않습니다."));

		//when
		ResultActions resultActions = findMenu(menuId);

		//then
		resultActions
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("메뉴가 존재하지 않습니다."));

	}

	@DisplayName("메뉴 목록 조회 성공")
	@Test
	void successFindMenus() throws Exception {
		//given
		Long restaurantId = 1L;
		given(menuService.findAllMenu(anyLong()))
			.willReturn(new ArrayList<>(Arrays.asList(
				new MenuResDto(1L, "양념치킨", 10000, MenuStatus.SALE),
				new MenuResDto(2L, "간장치킨", 10000, MenuStatus.SOLD_OUT)
			)));

		//when
		ResultActions resultActions = findMenus(restaurantId);

		//then
		MvcResult mvcResult = resultActions
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					responseFields(
						fieldWithPath("[].id")
							.type(JsonFieldType.NUMBER)
							.description("메뉴 식별자"),
						fieldWithPath("[].name")
							.type(JsonFieldType.STRING)
							.description("메뉴 이름"),
						fieldWithPath("[].price")
							.type(JsonFieldType.NUMBER)
							.description("메뉴 가격"),
						fieldWithPath("[].status")
							.type(JsonFieldType.VARIES)
							.description("메뉴 상태")
					)
				)
			)
			.andReturn();

		List<MenuResDto> menuResDtoList = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(),
			new TypeReference<List<MenuResDto>>() {
			}
		);

		assertThat(menuResDtoList.get(0).getId()).isEqualTo(1L);
		assertThat(menuResDtoList.get(0).getName()).isEqualTo("양념치킨");
		assertThat(menuResDtoList.get(0).getPrice()).isEqualTo(10000);
		assertThat(menuResDtoList.get(0).getStatus()).isEqualTo(MenuStatus.SALE);

	}

	@DisplayName("메뉴 수정 성공")
	@Test
	void successUpdateMenu() throws Exception {
		//given
		Long restaurantId = 1L;
		Long menuId = 1L;
		CreateMenuReqDto reqDto = new CreateMenuReqDto("양념치킨세트", 12000,
			"비법 소스로 만든 양념치킨과 콜라", FOOD_REQ_LIST);
		doNothing()
			.when(menuService)
			.updateMenu(anyString(), anyLong(), anyLong(), any(CreateMenuReqDto.class));

		//when
		ResultActions resultActions = updateMenu(reqDto, restaurantId, menuId);

		//then
		resultActions
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					requestFields(
						fieldWithPath("name")
							.type(JsonFieldType.STRING)
							.description("메뉴 이름"),
						fieldWithPath("price")
							.type(JsonFieldType.NUMBER)
							.description("메뉴 가격"),
						fieldWithPath("describe")
							.type(JsonFieldType.STRING)
							.description("메뉴 설명"),
						fieldWithPath("foodReqList")
							.type(JsonFieldType.ARRAY)
							.description("음식 요구 리스트"),
						fieldWithPath("foodReqList[].id")
							.type(JsonFieldType.NUMBER)
							.description("음식 식별자"),
						fieldWithPath("foodReqList[].quantity")
							.type(JsonFieldType.NUMBER)
							.description("음식 갯수")
					)
				)
			);
	}

	private ResultActions createMenu(CreateMenuReqDto reqDto, Long restaurantId) throws Exception {
		return mockMvc.perform(post("/api/v1/restaurants/{restaurantId}/menus", restaurantId)
			.header(AUTHORIZATION, TOKEN_DTO.getAccessToken())
			.contentType(APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(reqDto)));
	}

	private ResultActions findMenu(Long menuId) throws Exception {
		return mockMvc.perform(get("/api/v1/menus/{menuId}", menuId)
			.contentType(APPLICATION_JSON));
	}

	private ResultActions findMenus(Long restaurantId) throws Exception {
		return mockMvc.perform(get("/api/v1/restaurants/{restaurantId}/menus", restaurantId)
			.contentType(APPLICATION_JSON));
	}

	private ResultActions updateMenu(CreateMenuReqDto reqDto, Long restaurantId, Long menuId) throws Exception {
		return mockMvc.perform(put("/api/v1/restaurants/{restaurantId}/menus/{menuId}", restaurantId, menuId)
			.header(AUTHORIZATION, TOKEN_DTO.getAccessToken())
			.contentType(APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(reqDto)));
	}

}
