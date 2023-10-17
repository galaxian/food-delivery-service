package com.example.fooddelivery.food.controller;

import static com.example.fooddelivery.common.RestDocsConfiguration.*;
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
import com.example.fooddelivery.food.dto.FoodRequestDto;
import com.example.fooddelivery.food.dto.FoodResponseDto;
import com.example.fooddelivery.food.service.FoodService;
import com.fasterxml.jackson.core.type.TypeReference;

@WebMvcTest(FoodController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class FoodControllerTest extends AbstractRestDocsTest {

	private static final LoginResDto TOKEN_DTO = new LoginResDto("accessToken");

	@MockBean
	private FoodService foodService;

	@Test
	@DisplayName("음식 등록 성공")
	void successCreateFood() throws Exception {
		//given
		FoodRequestDto foodRequestDto = new FoodRequestDto("치킨", 10000);
		given(foodService.createFood(anyString(), any(FoodRequestDto.class), anyLong()))
			.willReturn(1L);

		//when
		ResultActions resultActions = createFood(foodRequestDto);

		//then
		resultActions
			.andExpect(status().isCreated())
			.andExpect(header().string(LOCATION, "/api/v1/foods/1"))
			.andDo(
				restDocs.document(
					requestFields(
						fieldWithPath("name")
							.type(JsonFieldType.STRING)
							.description("음식 이름")
							.attributes(field("constraint", "길이 50 이하")),
						fieldWithPath("price")
							.type(JsonFieldType.NUMBER)
							.description("음식 가격")
							.attributes(field("constraint", "0원 이상"))
					),
					responseHeaders(
						headerWithName(LOCATION).description("생성된 음식 URL")
					)
				)
			);

	}

	@Test
	@DisplayName("음식 등록 시 존재하지 않는 식당이면 실패")
	void failCreateFoodByNotFoundRestaurant() throws Exception {
		//given
		FoodRequestDto foodRequestDto = new FoodRequestDto("치킨", 10000);
		given(foodService.createFood(anyString(), any(FoodRequestDto.class), anyLong()))
			.willThrow(new NotFoundException("식당을 찾을 수 없습니다."));

		//when
		ResultActions resultActions = createFood(foodRequestDto);

		//then
		resultActions
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("식당을 찾을 수 없습니다."));

	}

	@Test
	@DisplayName("음식 등록 시 식당 주인이 아닌 경우 실패")
	void failCreateFoodByUnAuthOwner() throws Exception {
		//given
		FoodRequestDto foodRequestDto = new FoodRequestDto("치킨", 10000);
		given(foodService.createFood(anyString(), any(FoodRequestDto.class), anyLong()))
			.willThrow(new UnauthorizedException("식당 주인만 사용할 수 있는 기능입니다."));

		//when
		ResultActions resultActions = createFood(foodRequestDto);

		//then
		resultActions
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.message").value("식당 주인만 사용할 수 있는 기능입니다."));

	}

	@Test
	@DisplayName("음식 등록 음식 이름 미입력시 실패")
	void failCreateFoodByNullFoodName() throws Exception {
		//given
		FoodRequestDto foodRequestDto = new FoodRequestDto(null, 10000);
		given(foodService.createFood(anyString(), any(FoodRequestDto.class), anyLong()))
			.willThrow(new BadRequestException("음식 이름을 입력해주세요"));

		//when
		ResultActions resultActions = createFood(foodRequestDto);

		//then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("음식 이름을 입력해주세요"));

	}

	@Test
	@DisplayName("음식 등록 음식 가격 미입력시 실패")
	void failCreateFoodByNullFoodPrice() throws Exception {
		//given
		FoodRequestDto foodRequestDto = new FoodRequestDto("치킨", null);
		given(foodService.createFood(anyString(), any(FoodRequestDto.class), anyLong()))
			.willThrow(new BadRequestException("음식 가격을 입력해주세요"));

		//when
		ResultActions resultActions = createFood(foodRequestDto);

		//then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("음식 가격을 입력해주세요"));

	}

	@Test
	@DisplayName("음식 등록 음식 가격 음수인 경우 실패")
	void failCreateFoodByNegativePrice() throws Exception {
		//given
		FoodRequestDto foodRequestDto = new FoodRequestDto("치킨", -1000);
		given(foodService.createFood(anyString(), any(FoodRequestDto.class), anyLong()))
			.willThrow(new BadRequestException("음식 가격은 최소 0원 부터 입력가능합니다."));

		//when
		ResultActions resultActions = createFood(foodRequestDto);

		//then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("음식 가격은 최소 0원 부터 입력가능합니다."));

	}

	@Test
	@DisplayName("음식 목록 조회 성공")
	void successFindFoods() throws Exception {
		//given
		Long restaurantId = 1L;
		List<FoodResponseDto> expect = makeFoodResList();
		given(foodService.findAllFood(anyString(), anyLong()))
			.willReturn(expect);

		//when
		ResultActions resultActions = findFoods(restaurantId);

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
							.description("음식 이름"),
						fieldWithPath("[].price")
							.type(JsonFieldType.NUMBER)
							.description("음식 가격")
					)
				)
			)
			.andReturn();

		List<FoodResponseDto> foodResponseDtoList = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(),
			new TypeReference<List<FoodResponseDto>>() {
			}
		);
		assertThat(foodResponseDtoList.get(0).getId()).isEqualTo(1L);
		assertThat(foodResponseDtoList.get(1).getId()).isEqualTo(2L);
	}

	@Test
	@DisplayName("음식 조회 성공")
	void successFindFood() throws Exception {
		//given
		Long restaurantId = 1L;
		Long foodId = 1L;
		given(foodService.findFood(anyString(), anyLong(), anyLong()))
			.willReturn(new FoodResponseDto(foodId, "치킨", 10000));

		//when
		ResultActions resultActions = findFood(restaurantId, foodId);

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
							.description("음식 이름"),
						fieldWithPath("price")
							.type(JsonFieldType.NUMBER)
							.description("음식 가격")
					)
				)
			)
			.andReturn();

		FoodResponseDto foodResDto = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(),
			FoodResponseDto.class
		);
		assertThat(foodResDto.getId()).isEqualTo(foodId);
		assertThat(foodResDto.getName()).isEqualTo("치킨");
		assertThat(foodResDto.getPrice()).isEqualTo(10000);
	}

	@Test
	@DisplayName("음식 조회 시 음식점이 없는 경우 실패")
	void failFindFoodByNotFoundRestaurant() throws Exception {
		//given
		Long restaurantId = 1L;
		Long foodId = 1L;
		given(foodService.findFood(anyString(), anyLong(), anyLong()))
			.willThrow(new NotFoundException("식당을 찾을 수 없습니다."));

		//when
		ResultActions resultActions = findFood(restaurantId, foodId);

		//then
		resultActions
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("식당을 찾을 수 없습니다."));
	}

	@Test
	@DisplayName("음식 조회 시 주인이 아닌 경우 실패")
	void failFindFoodByUnAuth() throws Exception {
		//given
		Long restaurantId = 1L;
		Long foodId = 1L;
		given(foodService.findFood(anyString(), anyLong(), anyLong()))
			.willThrow(new UnauthorizedException("식당 주인만 사용할 수 있는 기능입니다."));

		//when
		ResultActions resultActions = findFood(restaurantId, foodId);

		//then
		resultActions
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.message").value("식당 주인만 사용할 수 있는 기능입니다."));
	}

	@Test
	@DisplayName("음식 조회 시 음식이 없는 경우 실패")
	void failFindFoodByNotFoundFood() throws Exception {
		//given
		Long restaurantId = 1L;
		Long foodId = 1L;
		given(foodService.findFood(anyString(), anyLong(), anyLong()))
			.willThrow(new NotFoundException("음식이 존재하지 않습니다."));

		//when
		ResultActions resultActions = findFood(restaurantId, foodId);

		//then
		resultActions
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("음식이 존재하지 않습니다."));
	}

	@Test
	@DisplayName("음식 정보 수정 성공")
	void successUpdateFood() throws Exception {
		//given
		FoodRequestDto requestDto = new FoodRequestDto("햄버거", 5000);
		Long restaurantId = 1L;
		Long foodId = 1L;
		doNothing().when(foodService).updateFood(anyString(), anyLong(), anyLong(), any(FoodRequestDto.class));

		//when
		ResultActions resultActions = updateFood(requestDto, restaurantId, foodId);

		//then
		resultActions
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					requestFields(
						fieldWithPath("name")
							.type(JsonFieldType.STRING)
							.description("음식 이름"),
						fieldWithPath("price")
							.type(JsonFieldType.NUMBER)
							.description("음식 가격")
					)
				)
			);
	}

	private ResultActions createFood(FoodRequestDto requestDto) throws Exception {
		return mockMvc.perform(post("/api/v1/restaurants/1/foods")
			.header(AUTHORIZATION, TOKEN_DTO.getAccessToken())
			.contentType(APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(requestDto)));
	}

	private ResultActions findFoods(Long restaurantId) throws Exception {
		return mockMvc.perform(get("/api/v1/restaurants/{restaurantId}/foods", restaurantId)
			.header(AUTHORIZATION, TOKEN_DTO.getAccessToken())
			.contentType(APPLICATION_JSON));
	}

	private ResultActions findFood(Long restaurantId, Long foodId) throws Exception {
		return mockMvc.perform(get("/api/v1/restaurants/{restaurantId}/foods/{foodId}", restaurantId, foodId)
			.header(AUTHORIZATION, TOKEN_DTO.getAccessToken())
			.contentType(APPLICATION_JSON));
	}

	private ResultActions updateFood(FoodRequestDto requestDto, Long restaurantId, Long foodId) throws Exception {
		return mockMvc.perform(put("/api/v1/restaurants/{restaurantId}/foods/{foodId}", restaurantId, foodId)
			.header(AUTHORIZATION, TOKEN_DTO.getAccessToken())
			.contentType(APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(requestDto)));
	}


	private List<FoodResponseDto> makeFoodResList() {
		List<FoodResponseDto> foodResponseDtoList = new ArrayList<>();
		foodResponseDtoList.add(new FoodResponseDto(1L, "치킨", 10000));
		foodResponseDtoList.add(new FoodResponseDto(2L, "햄버거", 5000));
		return foodResponseDtoList;
	}

}
