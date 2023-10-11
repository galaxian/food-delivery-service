package com.example.fooddelivery.common.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.example.fooddelivery.auth.service.AuthService;
import com.example.fooddelivery.common.exception.UnauthorizedException;

@Component
public class OwnerArgumentResolver implements HandlerMethodArgumentResolver {

	private static final String BEARER = "Bearer ";

	private final AuthService authService;

	public OwnerArgumentResolver(AuthService authService) {
		this.authService = authService;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(String.class)
			&& parameter.hasParameterAnnotation(OwnerIdentifier.class);
	}

	@Override
	public String resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		String authorizationHeader = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
		checkHeader(authorizationHeader);
		String token = authorizationHeader.substring(BEARER.length());
		return authService.findIdentifierByToken(token);
	}

	private void checkHeader(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
			throw new UnauthorizedException("인증 헤더가 유효하지 않습니다.");
		}
	}
}
