package com.example.fooddelivery.restaurant.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private State state;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private City city;

	@Column(nullable = false)
	private String street;

	@Column(nullable = false)
	private String etcAddress;

	public Address(String state, String city, String street, String etcAddress) {
		this.state = State.getEnumState(state);
		this.city = City.getEnumCity(city);
		this.street = street;
		this.etcAddress = etcAddress;
	}

	@Override
	public String toString() {
		return state.getState() + city.getCity() + getStreet() + getEtcAddress();
	}
}
