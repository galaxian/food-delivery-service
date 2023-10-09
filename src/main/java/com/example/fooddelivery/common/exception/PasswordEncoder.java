package com.example.fooddelivery.common.exception;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {

	private static final String ALGORITHM = "SHA-256";

	public String generateSalt(String password) {
		SecureRandom secureRandom = new SecureRandom();
		byte[] value = new byte[password.length()];
		secureRandom.nextBytes(value);
		return Base64.getEncoder().encodeToString(value);
	}

	public String encrypt(String password, String salt) {
		MessageDigest messageDigest = findMessageDigest();
		messageDigest.update(salt.getBytes());
		messageDigest.update(password.getBytes());
		byte[] hashedPassword = messageDigest.digest();
		return Base64.getEncoder().encodeToString(hashedPassword);
	}

	private MessageDigest findMessageDigest() {
		try {
			return MessageDigest.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new NotFoundException("");
		}
	}
}
