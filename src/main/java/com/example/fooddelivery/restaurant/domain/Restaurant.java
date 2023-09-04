package com.example.fooddelivery.restaurant.domain;

import com.example.fooddelivery.common.TimeStamped;
import com.example.fooddelivery.food.domain.Food;
import com.example.fooddelivery.menu.domain.Menu;
import com.example.fooddelivery.order.domain.Order;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "restaurants")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Restaurant extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "min_price")
    private int minPrice;

    @Column(name = "delivery_fee")
    private int deliveryFee;

    @OneToMany(mappedBy = "restaurant")
    @JoinColumn(name = "order_id")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    @JoinColumn(name = "menu_id")
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    @JoinColumn(name = "food_id")
    private List<Food> foods = new ArrayList<>();
}
