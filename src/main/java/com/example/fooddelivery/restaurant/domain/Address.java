package com.example.fooddelivery.restaurant.domain;

import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

	private State state;
	private City city;
	private String street;
	private String etcAddress;

	public Address(State state, City city, String street, String etcAddress) {
		this.state = state;
		this.city = city;
		this.street = street;
		this.etcAddress = etcAddress;
	}
}
