package com.example.fooddelivery.owner.controller;

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

import com.example.fooddelivery.common.AbstractRestDocsTest;
import com.example.fooddelivery.common.exception.BadRequestException;
import com.example.fooddelivery.common.exception.DuplicateException;
import com.example.fooddelivery.owner.dto.OwnerJoinReqDto;
import com.example.fooddelivery.owner.service.OwnerService;

@WebMvcTest(OwnerController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class OwnerControllerTest extends AbstractRestDocsTest {

	@MockBean
	private OwnerService ownerService;

	@Test
	@DisplayName("회원가입 성공")
	void successJoin() throws Exception {
		//given
		OwnerJoinReqDto reqDto = new OwnerJoinReqDto("hackle5783", "utop8372!~");
		given(ownerService.join(any(OwnerJoinReqDto.class)))
			.willReturn(1L);

		//when
		ResultActions resultActions = joinOwner(reqDto);

		//then
		resultActions
			.andExpect(status().isCreated())
			.andExpect(header().string(LOCATION, "/api/v1/owners/1"))
			.andDo(
				restDocs.document(
					requestFields(
						fieldWithPath("identifier")
							.type(JsonFieldType.STRING)
							.description("아이디"),
						fieldWithPath("password")
							.type(JsonFieldType.STRING)
							.description("비밀번호")
					),
					responseHeaders(
						headerWithName(LOCATION).description("생성된 식당 주인 URL")
					)
				)
			);
	}

	@Test
	@DisplayName("아이디 중복으로 인한 회원가입 실패")
	void failJoinByDuplicateIdentifier() throws Exception {
		//given
		OwnerJoinReqDto reqDto = new OwnerJoinReqDto("duplicate12", "utop8372!~");
		given(ownerService.join(any(OwnerJoinReqDto.class)))
			.willThrow(new DuplicateException("이미 존재하는 아이디입니다."));

		//when
		ResultActions resultActions = joinOwner(reqDto);

		//then
		resultActions
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("$.message").value("이미 존재하는 아이디입니다."));
	}

	@Test
	@DisplayName("아이디 형식이 맞지 않는 경우 회원가입 실패")
	void failJoinByValidIdentifier() throws Exception {
		//given
		OwnerJoinReqDto reqDto = new OwnerJoinReqDto("아이디형식이틀림", "utop8372!~");
		given(ownerService.join(any(OwnerJoinReqDto.class)))
			.willThrow(new BadRequestException("영문자와 숫자를 포함한 아이디를 입력해주세요"));

		//when
		ResultActions resultActions = joinOwner(reqDto);

		//then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("영문자와 숫자를 포함한 아이디를 입력해주세요"));
	}

	private ResultActions joinOwner(OwnerJoinReqDto reqDto) throws Exception {
		return mockMvc.perform(post("/api/v1/owners")
			.contentType(APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(reqDto)));
	}

}
