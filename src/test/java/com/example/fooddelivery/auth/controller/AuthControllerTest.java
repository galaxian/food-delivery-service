package com.example.fooddelivery.auth.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.example.fooddelivery.auth.dto.LoginReqDto;
import com.example.fooddelivery.auth.dto.LoginResDto;
import com.example.fooddelivery.auth.service.AuthService;
import com.example.fooddelivery.common.AbstractRestDocsTest;

@WebMvcTest(AuthController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class AuthControllerTest extends AbstractRestDocsTest {

	@MockBean
	private AuthService authService;

	@Test
	@DisplayName("로그인 성공")
	void successLogin() throws Exception {
		//given
		LoginReqDto reqDto = new LoginReqDto("hackle1234", "swede!!123");
		given(authService.login(any(LoginReqDto.class)))
			.willReturn(new LoginResDto("accessToken"));

		//when
		ResultActions resultActions = login(reqDto);

		//then
		MvcResult mvcResult = resultActions
			.andExpect(status().isOk())
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
					responseFields(
						fieldWithPath("accessToken")
							.type(JsonFieldType.STRING)
							.description("access token")
					)
				)
			)
			.andReturn();

		LoginResDto loginResDto = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(),
			LoginResDto.class
		);

		assertThat(loginResDto.getAccessToken()).isEqualTo("accessToken");

	}

	private ResultActions login(LoginReqDto reqDto) throws Exception {
		return mockMvc.perform(get("/api/v1/login")
			.contentType(APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(reqDto)));
	}

}
