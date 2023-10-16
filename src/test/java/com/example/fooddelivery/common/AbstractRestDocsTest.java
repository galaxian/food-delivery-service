package com.example.fooddelivery.common;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.example.fooddelivery.common.interceptor.AuthInterceptor;
import com.example.fooddelivery.common.resolver.OwnerArgumentResolver;
import com.fasterxml.jackson.databind.ObjectMapper;

@Import(RestDocsConfiguration.class)
@ExtendWith(RestDocumentationExtension.class)
public abstract class AbstractRestDocsTest {

	@Autowired
	protected RestDocumentationResultHandler restDocs;

	@Autowired
	protected MockMvc mockMvc;

	@MockBean
	protected AuthInterceptor authInterceptor;

	@MockBean
	protected OwnerArgumentResolver ownerArgumentResolver;

	@Autowired
	protected ObjectMapper objectMapper;

	@BeforeEach
	void setUp(
		final WebApplicationContext context,
		final RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.apply(documentationConfiguration(restDocumentation))
			.alwaysDo(MockMvcResultHandlers.print())
			.alwaysDo(restDocs)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.build();
	}

	@BeforeEach
	void authSetUp() {
		given(authInterceptor.preHandle(any(), any(), any()))
			.willReturn(true);
		given(ownerArgumentResolver.resolveArgument(any(), any(), any(), any()))
			.willReturn("Bearer token");
		given(ownerArgumentResolver.supportsParameter(any()))
			.willReturn(true);
	}
}
