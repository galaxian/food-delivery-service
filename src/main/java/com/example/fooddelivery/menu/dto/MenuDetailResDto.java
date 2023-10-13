package com.example.fooddelivery.menu.dto;

import java.util.List;

import com.example.fooddelivery.menu.domain.Menu;
import com.example.fooddelivery.menu.domain.MenuStatus;

import lombok.Getter;

@Getter
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
}
