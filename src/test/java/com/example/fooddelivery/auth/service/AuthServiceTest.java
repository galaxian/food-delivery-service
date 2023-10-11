package com.example.fooddelivery.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.fooddelivery.auth.dto.LoginReqDto;
import com.example.fooddelivery.auth.dto.LoginResDto;
import com.example.fooddelivery.common.JwtProvider;
import com.example.fooddelivery.common.PasswordEncoder;
import com.example.fooddelivery.common.exception.UnauthorizedException;
import com.example.fooddelivery.owner.domain.Owner;
import com.example.fooddelivery.owner.repository.OwnerRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	private static Owner owner = new Owner("abed1234", "encrypt", "salt");

	@Mock
	private OwnerRepository ownerRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtProvider jwtProvider;

	@InjectMocks
	private AuthService authService;

	@Test
	@DisplayName("로그인 성공")
	void login() {
		//given
		LoginReqDto reqDto = new LoginReqDto("abed1234", "xcvi0987");
		String accessToken = "accessToken";

		given(ownerRepository.findByIdentifier(any()))
			.willReturn(Optional.of(owner));
		given(passwordEncoder.encrypt(any(), any()))
			.willReturn("encrypt");
		given(jwtProvider.createAccessToken(any(), any()))
			.willReturn(accessToken);

		//when
		LoginResDto result = authService.login(reqDto);

		//then
		assertThat(result.getAccessToken()).isEqualTo(accessToken);

	}

	@Test
	@DisplayName("존재하지 않는 아이디로 로그인 실패")
	void loginNotFoundIdentifier() {
		//given
		LoginReqDto reqDto = new LoginReqDto("wrongId", "xcvi0987");

		given(ownerRepository.findByIdentifier(any()))
			.willReturn(Optional.empty());

		//when
		//then
		assertThatThrownBy(() -> authService.login(reqDto))
			.isInstanceOf(UnauthorizedException.class);

	}

	@Test
	@DisplayName("비밀번호 불일치로 로그인 실패")
	void loginNotMatchPassword() {
		//given
		LoginReqDto reqDto = new LoginReqDto("abed1234", "wrong");

		given(ownerRepository.findByIdentifier(any()))
			.willReturn(Optional.of(owner));
		given(passwordEncoder.encrypt(any(), any()))
			.willReturn("wrong-encrypt");

		//when
		//then
		assertThatThrownBy(() -> authService.login(reqDto))
			.isInstanceOf(UnauthorizedException.class);

	}
}
