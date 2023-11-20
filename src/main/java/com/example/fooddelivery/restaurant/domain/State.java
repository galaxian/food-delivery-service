package com.example.fooddelivery.restaurant.domain;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum State {
	SEOUL("서울특별시");

	private String state;

	State(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public static State getEnumState(String state) {
		return Arrays.stream(State.values())
			.filter(s -> s.getState().equals(state))
			.sequential()
			.collect(Collectors.toList())
			.get(0);
	}
}
