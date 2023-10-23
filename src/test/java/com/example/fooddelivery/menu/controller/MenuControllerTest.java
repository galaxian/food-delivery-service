package com.example.fooddelivery.menu.controller;

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
import org.springframework.test.web.servlet.ResultActions;

import com.example.fooddelivery.auth.dto.LoginResDto;
import com.example.fooddelivery.common.AbstractRestDocsTest;
import com.example.fooddelivery.common.exception.NotFoundException;
import com.example.fooddelivery.common.exception.UnauthorizedException;
import com.example.fooddelivery.menu.dto.CreateMenuReqDto;
import com.example.fooddelivery.menu.dto.FoodQuantityReqDto;
import com.example.fooddelivery.menu.service.MenuService;

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

	private ResultActions createMenu(CreateMenuReqDto reqDto, Long restaurantId) throws Exception {
		return mockMvc.perform(post("/api/v1/restaurants/{restaurantId}/menus", restaurantId)
			.header(AUTHORIZATION, TOKEN_DTO.getAccessToken())
			.contentType(APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(reqDto)));
	}

}
