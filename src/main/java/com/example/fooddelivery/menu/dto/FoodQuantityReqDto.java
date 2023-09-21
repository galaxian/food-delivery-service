package com.example.fooddelivery.menu.dto;

import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class FoodQuantityReqDto {

    @NotNull
    private Long id;

    @NotNull(message = "음식 수량을 입력해주세요")
    private Integer quantity;

    public FoodQuantityReqDto(Long id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }
}
