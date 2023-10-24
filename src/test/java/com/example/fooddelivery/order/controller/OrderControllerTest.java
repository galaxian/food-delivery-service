package com.example.fooddelivery.order.controller;

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
import com.example.fooddelivery.order.dto.CreateOrderReqDto;
import com.example.fooddelivery.order.dto.MenuQuantityReqDto;
import com.example.fooddelivery.order.service.OrderService;

@WebMvcTest(OrderController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class OrderControllerTest extends AbstractRestDocsTest {

	private static final LoginResDto TOKEN_DTO = new LoginResDto("accessToken");

	@MockBean
	private OrderService orderService;

	@DisplayName("주문 생성 성공")
	@Test
	void successCreateOrder() throws Exception {
		//given
		Long restaurantId = 1L;
		CreateOrderReqDto reqDto = new CreateOrderReqDto("010-1234-5678", new ArrayList<>(
			Arrays.asList(
				new MenuQuantityReqDto(1L, 1),
				new MenuQuantityReqDto(2L, 1)
			)
		));
		given(orderService.createOrder(any(CreateOrderReqDto.class), anyLong()))
			.willReturn(1L);

		//when
		ResultActions resultActions = createOrder(reqDto, restaurantId);

		//then
		resultActions
			.andExpect(status().isCreated())
			.andExpect(header().string(LOCATION, "/api/v1/orders/1"))
			.andDo(
				restDocs.document(
					requestFields(
						fieldWithPath("phoneNumber")
							.type(JsonFieldType.STRING)
							.description("전화번호"),
						fieldWithPath("menuReqList")
							.type(JsonFieldType.ARRAY)
							.description("주문 메뉴 리스트"),
						fieldWithPath("menuReqList[].id")
							.type(JsonFieldType.NUMBER)
							.description("메뉴 식별자"),
						fieldWithPath("menuReqList[].quantity")
							.type(JsonFieldType.NUMBER)
							.description("메뉴 갯수")
					),
					responseHeaders(
						headerWithName(LOCATION).description("생성된 주문 URL")
					)
				)
			);

	}

	@DisplayName("식당이 없는 경우 주문 생성 실패")
	@Test
	void failCreateOrder() throws Exception {
		//given
		Long restaurantId = 1L;
		CreateOrderReqDto reqDto = new CreateOrderReqDto("010-1234-5678", new ArrayList<>(
			Arrays.asList(
				new MenuQuantityReqDto(1L, 1),
				new MenuQuantityReqDto(2L, 1)
			)
		));
		given(orderService.createOrder(any(CreateOrderReqDto.class), anyLong()))
			.willThrow(new NotFoundException("식당을 찾을 수 없습니다."));

		//when
		ResultActions resultActions = createOrder(reqDto, restaurantId);

		//then
		resultActions
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("식당을 찾을 수 없습니다."));

	}

	private ResultActions createOrder(CreateOrderReqDto reqDto, Long restaurantId) throws Exception {
		return mockMvc.perform(post("/api/v1/restaurants/{restaurantId}/orders", restaurantId)
			.contentType(APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(reqDto)));
	}

}
