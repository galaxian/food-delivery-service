package com.example.fooddelivery.ordermenu.domain;

import com.example.fooddelivery.common.TimeStamped;
import com.example.fooddelivery.menu.domain.Menu;
import com.example.fooddelivery.order.domain.Order;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "order_menu")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderMenu extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private OrderMenu(int quantity, int price, Order order, Menu menu) {
        this.quantity = quantity;
        this.price = price;
        this.order = order;
        this.menu = menu;
    }

    public static OrderMenu createOrderMenu(int quantity, int price, Order order, Menu menu) {
        return new OrderMenu(quantity, price, order, menu);
    }
}
