package com.example.fooddelivery.common.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.fooddelivery.common.interceptor.AuthInterceptor;
import com.example.fooddelivery.common.resolver.OwnerArgumentResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final AuthInterceptor authInterceptor;
	private final OwnerArgumentResolver ownerArgumentResolver;

	public WebConfig(AuthInterceptor authInterceptor,
		OwnerArgumentResolver ownerArgumentResolver) {
		this.authInterceptor = authInterceptor;
		this.ownerArgumentResolver = ownerArgumentResolver;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(ownerArgumentResolver);
	}
}
