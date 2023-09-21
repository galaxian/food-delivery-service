package com.example.fooddelivery.order.dto;

import lombok.Getter;

import java.util.List;

import javax.validation.constraints.NotEmpty;

@Getter
public class CreateOrderReqDto {
    @NotEmpty(message = "주문 시 한개 이상의 메뉴를 선택해주세요")
    List<MenuQuantityReqDto> menuReqList;

    public CreateOrderReqDto(List<MenuQuantityReqDto> menuReqList) {
        this.menuReqList = menuReqList;
    }
}
