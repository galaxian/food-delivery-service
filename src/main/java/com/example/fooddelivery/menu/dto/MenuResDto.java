package com.example.fooddelivery.menu.dto;

import com.example.fooddelivery.menu.domain.Menu;
import com.example.fooddelivery.menu.domain.MenuStatus;

import lombok.Getter;

@Getter
public class MenuResDto {
	private Long id;
	private String name;
	private int price;
	private MenuStatus status;

	public MenuResDto(Menu menu) {
		this.id = menu.getId();
		this.name = menu.getName();
		this.price = menu.getPrice();
		this.status = menu.getMenuStatus();
	}

	public MenuResDto(Long id, String name, int price, MenuStatus status) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.status = status;
	}
}
