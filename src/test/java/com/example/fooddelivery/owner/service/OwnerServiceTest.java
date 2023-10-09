package com.example.fooddelivery.owner.service;

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

import com.example.fooddelivery.common.exception.DuplicateException;
import com.example.fooddelivery.common.exception.PasswordEncoder;
import com.example.fooddelivery.owner.domain.Owner;
import com.example.fooddelivery.owner.dto.OwnerJoinReqDto;
import com.example.fooddelivery.owner.repository.OwnerRepository;

@ExtendWith(MockitoExtension.class)
class OwnerServiceTest {

	@Mock
	private OwnerRepository ownerRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private OwnerService ownerService;

	@Test
	@DisplayName("회원가입 성공")
	void joinOwner() {
		//given
		OwnerJoinReqDto reqDto = new OwnerJoinReqDto("abed1234", "xcvi0987");

		given(ownerRepository.findByIdentifier(any()))
			.willReturn(Optional.empty());
		given(ownerRepository.save(any()))
			.willReturn(new Owner(1L, reqDto.getIdentifier(), "encrypt-password"
				+ ""));
		given(passwordEncoder.generateSalt(any()))
			.willReturn("salt");
		given(passwordEncoder.encrypt(any(), any()))
			.willReturn("encrypt-password");

		//when
		//then
		assertDoesNotThrow(() -> ownerService.join(reqDto));

	}

	@Test
	@DisplayName("아이디 중복 회원가입 실패")
	void joinOwnerDuplicate() {
		//given
		OwnerJoinReqDto reqDto = new OwnerJoinReqDto("abed1234", "xcvi0987");

		given(ownerRepository.findByIdentifier(any()))
			.willReturn(Optional.of(new Owner(1L, "abed1234", "encrypt-password")));

		//when
		//then
		assertThatThrownBy(() -> ownerService.join(reqDto))
			.isInstanceOf(DuplicateException.class);

	}
}
