package com.example.fooddelivery.menu.dto;

import com.example.fooddelivery.menu.domain.Menu;
import com.example.fooddelivery.restaurant.domain.Restaurant;
import lombok.Getter;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class CreateMenuReqDto {

    @NotNull(message = "메뉴 이름을 입력해주세요")
    @Size(max = 50, message = "메뉴 이름은 50자를 초과할 수 없습니다.")
    private String name;

    @NotNull(message = "메뉴 가격을 입력해 주세요")
    @Min(value = 0, message = "메뉴 가격은 최소 0원부터 가능합니다.")
    private Integer price;

    @Size(max = 200, message = "메뉴 설명은 최대 200자 까지 가능합니다.")
    private String describe;

    @NotEmpty(message = "메뉴의 음식 및 수량은 최소 한개 이상 입력해주세요")
    private List<FoodQuantityReqDto> foodReqList;

    public CreateMenuReqDto(String name, int price, String describe,
        List<FoodQuantityReqDto> foodReqList) {
        this.name = name;
        this.price = price;
        this.describe = describe;
        this.foodReqList = foodReqList;
    }

    public Menu toEntity(Restaurant restaurant) {
        return Menu.createMenu(name, price, describe, restaurant);
    }
}
