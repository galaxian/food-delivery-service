package com.example.fooddelivery.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fooddelivery.auth.dto.LoginReqDto;
import com.example.fooddelivery.common.exception.UnauthorizedException;
import com.example.fooddelivery.common.PasswordEncoder;
import com.example.fooddelivery.owner.domain.Owner;
import com.example.fooddelivery.owner.repository.OwnerRepository;

@Service
public class AuthService {

	private final OwnerRepository ownerRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthService(OwnerRepository ownerRepository,
		PasswordEncoder passwordEncoder) {
		this.ownerRepository = ownerRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public void login(LoginReqDto reqDto) {
		Owner owner = findOwnerByIdentifier(reqDto.getIdentifier());
		String salt = passwordEncoder.generateSalt(reqDto.getPassword());
		String encryptPassword = passwordEncoder.encrypt(reqDto.getPassword(), salt);
		validPassword(owner, reqDto.getPassword());
		owner.isMissMatchPassword(encryptPassword);
	}

	private void validPassword(Owner owner, String password) {
		if (owner.isMissMatchPassword(encryptPassword(password))) {
			throw new UnauthorizedException("비밀번호가 일치하지 않습니다.");
		}
	}

	private String encryptPassword(String password) {
		String salt = passwordEncoder.generateSalt(password);
		return passwordEncoder.encrypt(password, salt);
	}

	private Owner findOwnerByIdentifier(String identifier) {
		return ownerRepository.findByIdentifier(identifier)
			.orElseThrow(() -> new UnauthorizedException("존재하지 않는 아이디입니다."));
	}
}
