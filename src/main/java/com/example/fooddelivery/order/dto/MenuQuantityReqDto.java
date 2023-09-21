package com.example.fooddelivery.order.dto;

import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class MenuQuantityReqDto {
    @NotNull(message = "주문할 메뉴를 선택해주세요")
    private Long id;

    @NotNull(message = "주문할 메뉴 개수를 선택해주세요")
    private int quantity;

    public MenuQuantityReqDto(Long id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }
}
