package com.example.fooddelivery.order.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CreateOrderReqDto {
    List<MenuQuantityReqDto> menuReqList;
}
