package com.example.fooddelivery.order.dto;

import lombok.Getter;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class CreateOrderReqDto {
    @NotNull(message = "주문 시 연락받을 전화번호를 입력해주세요")
    private String phoneNumber;

    @NotEmpty(message = "주문 시 한개 이상의 메뉴를 선택해주세요")
    List<MenuQuantityReqDto> menuReqList;

    public CreateOrderReqDto(String phoneNumber, List<MenuQuantityReqDto> menuReqList) {
        this.phoneNumber = phoneNumber;
        this.menuReqList = menuReqList;
    }
}
