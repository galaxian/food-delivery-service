package com.example.fooddelivery.food.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class FoodRequestDto {

    @NotNull(message = "음식 이름을 입력해주세요")
    @Size(max = 50, message = "음식 이름은 50자를 넘을 수 없습니다.")
    private String name;

    @NotNull(message = "음식 가격을 입력해주세요")
    @Min(value = 0, message = "음식 가격은 최소 0원 부터 입력가능합니다.")
    private Integer price;

    public FoodRequestDto(String name, Integer price) {
        this.name = name;
        this.price = price;
    }
}
