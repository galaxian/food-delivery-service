package com.example.fooddelivery.owner.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.fooddelivery.common.TimeStamped;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "owners")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Owner extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "identifiers", unique = true, nullable = false)
	private String identifier;

	@Column(name = "password", nullable = false)
	private String encryptPassword;

	@Column(name = "salts", nullable = false)
	private String salt;

	public Owner(String identifier, String encryptPassword, String salt) {
		this.identifier = identifier;
		this.encryptPassword = encryptPassword;
		this.salt = salt;
	}

	public boolean isMissMatchPassword(String password) {
		return !this.encryptPassword.equals(password);
	}
}
