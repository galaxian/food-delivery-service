package com.example.fooddelivery.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.fooddelivery.auth.service.AuthService;
import com.example.fooddelivery.common.exception.UnauthorizedException;

@Component
public class AuthInterceptor implements HandlerInterceptor {

	private static final String BEARER = "Bearer ";

	private final AuthService authService;

	public AuthInterceptor(AuthService authService) {
		this.authService = authService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
		Object handler) {

		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		if (handlerMethod.hasMethodAnnotation(Authenticated.class)) {
			String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
			checkHeader(authorizationHeader);
			String token = authorizationHeader.substring(BEARER.length());
			verifyToken(token);
		}
		return true;
	}

	private void checkHeader(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
			throw new UnauthorizedException("인증 헤더가 적절하지 않습니다.");
		}
	}

	private void verifyToken(String token) {
		if (!authService.isVerifyToken(token)) {
			throw new UnauthorizedException("토큰이 유효하지 않습니다.");
		}
	}
}
