package com.example.fooddelivery.order.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
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
import com.example.fooddelivery.common.exception.NotFoundException;
import com.example.fooddelivery.order.domain.OrderStatus;
import com.example.fooddelivery.order.dto.CreateOrderReqDto;
import com.example.fooddelivery.order.dto.GetAllOrderByPhoneReqDto;
import com.example.fooddelivery.order.dto.MenuQuantityReqDto;
import com.example.fooddelivery.order.dto.OrderDetailResDto;
import com.example.fooddelivery.order.dto.OrderMenuResDto;
import com.example.fooddelivery.order.service.OrderService;
import com.fasterxml.jackson.core.type.TypeReference;

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
		CreateOrderReqDto reqDto = new CreateOrderReqDto("010-1234-5678",
			"서울특별시",
			"서초구",
			"선릉",
			"기타주소",
			new ArrayList<>(
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
						fieldWithPath("state")
							.type(JsonFieldType.STRING)
							.description("도/특별시/광역시"),
						fieldWithPath("city")
							.type(JsonFieldType.STRING)
							.description("시/군/구"),
						fieldWithPath("street")
							.type(JsonFieldType.STRING)
							.description("로/대로"),
						fieldWithPath("etcAddress")
							.type(JsonFieldType.STRING)
							.description("상세주소"),
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
		CreateOrderReqDto reqDto = new CreateOrderReqDto("010-1234-5678","서울특별시",
			"서초구",
			"선릉",
			"기타주소",
		new ArrayList<>(
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

	@DisplayName("주문한 메뉴가 없는 경우 주문 생성 실패")
	@Test
	void failCreateOrderByNotFoundMenu() throws Exception {
		//given
		Long restaurantId = 1L;
		CreateOrderReqDto reqDto = new CreateOrderReqDto("010-1234-5678",
			"서울특별시",
			"서초구",
			"선릉",
			"기타주소",
			new ArrayList<>(
			Arrays.asList(
				new MenuQuantityReqDto(1L, 1),
				new MenuQuantityReqDto(2L, 1)
			)
		));
		given(orderService.createOrder(any(CreateOrderReqDto.class), anyLong()))
			.willThrow(new NotFoundException("메뉴를 찾을 수 없습니다."));

		//when
		ResultActions resultActions = createOrder(reqDto, restaurantId);

		//then
		resultActions
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("메뉴를 찾을 수 없습니다."));

	}

	@DisplayName("주문 조회 성공")
	@Test
	void successFindOrder() throws Exception {
		//given
		Long orderId = 1L;
		given(orderService.findOrder(anyLong()))
			.willReturn(new OrderDetailResDto(1L, 20000, LocalDateTime.now(), OrderStatus.WAITING,
				new ArrayList<>(Arrays.asList(
					new OrderMenuResDto(1L, "양념치킨", 10000, 1),
					new OrderMenuResDto(2L, "맛있는치킨", 10000, 1)
				))));

		//when
		ResultActions resultActions = findOrder(orderId);

		//then
		MvcResult mvcResult = resultActions
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					responseFields(
						fieldWithPath("id")
							.type(JsonFieldType.NUMBER)
							.description("주문 식별자"),
						fieldWithPath("totalPrice")
							.type(JsonFieldType.NUMBER)
							.description("주문 총액"),
						fieldWithPath("orderTime")
							.type(JsonFieldType.STRING)
							.description("주문 시간"),
						fieldWithPath("orderStatus")
							.type(JsonFieldType.VARIES)
							.description("주문 상태"),
						fieldWithPath("orderMenuList")
							.type(JsonFieldType.ARRAY)
							.description("주문 메뉴 리스트"),
						fieldWithPath("orderMenuList[].id")
							.type(JsonFieldType.NUMBER)
							.description("메뉴 식별자"),
						fieldWithPath("orderMenuList[].name")
							.type(JsonFieldType.STRING)
							.description("메뉴 이름"),
						fieldWithPath("orderMenuList[].price")
							.type(JsonFieldType.NUMBER)
							.description("메뉴 가격"),
						fieldWithPath("orderMenuList[].quantity")
							.type(JsonFieldType.NUMBER)
							.description("메뉴 갯수")
					)
				)
			)
			.andReturn();

		OrderDetailResDto resDto = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(),
			OrderDetailResDto.class
		);

		assertThat(resDto.getId()).isEqualTo(orderId);
		assertThat(resDto.getTotalPrice()).isEqualTo(20000);
		assertThat(resDto.getOrderStatus()).isEqualTo(OrderStatus.WAITING);
		assertThat(resDto.getOrderMenuList().get(0).getId()).isEqualTo(1L);
		assertThat(resDto.getOrderMenuList().get(0).getName()).isEqualTo("양념치킨");
		assertThat(resDto.getOrderMenuList().get(0).getPrice()).isEqualTo(10000);
		assertThat(resDto.getOrderMenuList().get(0).getQuantity()).isEqualTo(1);

	}

	@DisplayName("주문이 없는 경우 주문 조회 실패")
	@Test
	void failFindOrderByNotFoundOrder() throws Exception {
		//given
		Long orderId = 1L;
		given(orderService.findOrder(anyLong()))
			.willThrow(new NotFoundException("주문을 찾을 수 없습니다."));

		//when
		ResultActions resultActions = findOrder(orderId);

		//then
		resultActions
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("주문을 찾을 수 없습니다."));
	}

	@DisplayName("전화번호를 통한 주문 목록 조회 성공")
	@Test
	void successFindOrdersByPhone() throws Exception {
		//given
		GetAllOrderByPhoneReqDto reqDto = new GetAllOrderByPhoneReqDto("010-1234-5678");
		given(orderService.findAllOrderByPhoneNumber(any(GetAllOrderByPhoneReqDto.class)))
			.willReturn(new ArrayList<>(
				Arrays.asList(
				new OrderDetailResDto(1L, 20000, LocalDateTime.now(), OrderStatus.WAITING,
					new ArrayList<>(Arrays.asList(
						new OrderMenuResDto(1L, "양념치킨", 10000, 1),
						new OrderMenuResDto(2L, "맛있는치킨", 10000, 1)
					))),
				new OrderDetailResDto(2L, 5000, LocalDateTime.now(), OrderStatus.ACCEPT,
					new ArrayList<>(Arrays.asList(
						new OrderMenuResDto(3L, "떡볶이", 5000, 1)
					)))
			)));

		//when
		ResultActions resultActions = findOrdersByPhone(reqDto);

		//then
		MvcResult mvcResult = resultActions
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					requestFields(
						fieldWithPath("phoneNumber")
							.type(JsonFieldType.STRING)
							.description("전화번호")
					),
					responseFields(
						fieldWithPath("[].id")
							.type(JsonFieldType.NUMBER)
							.description("주문 식별자"),
						fieldWithPath("[].totalPrice")
							.type(JsonFieldType.NUMBER)
							.description("주문 총액"),
						fieldWithPath("[].orderTime")
							.type(JsonFieldType.STRING)
							.description("주문 시간"),
						fieldWithPath("[].orderStatus")
							.type(JsonFieldType.VARIES)
							.description("주문 상태"),
						fieldWithPath("[].orderMenuList")
							.type(JsonFieldType.ARRAY)
							.description("주문 메뉴 리스트"),
						fieldWithPath("[].orderMenuList[0].id")
							.type(JsonFieldType.NUMBER)
							.description("메뉴 식별자"),
						fieldWithPath("[].orderMenuList[0].name")
							.type(JsonFieldType.STRING)
							.description("메뉴 이름"),
						fieldWithPath("[].orderMenuList[0].price")
							.type(JsonFieldType.NUMBER)
							.description("메뉴 가격"),
						fieldWithPath("[].orderMenuList[0].quantity")
							.type(JsonFieldType.NUMBER)
							.description("메뉴 갯수")
					)
				)
			)
			.andReturn();

		List<OrderDetailResDto> resDto = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(),
			new TypeReference<List<OrderDetailResDto>>() {
			}
		);

		assertThat(resDto.get(1).getId()).isEqualTo(2L);
		assertThat(resDto.get(1).getTotalPrice()).isEqualTo(5000);
		assertThat(resDto.get(1).getOrderStatus()).isEqualTo(OrderStatus.ACCEPT);
		assertThat(resDto.get(1).getOrderMenuList().get(0).getId()).isEqualTo(3L);
		assertThat(resDto.get(1).getOrderMenuList().get(0).getName()).isEqualTo("떡볶이");
		assertThat(resDto.get(1).getOrderMenuList().get(0).getPrice()).isEqualTo(5000);
		assertThat(resDto.get(1).getOrderMenuList().get(0).getQuantity()).isEqualTo(1);

	}

	private ResultActions createOrder(CreateOrderReqDto reqDto, Long restaurantId) throws Exception {
		return mockMvc.perform(post("/api/v1/restaurants/{restaurantId}/orders", restaurantId)
			.contentType(APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(reqDto)));
	}

	private ResultActions findOrder(Long orderId) throws Exception{
		return mockMvc.perform(get("/api/v1/orders/{orderId}", orderId)
			.contentType(APPLICATION_JSON));
	}

	private ResultActions findOrdersByPhone(GetAllOrderByPhoneReqDto reqDto) throws Exception{
		return mockMvc.perform(get("/api/v1/orders/phone")
			.contentType(APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(reqDto)));
	}

}
