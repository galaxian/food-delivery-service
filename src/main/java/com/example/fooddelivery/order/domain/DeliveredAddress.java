package com.example.fooddelivery.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.example.fooddelivery.restaurant.domain.City;
import com.example.fooddelivery.restaurant.domain.State;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveredAddress {

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

	public DeliveredAddress(String  state, String city, String street, String etcAddress) {
		this.state = State.getEnumState(state);
		this.city = City.getEnumCity(city);
		this.street = street;
		this.etcAddress = etcAddress;
	}
}
