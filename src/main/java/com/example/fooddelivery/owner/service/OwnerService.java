package com.example.fooddelivery.owner.service;

import org.springframework.stereotype.Service;

import com.example.fooddelivery.common.exception.DuplicateException;
import com.example.fooddelivery.common.PasswordEncoder;
import com.example.fooddelivery.owner.domain.Owner;
import com.example.fooddelivery.owner.dto.OwnerJoinReqDto;
import com.example.fooddelivery.owner.repository.OwnerRepository;

@Service
public class OwnerService {

	private final OwnerRepository ownerRepository;
	private final PasswordEncoder passwordEncoder;

	public OwnerService(OwnerRepository ownerRepository,
		PasswordEncoder passwordEncoder) {
		this.ownerRepository = ownerRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public Long join(OwnerJoinReqDto reqDto) {
		validIdentifier(reqDto.getIdentifier());
		String salt = passwordEncoder.generateSalt(reqDto.getPassword());
		String encryptPassword = passwordEncoder.encrypt(reqDto.getPassword(), salt);

		Owner owner = new Owner(reqDto.getIdentifier(), encryptPassword, salt);
		Owner saveOwner = ownerRepository.save(owner);
		return saveOwner.getId();
	}

	private void validIdentifier(String identifier)  {
		if (isDuplicateIdentifier(identifier)) {
			throw new DuplicateException("이미 존재하는 아이디입니다.");
		}
	}

	private boolean isDuplicateIdentifier(String identifier) {
		return ownerRepository.findByIdentifier(identifier).isPresent();
	}
}
