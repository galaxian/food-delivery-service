package com.example.fooddelivery.auth.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fooddelivery.auth.dto.LoginReqDto;
import com.example.fooddelivery.auth.dto.LoginResDto;
import com.example.fooddelivery.auth.service.AuthService;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@GetMapping("/login")
	public ResponseEntity<LoginResDto> login(@RequestBody @Valid LoginReqDto reqDto) {
		return ResponseEntity.ok(authService.login(reqDto));
	}
}
