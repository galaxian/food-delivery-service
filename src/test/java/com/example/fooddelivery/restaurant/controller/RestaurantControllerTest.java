package com.example.fooddelivery.restaurant.controller;

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
import java.util.Collections;
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
import com.example.fooddelivery.common.exception.DuplicateException;
import com.example.fooddelivery.common.exception.NotFoundException;
import com.example.fooddelivery.menu.domain.MenuStatus;
import com.example.fooddelivery.menu.dto.MenuResDto;
import com.example.fooddelivery.restaurant.dto.CreateRestaurantReqDto;
import com.example.fooddelivery.restaurant.dto.RestaurantDetailResDto;
import com.example.fooddelivery.restaurant.dto.RestaurantResDto;
import com.example.fooddelivery.restaurant.dto.UpdateRestaurantReqDto;
import com.example.fooddelivery.restaurant.service.RestaurantService;
import com.fasterxml.jackson.core.type.TypeReference;

@WebMvcTest(RestaurantController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class RestaurantControllerTest extends AbstractRestDocsTest {

	private static final LoginResDto TOKEN_DTO = new LoginResDto("accessToken");
	private static final List<MenuResDto> MENU_LIST = new ArrayList<>(
		Collections.singletonList(
			new MenuResDto(1L, "양념치킨", 10000, MenuStatus.SALE))
	);

	@MockBean
	private RestaurantService restaurantService;

	@Test
	@DisplayName("식당 등록 성공")
	void successCreateRestaurant() throws Exception {
		//given
		CreateRestaurantReqDto reqDto = new CreateRestaurantReqDto("치킨집", 10000, 3000
		, "서울특별시", "서초구", "선릉", "상세주소");
		given(restaurantService.createRestaurant(anyString(), any(CreateRestaurantReqDto.class)))
			.willReturn(1L);

		//when
		ResultActions resultActions = createRestaurant(reqDto);

		//then
		resultActions
			.andExpect(status().isCreated())
			.andExpect(header().string(LOCATION, "/api/v1/restaurants/1"))
			.andDo(
				restDocs.document(
					requestFields(
						fieldWithPath("name")
							.type(JsonFieldType.STRING)
							.description("식당 이름"),
						fieldWithPath("minPrice")
							.type(JsonFieldType.NUMBER)
							.description("주문 최소 금액"),
						fieldWithPath("deliveryFee")
							.type(JsonFieldType.NUMBER)
							.description("배달비"),
						fieldWithPath("state")
							.type(JsonFieldType.STRING)
							.description("도/특별시/광역시"),
						fieldWithPath("city")
							.type(JsonFieldType.STRING)
							.description("시/군/구"),
						fieldWithPath("street")
							.type(JsonFieldType.STRING)
							.description("로/길/대로"),
						fieldWithPath("etcAddress")
							.type(JsonFieldType.STRING)
							.description("기타주소")
					),
					responseHeaders(
						headerWithName(LOCATION).description("생성된 식당 URL")
					)
				)
			);
	}

	@Test
	@DisplayName("식당 주인이 존재하지 않을 경우 식당 등록 실패")
	void failCreateRestaurantByNotFoundOwner() throws Exception {
		//given
		CreateRestaurantReqDto reqDto = new CreateRestaurantReqDto("치킨집", 10000, 3000
		, "서울특별시", "서초구", "선릉", "상세주소");
		given(restaurantService.createRestaurant(anyString(), any(CreateRestaurantReqDto.class)))
			.willThrow(new NotFoundException("사용자를 찾을 수 없습니다."));

		//when
		ResultActions resultActions = createRestaurant(reqDto);

		//then
		resultActions
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("사용자를 찾을 수 없습니다."));
	}

	@Test
	@DisplayName("식당 이름이 중복인 경우 식당 등록 실패")
	void failCreateRestaurantByDuplicateName() throws Exception {
		//given
		CreateRestaurantReqDto reqDto = new CreateRestaurantReqDto("식당명 중복", 10000, 3000,
			"서울특별시", "서초구", "선릉", "상세주소");
		given(restaurantService.createRestaurant(anyString(), any(CreateRestaurantReqDto.class)))
			.willThrow(new DuplicateException("동일한 식당 이름이 존재합니다."));

		//when
		ResultActions resultActions = createRestaurant(reqDto);

		//then
		resultActions
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("$.message").value("동일한 식당 이름이 존재합니다."));
	}

	@Test
	@DisplayName("식당 조회 성공")
	void successFindRestaurant() throws Exception {
		//given
		Long restaurantId = 1L;
		given(restaurantService.findRestaurant(anyLong()))
			.willReturn(new RestaurantDetailResDto(restaurantId, "치킨집", 10000, 3000,
				"서울특별시 강남구 ㅁㅁ3로 12-13", MENU_LIST));

		//when
		ResultActions resultActions = findRestaurant(restaurantId);

		//then
		MvcResult mvcResult = resultActions
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					responseFields(
						fieldWithPath("id")
							.type(JsonFieldType.NUMBER)
							.description("식별자"),
						fieldWithPath("name")
							.type(JsonFieldType.STRING)
							.description("식당 이름"),
						fieldWithPath("minPrice")
							.type(JsonFieldType.NUMBER)
							.description("최소 주문 가격"),
						fieldWithPath("deliveryFee")
							.type(JsonFieldType.NUMBER)
							.description("배달비"),
						fieldWithPath("address")
							.type(JsonFieldType.STRING)
							.description("주소"),
						fieldWithPath("menuList")
							.type(JsonFieldType.ARRAY)
							.description("메뉴 목록"),
						fieldWithPath("menuList[0].id")
							.type(JsonFieldType.NUMBER)
							.description("메뉴 식별자"),
						fieldWithPath("menuList[0].name")
							.type(JsonFieldType.STRING)
							.description("메뉴 이름"),
						fieldWithPath("menuList[0].price")
							.type(JsonFieldType.NUMBER)
							.description("메뉴 가격"),
						fieldWithPath("menuList[0].status")
							.type(JsonFieldType.STRING)
							.description("메뉴 상태")
					)
				)
			)
			.andReturn();

		RestaurantDetailResDto restaurantResDto = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(),
			RestaurantDetailResDto.class
		);

		assertThat(restaurantResDto.getId()).isEqualTo(restaurantId);
		assertThat(restaurantResDto.getName()).isEqualTo("치킨집");
		assertThat(restaurantResDto.getMinPrice()).isEqualTo(10000);
		assertThat(restaurantResDto.getDeliveryFee()).isEqualTo(3000);
		assertThat(restaurantResDto.getMenuList().get(0).getId()).isEqualTo(1L);
		assertThat(restaurantResDto.getMenuList().get(0).getName()).isEqualTo("양념치킨");
		assertThat(restaurantResDto.getMenuList().get(0).getPrice()).isEqualTo(10000);
		assertThat(restaurantResDto.getMenuList().get(0).getStatus()).isEqualTo(MenuStatus.SALE);

	}

	@Test
	@DisplayName("식당이 존재하지 않는 경우 식당 조회 실패")
	void failFindRestaurantByNotFoundRestaurant() throws Exception {
		//given
		Long restaurantId = 1L;
		given(restaurantService.findRestaurant(anyLong()))
			.willThrow(new NotFoundException("식당을 찾을 수 없습니다."));

		//when
		ResultActions resultActions = findRestaurant(restaurantId);

		//then
		resultActions
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("식당을 찾을 수 없습니다."));

	}

	@Test
	@DisplayName("식당 목록 조회 성공")
	void successFindRestaurants() throws Exception {
		//given
		given(restaurantService.findAllRestaurant())
			.willReturn(new ArrayList<>(Arrays.asList(
				new RestaurantResDto(1L, "치킨집", 10000, 3000, "서울특별시 강남구 선릉로 112-3"),
				new RestaurantResDto(2L, "분식집", 5000, 2000, "서울특별시 강남구 선릉로 115-3")
			)));

		//when
		ResultActions resultActions = findRestaurants();

		//then
		MvcResult mvcResult = resultActions
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					responseFields(
						fieldWithPath("[].id")
							.type(JsonFieldType.NUMBER)
							.description("식별자"),
						fieldWithPath("[].name")
							.type(JsonFieldType.STRING)
							.description("식당 이름"),
						fieldWithPath("[].minPrice")
							.type(JsonFieldType.NUMBER)
							.description("최소 주문 가격"),
						fieldWithPath("[].deliveryFee")
							.type(JsonFieldType.NUMBER)
							.description("배달비"),
						fieldWithPath("[].address")
							.type(JsonFieldType.STRING)
							.description("주소")
					)
				)
			)
			.andReturn();

		List<RestaurantResDto> restaurantResDto = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(),
			new TypeReference<List<RestaurantResDto>>() {
			}
		);

		assertThat(restaurantResDto.get(0).getId()).isEqualTo(1L);
		assertThat(restaurantResDto.get(0).getName()).isEqualTo("치킨집");
		assertThat(restaurantResDto.get(0).getMinPrice()).isEqualTo(10000);
		assertThat(restaurantResDto.get(0).getDeliveryFee()).isEqualTo(3000);

		assertThat(restaurantResDto.get(1).getId()).isEqualTo(2L);
		assertThat(restaurantResDto.get(1).getName()).isEqualTo("분식집");
		assertThat(restaurantResDto.get(1).getMinPrice()).isEqualTo(5000);
		assertThat(restaurantResDto.get(1).getDeliveryFee()).isEqualTo(2000);

	}

	@Test
	@DisplayName("식당 정보 수정 성공")
	void successUpdateRestaurant() throws Exception {
		//given
		Long restaurantId = 1L;
		UpdateRestaurantReqDto reqDto = new UpdateRestaurantReqDto("치킨집", 9000, 3300);
		doNothing().when(restaurantService).updateRestaurant(anyString(), any(UpdateRestaurantReqDto.class), anyLong());

		//when
		ResultActions resultActions = updateRestaurant(reqDto, restaurantId);

		//then
		resultActions
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					requestFields(
						fieldWithPath("name")
							.type(JsonFieldType.STRING)
							.description("식당 이름"),
						fieldWithPath("minPrice")
							.type(JsonFieldType.NUMBER)
							.description("주문 최소 금액"),
						fieldWithPath("deliveryFee")
							.type(JsonFieldType.NUMBER)
							.description("배달비")
					)
				)
			);
	}

	private ResultActions createRestaurant(CreateRestaurantReqDto reqDto) throws Exception {
		return mockMvc.perform(post("/api/v1/restaurants")
			.header(AUTHORIZATION, TOKEN_DTO.getAccessToken())
			.contentType(APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(reqDto)));
	}

	private ResultActions findRestaurant(Long restaurantId) throws Exception {
		return mockMvc.perform(get("/api/v1/restaurants/{restaurantId}", restaurantId)
			.contentType(APPLICATION_JSON));
	}


	private ResultActions findRestaurants() throws Exception {
		return mockMvc.perform(get("/api/v1/restaurants")
			.contentType(APPLICATION_JSON));
	}

	private ResultActions updateRestaurant(UpdateRestaurantReqDto reqDto, Long restaurantId) throws Exception {
		return mockMvc.perform(put("/api/v1/restaurants/{restaurantId}", restaurantId)
			.header(AUTHORIZATION, TOKEN_DTO.getAccessToken())
			.contentType(APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(reqDto)));
	}
}
