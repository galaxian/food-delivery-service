package com.example.fooddelivery.menu.dto;

import java.util.List;

import com.example.fooddelivery.menu.domain.Menu;
import com.example.fooddelivery.menu.domain.MenuStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuDetailResDto {
	private Long id;
	private String name;
	private int price;
	private String describe;
	private MenuStatus status;

	private List<MenuFoodResDto> menuFoodList;

	public MenuDetailResDto(Menu menu, List<MenuFoodResDto> menuFoodList) {
		this.id = menu.getId();
		this.name = menu.getName();
		this.price = menu.getPrice();
		this.describe = menu.getDescribe();
		this.status = menu.getMenuStatus();
		this.menuFoodList = menuFoodList;
	}

	public MenuDetailResDto(Long id, String name, int price, String describe,
		MenuStatus status, List<MenuFoodResDto> menuFoodList) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.describe = describe;
		this.status = status;
		this.menuFoodList = menuFoodList;
	}
}
