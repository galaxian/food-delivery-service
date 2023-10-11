package com.example.fooddelivery.auth.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fooddelivery.auth.dto.LoginReqDto;
import com.example.fooddelivery.auth.dto.LoginResDto;
import com.example.fooddelivery.common.JwtProvider;
import com.example.fooddelivery.common.exception.UnauthorizedException;
import com.example.fooddelivery.common.PasswordEncoder;
import com.example.fooddelivery.owner.domain.Owner;
import com.example.fooddelivery.owner.repository.OwnerRepository;

@Service
public class AuthService {

	private final OwnerRepository ownerRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	public AuthService(OwnerRepository ownerRepository,
		PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
		this.ownerRepository = ownerRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtProvider = jwtProvider;
	}

	@Transactional(readOnly = true)
	public LoginResDto login(LoginReqDto reqDto) {
		Owner owner = findOwnerByIdentifier(reqDto.getIdentifier());
		validPassword(owner, reqDto.getPassword());
		return makeLoginResDto(owner);
	}

	private LoginResDto makeLoginResDto(Owner owner) {
		String accessToken = jwtProvider.createAccessToken(
			owner.getIdentifier(), new HashMap<>());
		return new LoginResDto(accessToken);
	}

	private void validPassword(Owner owner, String password) {
		if (owner.isMissMatchPassword(encryptPassword(password, owner.getSalt()))) {
			throw new UnauthorizedException("비밀번호가 일치하지 않습니다.");
		}
	}

	private String encryptPassword(String password, String salt) {;
		return passwordEncoder.encrypt(password, salt);
	}

	private Owner findOwnerByIdentifier(String identifier) {
		return ownerRepository.findByIdentifier(identifier)
			.orElseThrow(() -> new UnauthorizedException("존재하지 않는 아이디입니다."));
	}

	public boolean isVerifyToken(String token) {
		return jwtProvider.isValidToken(token);
	}

	public String findIdentifierByToken(String token) {
		return jwtProvider.findSubject(token);
	}
}
