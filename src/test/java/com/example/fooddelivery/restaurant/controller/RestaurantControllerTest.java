package com.example.fooddelivery.restaurant.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import com.example.fooddelivery.restaurant.dto.CreateRestaurantReqDto;
import com.example.fooddelivery.restaurant.service.RestaurantService;

@WebMvcTest(RestaurantController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class RestaurantControllerTest extends AbstractRestDocsTest {

	private static final LoginResDto TOKEN_DTO = new LoginResDto("accessToken");

	@MockBean
	private RestaurantService restaurantService;

	@Test
	@DisplayName("식당 등록 성공")
	void successCreateRestaurant() throws Exception {
		//given
		CreateRestaurantReqDto reqDto = new CreateRestaurantReqDto("치킨집", 10000, 3000);
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
							.description("배달비")
					),
					responseHeaders(
						headerWithName(LOCATION).description("생성된 식당 URL")
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

}
