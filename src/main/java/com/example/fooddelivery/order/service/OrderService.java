package com.example.fooddelivery.order.service;

import com.example.fooddelivery.common.exception.NotFoundException;
import com.example.fooddelivery.menu.domain.Menu;
import com.example.fooddelivery.menu.repository.MenuRepository;
import com.example.fooddelivery.order.domain.Order;
import com.example.fooddelivery.order.dto.CreateOrderReqDto;
import com.example.fooddelivery.order.dto.MenuQuantityReqDto;
import com.example.fooddelivery.order.repository.OrderRepository;
import com.example.fooddelivery.ordermenu.domain.OrderMenu;
import com.example.fooddelivery.ordermenu.repository.OrderMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderMenuRepository orderMenuRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, MenuRepository menuRepository, OrderMenuRepository orderMenuRepository) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.orderMenuRepository = orderMenuRepository;
    }

    @Transactional
    public Long createOrder(CreateOrderReqDto reqDto) {
        Order saveOrder = orderRepository.save(Order.createOrder(LocalDateTime.now()));
        return saveOrder.getId();
    }
}
